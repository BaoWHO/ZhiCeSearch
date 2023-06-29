package com.fantasy.policy_search_server.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.MsearchRequest;
import co.elastic.clients.elasticsearch.core.MsearchResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.msearch.MultiSearchResponseItem;
import co.elastic.clients.elasticsearch.core.msearch.RequestItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.alibaba.fastjson.JSONObject;
import com.fantasy.policy_search_server.common.BusinessException;
import com.fantasy.policy_search_server.dao.SearchHistoryModelMapper;
import com.fantasy.policy_search_server.model.PolicyModel;
import com.fantasy.policy_search_server.model.SearchHistoryModel;
import com.fantasy.policy_search_server.model.UserModel;
import com.fantasy.policy_search_server.service.PolicyService;
import com.fantasy.policy_search_server.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@Service
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    private ElasticsearchClient client;
    @Autowired
    SearchHistoryModelMapper searchHistoryModelMapper;
    @Autowired
    private PolicyService policyService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public List<PolicyModel> recommendToNoLogin(int size) throws BusinessException, IOException {
        policyService.getRank();
//        List<PolicyModel> policyModelList=new ArrayList<>();
//        for(int r=1;r<11;r++) {
//            Object o = redisTemplate.opsForValue().get("rank:"+r);
//            PolicyModel policyModel = JSONObject.parseObject(JSONObject.toJSONString(o), PolicyModel.class);
//            if (policyModel != null) policyModelList.add(policyModel);
//        }
        return policyService.getNewRank();
    }

    @Override
    public List<PolicyModel> recommendToLogged(UserModel userModel, int size) throws IOException, BusinessException {
        Map<String, Integer> keywordNumMap = getKeywordNumMap(userModel, size);
        if (keywordNumMap.size() == 0) {
            return recommendToNoLogin(size);
        }
        MsearchRequest.Builder builder = getBulkRequest(keywordNumMap);
        //向es请求
        MsearchResponse<PolicyModel> mSearchResponse = client.msearch(builder.build(), PolicyModel.class);
        List<PolicyModel> policyModelList = getPolicyListFromResponse(mSearchResponse);
        return doFilterByShuffle(policyModelList, size);
    }

    public Map<String, Integer> getKeywordNumMap(UserModel userModel, int size) {
        SearchHistoryModel historyModel = searchHistoryModelMapper.selectByUserId(userModel.getId());
        Map<String, Integer> keywordNumMap = new HashMap<>();
        if (historyModel != null && historyModel.getKeyword() != null && !historyModel.getKeyword().equals("")) {
            Map<String, Integer> wordsNumMap = new HashMap<>();
            String[] historyWordsWithNum = historyModel.getKeyword().split("::");
            for (String wordWithNum : historyWordsWithNum) {
                String[] wordAndNum = wordWithNum.split(",");
                wordsNumMap.put(wordAndNum[0], Integer.parseInt(wordAndNum[1]));
            }
            List<Map.Entry<String, Integer>> wordNumList = new ArrayList<>(wordsNumMap.entrySet());
            wordNumList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
            int count = 0, sum = 0;
            // 10 5,5,4,3,2,2,1
            Random random = new Random();
            for (Map.Entry<String, Integer> entry : wordNumList)
                sum += entry.getValue();
            List<Integer> randomList = new ArrayList<>();
            for (int i = 0; i < size; i++)
                randomList.add(random.nextInt(sum) + 1);
            Collections.sort(randomList);
            for (int i = 0, j = 0, now = 0; i < wordNumList.size() && j < randomList.size(); i++) {
                now += wordNumList.get(i).getValue();
                String word = wordNumList.get(i).getKey();
                int num = 0;
                while (j < randomList.size() && randomList.get(j) <= now) {
                    num++;
                    j++;
                }
                keywordNumMap.put(word, num);
            }
        }
        return keywordNumMap;
    }

    public MsearchRequest.Builder getBulkRequest(Map<String, Integer> keywordNumMap) throws IOException {
        List<RequestItem> requestItems = new ArrayList<>();
        for (Map.Entry<String, Integer> entry: keywordNumMap.entrySet()) {
            RequestItem.Builder riBuilder = new RequestItem.Builder();
            riBuilder.header(h -> h.index("policy-v1"));
            riBuilder.body(b -> b
                    .query(q -> q
                            .multiMatch(m -> m
                                    .query(entry.getKey())
                                    .type(TextQueryType.MostFields)
                                    .fields("title^5", "body")
                            )
                    ).from(0).size(entry.getValue() *2)
            );
            requestItems.add(riBuilder.build());
        }
        MsearchRequest.Builder builder = new MsearchRequest.Builder();
        builder.index("policy-v1").searches(requestItems);
        return builder;
    }

    public List<PolicyModel> getPolicyListFromResponse(MsearchResponse<PolicyModel> mSearchResponse) {
        List<PolicyModel> policyModelList = new ArrayList<>();
        List<MultiSearchResponseItem<PolicyModel>> responses = mSearchResponse.responses();
        for (MultiSearchResponseItem<PolicyModel> item: responses) {
            List<Hit<PolicyModel>> hitList = item.result().hits().hits();
            for (Hit<PolicyModel> hit: hitList) {
                policyModelList.add(hit.source());
            }
        }
        return policyModelList;
    }

    public List<PolicyModel> doFilterByShuffle(List<PolicyModel> ori, int size) throws BusinessException, IOException {
        Collections.shuffle(ori);
        if (ori.size() >= size) {
            return ori.subList(0, size);
        } else {
            ori.addAll(recommendToNoLogin(size - ori.size()));
            return ori;
        }
    }
}
