package com.fantasy.policy_search_server.service.impl;

import com.fantasy.policy_search_server.common.BusinessException;
import com.fantasy.policy_search_server.common.EmBusinessError;
import com.fantasy.policy_search_server.dao.UserModelMapper;
import com.fantasy.policy_search_server.model.UserModel;
import com.fantasy.policy_search_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserModelMapper userModelMapper;

    @Override
    public UserModel login(String phone, String password)
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        UserModel userModel = userModelMapper.selectByPhoneAndPassword(phone, encodeByMd5(password));
        //System.out.println(phone + " " + encodeByMd5(password));
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.LOGIN_FAIL);
        }
        userModel.setPassword("*");
        return userModel;
    }

    @Override
    public String register(UserModel userModel)
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if(userModelMapper.selectByPhone(userModel.getPhone())!=null) {
            throw new BusinessException(EmBusinessError.REGISTER_DUP_FAIL);
        }
        userModel.setPassword(encodeByMd5(userModel.getPassword()));
        userModelMapper.addUser(userModel);
        return "注册成功";
    }

    @Override
    public String delete(String phone) {
        userModelMapper.deleteByPhone(phone);
        return "注销成功";
    }

    @Override
    public int countAllUser() {
        return userModelMapper.countAllUser();
    }

    @Override
    public List<UserModel> selectAll() {
        return userModelMapper.selectAll();
    }

    @Override
    public int create(UserModel userModel) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if(userModelMapper.selectByPhone(userModel.getPhone()) != null) {
            throw new BusinessException(EmBusinessError.REGISTER_DUP_FAIL);
        }
        userModel.setPassword(encodeByMd5(userModel.getPassword()));
        return userModelMapper.addUser(userModel);
    }

    @Override
    public int deleteById(Long id) throws BusinessException {
        if(userModelMapper.selectByPrimaryKey(id) == null) {
            throw new BusinessException(EmBusinessError.USER_DO_NOT_EXIST);
        }
        return userModelMapper.deleteByPrimaryKey(id);
    }


    private String encodeByMd5(String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(messageDigest.digest(str.getBytes(StandardCharsets.UTF_8)));
    }
}
