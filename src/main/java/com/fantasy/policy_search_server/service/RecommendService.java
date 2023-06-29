package com.fantasy.policy_search_server.service;

import com.fantasy.policy_search_server.common.BusinessException;
import com.fantasy.policy_search_server.model.PolicyModel;
import com.fantasy.policy_search_server.model.UserModel;

import java.io.IOException;
import java.util.List;

public interface RecommendService {
    List<PolicyModel> recommendToNoLogin(int size) throws BusinessException, IOException;

    List<PolicyModel> recommendToLogged(UserModel userModel, int size) throws IOException, BusinessException;
}
