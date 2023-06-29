package com.fantasy.policy_search_server.service;

import com.fantasy.policy_search_server.model.UserModel;

import java.io.IOException;

public interface SearchHistoryService {
    void addHistory(UserModel userModel, String keyword) throws IOException;
}
