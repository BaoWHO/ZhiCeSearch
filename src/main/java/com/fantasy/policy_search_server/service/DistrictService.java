package com.fantasy.policy_search_server.service;

import com.fantasy.policy_search_server.request.DistrictReq;
import com.fantasy.policy_search_server.response.DistrictRes;

public interface DistrictService {
    DistrictRes getDistrict(DistrictReq req);
}
