package com.fantasy.policy_search_server.dao;

import com.fantasy.policy_search_server.model.SearchHistoryModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchHistoryModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SearchHistoryModel row);

    int insertSelective(SearchHistoryModel row);

    SearchHistoryModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SearchHistoryModel row);

    int updateByPrimaryKey(SearchHistoryModel row);

    SearchHistoryModel selectByUserId(Long userId);
}