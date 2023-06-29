package com.fantasy.policy_search_server.controller;

import com.fantasy.policy_search_server.common.*;
import com.fantasy.policy_search_server.model.UserModel;
import com.fantasy.policy_search_server.request.*;
import com.fantasy.policy_search_server.service.UserService;
import com.fantasy.policy_search_server.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller("/api/user")
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    @ResponseBody
    public CommonRes login(@Valid @RequestBody LoginReq loginReq,
                           BindingResult bindingResult)
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    CommonUtil.processErrorString(bindingResult));
        }
        UserModel userModel = userService.login(loginReq.getPhone(), loginReq.getPassword());
        String token = TokenUtil.generate(userModel);
        System.out.println(token);
        TokenUtil.save(token, userModel);
        Map<String, Object> res = new HashMap<>();
        res.put("data", userModel);
        res.put("token", token);
        return CommonRes.create(res);
    }

    @RequestMapping("/loginout")
    @ResponseBody
    public CommonRes loginout(@Valid @RequestBody LogoutReq logoutReq,
                              BindingResult bindingResult) throws BusinessException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    CommonUtil.processErrorString(bindingResult));
        }
        System.out.println(logoutReq.getToken());
        UserModel userModel = (UserModel) TokenUtil.getData(logoutReq.getToken());
        if (userModel != null) {
            TokenUtil.invalidate(logoutReq.getToken());
        } else {
            throw new BusinessException(EmBusinessError.DO_NOT_LOGIN);
        }
        return CommonRes.create("登出成功");
    }

    @RequestMapping("/register")
    @ResponseBody
    public CommonRes register(@Valid @RequestBody RegisterReq registerReq,
                              BindingResult bindingResult)
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    CommonUtil.processErrorString(bindingResult));
        }
        UserModel userModel = new UserModel(3L, registerReq.getPhone(), registerReq.getPassword(),
                registerReq.getNickName(), registerReq.getGender(), registerReq.getEmail(),
                registerReq.getCareer(), registerReq.getLongitude(), registerReq.getLatitude(),
                new Date(), new Date());
        return CommonRes.create(userService.register(userModel));
    }

    @RequestMapping("/unsubscribe")
    @ResponseBody
    public CommonRes unsubscribe(@Valid @RequestBody UnsubscribeReq unsubscribeReq,
                                 BindingResult bindingResult)
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    CommonUtil.processErrorString(bindingResult));
        }
        UserModel userModel = (UserModel) TokenUtil.getData(unsubscribeReq.getToken());
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.DO_NOT_LOGIN);
        }
        return CommonRes.create(userService.delete(userModel.getPhone()));
    }

    @RequestMapping("/is_login")
    @ResponseBody
    public CommonRes isLogin(@Valid @RequestBody IsLoginReq isLoginReq,
                             BindingResult bindingResult)
            throws BusinessException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    CommonUtil.processErrorString(bindingResult));
        }
        UserModel userModel = (UserModel) TokenUtil.getData(isLoginReq.getToken());
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.DO_NOT_LOGIN);
        }
        return CommonRes.create("已登陆");
    }
}