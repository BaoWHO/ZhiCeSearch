package com.fantasy.policy_search_server.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;
import com.fantasy.policy_search_server.dao.SearchHistoryModelMapper;
import com.fantasy.policy_search_server.model.SearchHistoryModel;
import com.fantasy.policy_search_server.model.UserModel;
import com.fantasy.policy_search_server.service.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {
    @Autowired
    private ElasticsearchClient client;
    @Autowired
    SearchHistoryModelMapper searchHistoryModelMapper;
    @Override
    public void addHistory(UserModel userModel, String keyword) throws IOException {
        //删除停用词
        keyword = deleteStopWord(keyword);
        //es对关键词分词
        List<String> kws = getSplitKeyword(keyword);
        //数据库获取用户搜索历史关键词
        SearchHistoryModel historyModel = searchHistoryModelMapper.selectByUserId(userModel.getId());
        //新老合并
        Map<String, Integer> wordsNumMap = new HashMap<>();
        if (historyModel != null) {
            if (historyModel.getKeyword() != null && !historyModel.getKeyword().equals("")) {
                String[] historyWordsWithNum = historyModel.getKeyword().split("::");
                for (String wordWithNum : historyWordsWithNum) {
                    String[] wordAndNum = wordWithNum.split(",");
                    wordsNumMap.put(wordAndNum[0], Integer.parseInt(wordAndNum[1]));
                }
            }
            //新老合并成一个String, 保留前500位
            String saveString = newWordsToMapToString(kws, wordsNumMap);
            historyModel.setKeyword(saveString);
            searchHistoryModelMapper.updateByPrimaryKey(historyModel);
        } else {
            String saveString = newWordsToMapToString(kws, wordsNumMap);
            SearchHistoryModel saveModel = new SearchHistoryModel(
                    0, userModel.getId(), saveString);
            searchHistoryModelMapper.insert(saveModel);
        }
    }

    public String newWordsToMapToString(List<String> kws, Map<String, Integer> wordsNumMap) {
        for (String newWord : kws) {
            if (wordsNumMap.containsKey(newWord)) {
                wordsNumMap.put(newWord, wordsNumMap.get(newWord) + 1);
            } else {
                wordsNumMap.put(newWord, 1);
            }
        }
        List<Map.Entry<String, Integer>> wordNumList = new ArrayList<>(wordsNumMap.entrySet());
        wordNumList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        StringBuilder saveStringBuilder = new StringBuilder();
        int count = 0;
        for (Map.Entry<String, Integer> entry: wordNumList) {
            if (count >= 500)
                break;
            saveStringBuilder.append(entry.getKey()).append(",").append(entry.getValue()).append("::");
            count++;
        }
        return saveStringBuilder.toString();
    }

    public String deleteStopWord(String keyword) {
        Character[] stopWords = new Character[]{'也','了','仍','从','以','使','则','却','又',
                '及','对','就','并','很','或','把','是','的','着','给','而','被','让','在','还',
                '比','等','当','与','于','但'};
        Set<Character> set = Arrays.stream(stopWords).collect(Collectors.toSet());
        StringBuilder sb = new StringBuilder(keyword);
        for (int i = 0; i < sb.length(); i++) {
            if (set.contains(sb.charAt(i)))
                sb.deleteCharAt(i);
        }
        return sb.toString();
    }

    public List<String> getSplitKeyword(String keyword) throws IOException {
        AnalyzeResponse analyzeResponse = client.indices().analyze(a -> a
                .analyzer("ik_smart").text(keyword)
        );
        List<String> ret = new ArrayList<>();
        for (AnalyzeToken analyzeToken: analyzeResponse.tokens()) {
            ret.add(analyzeToken.token());
        }
        return ret;
    }
}
