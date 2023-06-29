package com.fantasy.policy_search_server.service;

import com.fantasy.policy_search_server.common.BusinessException;
import com.fantasy.policy_search_server.model.PolicyDBModel;
import com.fantasy.policy_search_server.model.PolicyModel;
import com.fantasy.policy_search_server.request.RequestModel;
import com.fantasy.policy_search_server.model.SuggestModel;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PolicyService {
    Map<String, Object> searchES(String keyword, int page_no, int page_size) throws IOException;

    List<SuggestModel> searchSug(String prefix) throws IOException;

    PolicyModel getPolicy(String id) throws IOException, BusinessException;

    void getRank() throws BusinessException, IOException;

    void saveViewToRedis(String policyId, Long userId);

    List<PolicyModel> testSearch(String title) throws IOException;

    public Map<String, Object> searchESRegionScript(RequestModel requestModel) throws IOException;

    public Map<String, Object> searchESCountryScript(RequestModel requestModel) throws IOException;

    public Map<String, Object> searchESAllScript(RequestModel requestModel) throws IOException;

    int countAllPolicy();

    List<PolicyDBModel> selectAll();

    List<PolicyModel> getNewRank();

    /**
     * 根据名词获取推荐第几条数据
     *
     * @param num
     * @return
     */
    PolicyModel getRankByNum(Integer num);
}
