package com.fantasy.policy_search_server.controller.admin;

import com.fantasy.policy_search_server.common.AdminPermission;
import com.fantasy.policy_search_server.common.BusinessException;
import com.fantasy.policy_search_server.common.EmBusinessError;
import com.fantasy.policy_search_server.service.PolicyService;
import com.fantasy.policy_search_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Admin用户控制器类
 * @author orange
 * @version 1.0
 * @since 2023-02-01
 */
@Controller("/admin/admin")
@RequestMapping("/admin/admin")
public class AdminController {
    @Value("${admin.email}")
    private String email;
    @Value("${admin.encryptPassword}")
    private String encryptPassword;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private UserService userService;
    @Autowired
    private PolicyService policyService;

    /**
     * 登录页面控制
     * @return 返回登陆页面
     */
    @RequestMapping("/login_page")
    public ModelAndView loginPage(){
        ModelAndView modelAndView = new ModelAndView("/admin/admin/login");
        return modelAndView;
    }

    /**
     * 登录接口
     * @param email admin用户的邮箱
     * @param password admin用户的密码
     * @return 返回值页面跳转到主页
     * @throws BusinessException 参数不匹配、用户不存在、密码错误
     * @throws UnsupportedEncodingException 编码不支持
     * @throws NoSuchAlgorithmException 加密算法不存在
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestParam(name="email")String email,
                        @RequestParam(name="password")String password )
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户名密码不能为空");
        }
        if(email.equals(this.email) && encodeByMd5(password).equals(this.encryptPassword)){
            //登录成功
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute(session.getId(), email);
            return "redirect:/admin/admin/index";
        }else{
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户名密码错误");
        }
    }

    /**
     * 主页页面
     * @return 返回值主页面
     */
    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("/admin/admin/index");
        modelAndView.addObject("userCount", userService.countAllUser());
        modelAndView.addObject("policyCount", policyService.countAllPolicy());
        modelAndView.addObject("CONTROLLER_NAME", "admin");
        modelAndView.addObject("ACTION_NAME", "index");
        return modelAndView;
    }

    /**
     * 加密算法
     * @param str 需要加密的字符串
     * @return 返回值页面跳转到主页
     * @throws UnsupportedEncodingException 编码不支持
     * @throws NoSuchAlgorithmException 加密算法不存在
     */
    private String encodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确认计算方法MD5
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(messageDigest.digest(str.getBytes("utf-8")));
    }
}
