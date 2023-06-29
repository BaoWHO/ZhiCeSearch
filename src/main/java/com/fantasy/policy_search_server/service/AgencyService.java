package com.fantasy.policy_search_server.service;

import com.fantasy.policy_search_server.model.AgencyModel;
import com.fantasy.policy_search_server.model.AgencySuggestModel;
import com.fantasy.policy_search_server.request.AgencyFilterReq;

import java.io.IOException;
import java.util.List;

public interface AgencyService {
    List<AgencySuggestModel> agencySug(String prefix) throws IOException;

    List<AgencyModel> agencyFilter(AgencyFilterReq agencyFilterReq) throws IOException;
}
