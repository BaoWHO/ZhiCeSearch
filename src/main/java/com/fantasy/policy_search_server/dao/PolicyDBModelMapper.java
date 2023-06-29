package com.fantasy.policy_search_server.dao;

import com.fantasy.policy_search_server.model.PolicyDBModel;
import com.fantasy.policy_search_server.model.PolicyModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PolicyDBModelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PolicyDBModel row);

    int insertSelective(PolicyDBModel row);

    PolicyDBModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PolicyDBModel row);

    int updateByPrimaryKey(PolicyDBModel row);

    int insertNormally(PolicyDBModel row);

    int getViewByID(Long id);

    int addView(@Param("id") Long id, @Param("view") Integer view);

    int countAllPolicy();

    List<PolicyDBModel> selectAll();
}