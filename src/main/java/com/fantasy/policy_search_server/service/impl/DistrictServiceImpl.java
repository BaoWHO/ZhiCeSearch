package com.fantasy.policy_search_server.service.impl;

import com.fantasy.policy_search_server.dao.DistrictModelMapper;
import com.fantasy.policy_search_server.model.DistrictModel;
import com.fantasy.policy_search_server.request.DistrictReq;
import com.fantasy.policy_search_server.response.DistrictRes;
import com.fantasy.policy_search_server.response.ValueLabel;
import com.fantasy.policy_search_server.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    DistrictModelMapper districtModelMapper;

    @Override
    public DistrictRes getDistrict(DistrictReq req) {
        DistrictRes res = new DistrictRes();
        res.setProvince(new ArrayList<>());
        res.setCity(new ArrayList<>());
        res.setCounty(new ArrayList<>());
        List<DistrictModel> models;
        if (req.getProvince().equals("*")) {
            //获取所有省级
            models = districtModelMapper.getAllProvinces();
            for (DistrictModel m: models) {
                res.getProvince().add(new ValueLabel(m.getName(), m.getName()));
            }
        } else if (req.getCity().equals("*")) {
            //根据省获取省下的市级
            int provinceId = districtModelMapper.getModelByName(req.getProvince()).getId();
            models = districtModelMapper.getModelByPid(provinceId);
            res.getProvince().add(new ValueLabel(req.getProvince(),req.getProvince()));
            for (DistrictModel m: models) {
                res.getCity().add(new ValueLabel(m.getName(), m.getName()));
            }
        } else {
            //根据市获取市下的区级
            int cityId = districtModelMapper.getModelByName(req.getCity()).getId();
            models = districtModelMapper.getModelByPid(cityId);
            res.getProvince().add(new ValueLabel(req.getProvince(), req.getProvince()));
            res.getCity().add(new ValueLabel(req.getCity(), req.getCity()));
            for (DistrictModel m: models) {
                res.getCounty().add(new ValueLabel(m.getName(), m.getName()));
            }
        }
        return res;
    }
}
