package com.fantasy.policy_search_server.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.ObjectBuilder;
import com.alibaba.fastjson.JSONObject;
import com.fantasy.policy_search_server.algorithm.CountryAlgorithmModel;
import com.fantasy.policy_search_server.algorithm.RegionAlgorithmModel;
import com.fantasy.policy_search_server.common.BusinessException;
import com.fantasy.policy_search_server.common.EmBusinessError;
import com.fantasy.policy_search_server.dao.PolicyDBModelMapper;
import com.fantasy.policy_search_server.model.PolicyDBModel;
import com.fantasy.policy_search_server.model.PolicyModel;
import com.fantasy.policy_search_server.request.RequestModel;
import com.fantasy.policy_search_server.model.SuggestModel;
import com.fantasy.policy_search_server.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class PolicyServiceImpl implements PolicyService {
    @Autowired
    private ElasticsearchClient client;
    @Autowired
    private PolicyDBModelMapper policyDBModelMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public Map<String, Object> searchESRegionScript(RequestModel requestModel) throws IOException {
        RegionAlgorithmModel algorithmModel = new RegionAlgorithmModel();
        if (requestModel.getProvince().equals("*")) algorithmModel.setProvince(requestModel.getProvince());
        if (requestModel.getCity().equals("*")) algorithmModel.setCity(requestModel.getCity());
        if (requestModel.getCounty().equals("*")) algorithmModel.setCounty(requestModel.getCounty());
        algorithmModel.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()));
        algorithmModel.setDistanceWeight(0.2);
        algorithmModel.setTimeWeight(0.3);
        algorithmModel.setTitleWeight(0.5);
        List<co.elastic.clients.elasticsearch._types.SortOptions> list = new ArrayList<>();
        if (requestModel.getSort_by().equals("time")) {
            list.add(SortOptions.of(s -> s.field(FieldSort.of(f -> f.field("pub_time").order(SortOrder.Desc)))));
        }
        Function<RangeQuery.Builder, ObjectBuilder<RangeQuery>> finalFn = setTimeLimit(requestModel);
        List<Query> fnPosition = setPosition(requestModel);
        Function<Builder, ObjectBuilder<Query>> finalFnAgency = getFinalFnAgency(requestModel);
        SearchResponse<PolicyModel> searchResponse = client.search(s -> s.index("policy-v1")
                        .query(q -> q
                                .scriptScore(s1 -> s1
                                        .query(q1 -> q1.
                                                bool(b -> b
                                                        .must(m -> m.
                                                                bool(b1 -> b1.
                                                                        should(Query.of(s2 -> s2.match(m1 -> m1.field("title").query(requestModel.getKeyword()))),
                                                                                Query.of(s3 -> s3.match(m1 -> m1.field("body").query(requestModel.getKeyword()))))))
                                                        .mustNot(m -> m.term(u -> u.field("grade").value("国家级")))
                                                        .must(m -> m.wildcard(w -> w.field("type").value(requestModel.getType())))
                                                        .must(finalFnAgency)
                                                        .must(m -> m.range(finalFn))
                                                        .must(fnPosition)
                                                )
                                        ).script(s4 -> s4.inline(i -> i.source(algorithmModel.getTitleScoreScript()).params(algorithmModel.getParams())))
                                )
                        ).highlight(h -> h
                                .fields("title", f -> f
                                        .preTags("<font color='red'>").postTags("</font>"))
                                .fields("body", f -> f
                                        .preTags("<font color='red'>").postTags("</font>"))
                        ).sort(list)
                        .from((requestModel.getPage_no() - 1) * requestModel.getPage_size())
                        .size(requestModel.getPage_size())
                , PolicyModel.class);

        List<PolicyModel> policyModelList = new ArrayList<PolicyModel>();
        List<Hit<PolicyModel>> hits = searchResponse.hits().hits();

        for (Hit<PolicyModel> hit : hits) {
            PolicyModel model = hit.source();
            if (model != null) {
                String highLightTitle;
                String highLightBody;
                if (hit.highlight().get("title") != null) {
                    model.setNo_html_title(model.getTitle());
                    highLightTitle = hit.highlight().get("title").get(0);
                    model.setTitle(highLightTitle);
                }
                if (hit.highlight().get("body") != null) {
                    highLightBody = hit.highlight().get("body").get(0);
                    model.setBody(highLightBody);
                }
                policyModelList.add(model);
            }
        }
        Long total = searchResponse.hits().total().value();
        Map<String, Object> res = new HashMap<>();
        res.put("data", setView(policyModelList));
        res.put("total", total);
        return res;
    }

    @Override
    public Map<String, Object> searchESCountryScript(RequestModel requestModel) throws IOException {
        CountryAlgorithmModel algorithmModel = new CountryAlgorithmModel();
        algorithmModel.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()));
        algorithmModel.setTimeWeight(0.3);
        algorithmModel.setTitleWeight(0.7);
        List<co.elastic.clients.elasticsearch._types.SortOptions> list = new ArrayList<>();
        if (requestModel.getSort_by().equals("time")) {
            list.add(SortOptions.of(s -> s.field(FieldSort.of(f -> f.field("pub_time").order(SortOrder.Desc)))));
        }
        Function<RangeQuery.Builder, ObjectBuilder<RangeQuery>> finalFn = setTimeLimit(requestModel);
        Function<Builder, ObjectBuilder<Query>> finalFnAgency = getFinalFnAgency(requestModel);
        SearchResponse<PolicyModel> searchResponse = client.search(s -> s.index("policy-v1")
                        .query(q -> q
                                .scriptScore(s1 -> s1
                                        .query(q1 -> q1.
                                                bool(b -> b
                                                        .must(m -> m.
                                                                bool(b1 -> b1.
                                                                        should(Query.of(s2 -> s2.match(m1 -> m1.field("title").query(requestModel.getKeyword()))),
                                                                                Query.of(s3 -> s3.match(m1 -> m1.field("body").query(requestModel.getKeyword()))))))
                                                        .must(m -> m.term(u -> u.field("grade").value("国家级")))
                                                        .must(m -> m.wildcard(w -> w.field("type").value(requestModel.getType())))
                                                        .must(finalFnAgency)
                                                        .must(m -> m.range(finalFn))
                                                )
                                        ).script(s4 -> s4.inline(i -> i.source(algorithmModel.getTitleScoreScript()).params(algorithmModel.getParams())))
                                )
                        ).highlight(h -> h
                                .fields("title", f -> f
                                        .preTags("<font color='red'>").postTags("</font>"))
                                .fields("body", f -> f
                                        .preTags("<font color='red'>").postTags("</font>"))
                        ).sort(list)
                        .from((requestModel.getPage_no() - 1) * requestModel.getPage_size())
                        .size(requestModel.getPage_size())
                , PolicyModel.class);

        List<PolicyModel> policyModelList = new ArrayList<PolicyModel>();
        List<Hit<PolicyModel>> hits = searchResponse.hits().hits();
        for (Hit<PolicyModel> hit : hits) {
            PolicyModel model = hit.source();
            if (model != null) {
                String highLightTitle;
                String highLightBody;
                if (hit.highlight().get("title") != null) {
                    model.setNo_html_title(model.getTitle());
                    highLightTitle = hit.highlight().get("title").get(0);
                    model.setTitle(highLightTitle);
                }
                if (hit.highlight().get("body") != null) {
                    highLightBody = hit.highlight().get("body").get(0);
                    model.setBody(highLightBody);
                }
                policyModelList.add(model);
            }
        }
        Long total = searchResponse.hits().total().value();
        Map<String, Object> res = new HashMap<>();
        res.put("data", setView(policyModelList));
        res.put("total", total);
        return res;
    }

    @Override
    public Map<String, Object> searchESAllScript(RequestModel requestModel) throws IOException {
        List<co.elastic.clients.elasticsearch._types.SortOptions> list = new ArrayList<>();
        if (requestModel.getSort_by().equals("time")) {
            list.add(SortOptions.of(s -> s.field(FieldSort.of(f -> f.field("pub_time").order(SortOrder.Desc)))));
        }
        Function<RangeQuery.Builder, ObjectBuilder<RangeQuery>> finalFn = setTimeLimit(requestModel);
        Function<Builder, ObjectBuilder<Query>> finalFnAgency = getFinalFnAgency(requestModel);
        SearchResponse<PolicyModel> searchResponse = client.search(s -> s.index("policy-v1")
                        .query(q -> q
                                .bool(b -> b
                                        .must(m -> m.
                                                bool(b1 -> b1.
                                                        should(Query.of(s2 -> s2.match(m1 -> m1.field("title").query(requestModel.getKeyword()))),
                                                                Query.of(s3 -> s3.match(m1 -> m1.field("body").query(requestModel.getKeyword()))))))
                                        .must(m -> m.term(u -> u.field("grade").value("国家级")))
                                        .must(m -> m.wildcard(w -> w.field("type").value(requestModel.getType())))
                                        .must(finalFnAgency)
                                        .must(m -> m.range(finalFn))
                                )
                        ).highlight(h -> h
                                .fields("title", f -> f
                                        .preTags("<font color='red'>").postTags("</font>"))
                                .fields("body", f -> f
                                        .preTags("<font color='red'>").postTags("</font>"))
                        ).sort(list)
                        .from((requestModel.getPage_no() - 1) * requestModel.getPage_size())
                        .size(requestModel.getPage_size())
                , PolicyModel.class);
        List<PolicyModel> policyModelList = new ArrayList<PolicyModel>();
        List<Hit<PolicyModel>> hits = searchResponse.hits().hits();
        for (Hit<PolicyModel> hit : hits) {
            PolicyModel model = hit.source();
            if (model != null) {
                String highLightTitle;
                String highLightBody;
                if (hit.highlight().get("title") != null) {
                    model.setNo_html_title(model.getTitle());
                    highLightTitle = hit.highlight().get("title").get(0);
                    model.setTitle(highLightTitle);
                }
                if (hit.highlight().get("body") != null) {
                    highLightBody = hit.highlight().get("body").get(0);
                    model.setBody(highLightBody);
                }
                policyModelList.add(model);
            }
        }
        Long total = searchResponse.hits().total().value();
        Map<String, Object> res = new HashMap<>();
        res.put("data", setView(policyModelList));
        res.put("total", total);
        return res;
    }

    @Override
    public int countAllPolicy() {
        return policyDBModelMapper.countAllPolicy();
    }

    @Override
    public List<PolicyDBModel> selectAll() {
        return policyDBModelMapper.selectAll();
    }

    @Override
    public List<PolicyModel> getNewRank() {
        Set<Object> policy_id = redisTemplate.opsForZSet().reverseRange("policyRank", 0, 9);
        List<PolicyModel> policyModelList = new ArrayList<>();
        for (Object i : policy_id) {
            String id = i.toString().split(":")[1];
            Object o = redisTemplate.opsForValue().get("rank:" + id);
            PolicyModel policyModel = JSONObject.parseObject(JSONObject.toJSONString(o), PolicyModel.class);
            if (policyModel != null) {
                policyModelList.add(policyModel);
                policyModel.setView(redisTemplate.opsForZSet().score("policyRank", "policyId:" + policyModel.getId()).intValue());
            }
        }
        policyModelList.sort((a, b) -> b.getView() - a.getView());
        return policyModelList;
    }

    @Override
    public PolicyModel getRankByNum(Integer num) {
        List<PolicyModel> newRank = this.getNewRank();
        if (CollectionUtils.isEmpty(newRank) || num > newRank.size()) {
            return null;
        }
        return newRank.get(num - 1);
    }

    @Override
    public Map<String, Object> searchES(String keyword, int page_no, int page_size) throws IOException {
        SearchResponse<PolicyModel> searchResponse = client.search(s -> s.index("policy-v1")
                        .query(q -> q
                                .scriptScore(s1 -> s1
                                        .query(q1 -> q1.
                                                bool(b -> b
                                                        .should(
                                                                Query.of(s2 -> s2.match(m -> m.field("title").query(keyword))),
                                                                Query.of(s3 -> s3.match(m -> m.field("body").query(keyword)))
                                                        )
                                                )
                                        ).script(s4 -> s4.inline(i -> i.source("_score*0.5 + 20*0.3+30*0.2")))
                                )
                        ).highlight(h -> h
                                .fields("title", f -> f
                                        .preTags("<font color='red'>").postTags("</font>"))
                                .fields("body", f -> f
                                        .preTags("<font color='red'>").postTags("</font>"))
                        ).from((page_no - 1) * page_size)
                        .size(page_size)
                , PolicyModel.class);
        List<PolicyModel> policyModelList = new ArrayList<PolicyModel>();
        List<Hit<PolicyModel>> hits = searchResponse.hits().hits();

        for (Hit<PolicyModel> hit : hits) {
            PolicyModel model = hit.source();
            if (model != null) {
                String highLightTitle;
                String highLightBody;
                if (hit.highlight().get("title") != null) {
                    model.setNo_html_title(model.getTitle());
                    highLightTitle = hit.highlight().get("title").get(0);
                    model.setTitle(highLightTitle);
                }
                if (hit.highlight().get("body") != null) {
                    highLightBody = hit.highlight().get("body").get(0);
                    model.setBody(highLightBody);
                }
                policyModelList.add(model);
            }
        }
        Long total = searchResponse.hits().total().value();
        Map<String, Object> res = new HashMap<>();
        res.put("data", setView(policyModelList));
        res.put("total", total);
        return res;
    }

    @Override
    public List<SuggestModel> searchSug(String prefix) throws IOException {
        SearchResponse<SuggestModel> searchResponse = client.search(s -> s.index("title_suggest")
                        .query(q -> q
                                .bool(b -> b
                                        .should(
                                                Query.of(s1 -> s1.matchPhrasePrefix(m -> m.field("title").query(prefix)))
                                        )
                                )
                        )
                , SuggestModel.class);
        List<SuggestModel> suggestModelList = new ArrayList<SuggestModel>();
        List<Hit<SuggestModel>> hits = searchResponse.hits().hits();
        for (Hit<SuggestModel> hit : hits) {
            SuggestModel model = hit.source();
            model.setHtmlTitle(addHighLight(model.getTitle(), prefix, "<strong>", "</strong>"));
            suggestModelList.add(model);
        }
        return suggestModelList;
    }

    @Override
    public PolicyModel getPolicy(String id) throws IOException, BusinessException {
        SearchResponse<PolicyModel> searchResponse = client.search(s -> s.index("policy-v1")
                        .query(q -> q.term(t -> t
                                .field("id").value(id))
                        )
                , PolicyModel.class);
        List<Hit<PolicyModel>> hits = searchResponse.hits().hits();
        if (hits.size() != 0) {
            PolicyModel res = hits.get(0).source();
            return res;
        } else {
            throw new BusinessException(EmBusinessError.POLICY_DO_NOT_EXIT);
        }
    }

    public void getRank() throws BusinessException, IOException {
        Set<Object> policy_id = redisTemplate.opsForZSet().reverseRange("policyRank", 0, 9);
        int r = 1;
        for (Object i : policy_id) {
            String id = i.toString().split(":")[1];
            Object o = redisTemplate.opsForValue().get("rank:" + r);
            if (o == null) {
                redisTemplate.opsForValue().set("rank:" + r, getPolicy(id));
                //三天后更新一次
                redisTemplate.expire("rank:" + r, 3, TimeUnit.HOURS);
            }
            r++;
        }
    }

    public void saveViewToRedis(String policyId, Long userId) {
        //排行榜
        //一周后更新一次
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long timeOut = (calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;

        //记录浏览量
        redisTemplate.opsForHyperLogLog().add("policyId:" + policyId, userId);
        int a_view = redisTemplate.opsForHyperLogLog().size("policyId:" + policyId).intValue();
        redisTemplate.expire("policyId:" + policyId, timeOut, TimeUnit.SECONDS);

        //存储一周的排行榜
        Double score = redisTemplate.opsForZSet().score("policyRank", "policyId:" + policyId);
        if (score == null) {
            redisTemplate.opsForZSet().addIfAbsent("policyRank", "policyId:" + policyId, a_view);
            redisTemplate.expire("policyRank", timeOut, TimeUnit.SECONDS);
        } else
            redisTemplate.opsForZSet().add("policyRank", "policyId:" + policyId, a_view);//存在就只更新值
    }

    @Override
    public List<PolicyModel> testSearch(String title) throws IOException {
        SearchResponse<PolicyModel> searchResponse = client.search(s -> s.index("policy-v1")
                        .query(q -> q
                                .scriptScore(s2 -> s2
                                        .query(q1 -> q1
                                                .match(a -> a
                                                        .field("title").query(title))
                                        ).script(s1 -> s1.inline(i -> i.source("_score*0.5 + 20*0.3+30*0.2")))
                                )
                        )
                , PolicyModel.class);
        return null;
    }

    private String addHighLight(String ori, String target, String pre, String post) {
        StringBuffer sb = new StringBuffer(ori);
        int p = 0;
        while ((p = sb.indexOf(target, p)) != -1) {
            sb.insert(p + target.length(), post);
            sb.insert(p, pre);
            p += p + target.length() + pre.length() + post.length();
        }
        return sb.toString();
    }

    private Function<RangeQuery.Builder, ObjectBuilder<RangeQuery>> setTimeLimit(RequestModel requestModel) {
        Function<RangeQuery.Builder, ObjectBuilder<RangeQuery>> fn = null;
        if (requestModel.getTime_from().equals("*") && requestModel.getTime_to().equals("*")) {
            requestModel.setTime_from("2018/01/01");
            requestModel.setTime_to("2022/09/01");
            fn = r -> r.field("pub_time").gte(JsonData.of("2018/01/01")).lte(JsonData.of("2022/09/01"));
        } else {
            if (!requestModel.getTime_from().equals("*") && !requestModel.getTime_from().equals("*")) {
                fn = r -> r.field("pub_time")
                        .gte(JsonData.of(requestModel.getTime_from())).lte(JsonData.of(requestModel.getTime_to()));
            } else if (!requestModel.getTime_from().equals("*")) {
                fn = r -> r.field("pub_time")
                        .gte(JsonData.of(requestModel.getTime_from()));
            } else {
                fn = r -> r.field("pub_time")
                        .lte(JsonData.of(requestModel.getTime_to()));
            }
        }
        return fn;
    }

    private List<Query> setPosition(RequestModel requestModel) {
        List<Query> fns = new ArrayList<>();
        if (!requestModel.getProvince().equals("*")) {
            fns.add(Query.of(q -> q.term(u -> u.field("province").value(requestModel.getProvince()))));
            if (!requestModel.getCity().equals("*")) {
                fns.add(Query.of(m -> m.term(u -> u.field("city").value(requestModel.getCity()))));
                if (!requestModel.getCounty().equals("*")) {
                    fns.add(Query.of(m -> m.term(u -> u.field("county").value(requestModel.getCounty()))));
                }
            }
        }
        return fns;
    }

    private Function<Builder, ObjectBuilder<Query>> getFinalFnAgency(RequestModel requestModel) {
        Function<Builder, ObjectBuilder<Query>> fnAgency = m -> m.wildcard(w -> w.field("agency_full_name").value(requestModel.getAgency()));
        if (!requestModel.getAgency().equals("*")) {
            fnAgency = m -> m.term(t -> t.field("agency_full_name").value(requestModel.getAgency()));
        }
        return fnAgency;
    }

    private List<PolicyModel> setView(List<PolicyModel> list) {
        for (PolicyModel p : list) {
            p.setView(policyDBModelMapper.getViewByID(Long.parseLong(p.getId())));
        }
        return list;
    }
}

//        构建查询请求
//        final SearchRequest.Builder searchRequestBuilder = new
//                SearchRequest.Builder().index("policy");
//        MatchQuery matchQuery = new MatchQuery.Builder().field("title")
//                .query(FieldValue.of(keyword)).field("body").query(FieldValue.of(keyword)).build();
//        Query query = new Query.Builder().match(matchQuery).build();
//        searchRequestBuilder.query(query);
//        SearchRequest searchRequest = searchRequestBuilder.build();