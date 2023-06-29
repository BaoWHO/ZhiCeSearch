package com.fantasy.policy_search_server.service;

import com.fantasy.policy_search_server.common.BusinessException;
import com.fantasy.policy_search_server.model.UserModel;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {
    UserModel login(String phone, String password)
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException;

    String register(UserModel userModel)
            throws BusinessException,UnsupportedEncodingException,NoSuchAlgorithmException;

    String delete(String phone) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException;

    int countAllUser();

    List<UserModel> selectAll();

    int create(UserModel userModel) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException;

    int deleteById(Long id) throws BusinessException;
}
