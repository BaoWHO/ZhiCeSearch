package com.fantasy.policy_search_server.dao;

import com.fantasy.policy_search_server.model.DistrictModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DistrictModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistrictModel row);

    int insertSelective(DistrictModel row);

    DistrictModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistrictModel row);

    int updateByPrimaryKey(DistrictModel row);

    List<DistrictModel> getAllProvinces();

    DistrictModel getModelByName(String name);

    List<DistrictModel> getModelByPid(Integer pid);
}