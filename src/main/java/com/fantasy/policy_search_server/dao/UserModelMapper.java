package com.fantasy.policy_search_server.dao;

import com.fantasy.policy_search_server.model.UserModel;
import com.fantasy.policy_search_server.request.RegisterReq;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserModelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserModel row);

    int insertSelective(UserModel row);

    UserModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserModel row);

    int updateByPrimaryKey(UserModel row);

    UserModel selectByPhoneAndPassword(@Param("phone")String phone, @Param("password")String password);

    UserModel selectByPhone(@Param("phone")String phone);

    int addUser(UserModel user);

    int deleteByPhone(@Param("phone")String phone);

    int countAllUser();

    List<UserModel> selectAll();
}