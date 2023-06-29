package com.fantasy.policy_search_server.controller.admin;

import com.fantasy.policy_search_server.common.AdminPermission;
import com.fantasy.policy_search_server.common.BusinessException;
import com.fantasy.policy_search_server.common.CommonRes;
import com.fantasy.policy_search_server.model.UserModel;
import com.fantasy.policy_search_server.request.PageQuery;
import com.fantasy.policy_search_server.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Controller("/admin/user")
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(PageQuery pageQuery){
        PageHelper.startPage(pageQuery.getPage(),pageQuery.getSize());
        List<UserModel> userModelList = userService.selectAll();
        PageInfo<UserModel> userModelPageInfo = new PageInfo<>(userModelList);
        ModelAndView modelAndView = new ModelAndView("/admin/user/index");
        modelAndView.addObject("data", userModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME","user");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
    }

    @RequestMapping("/create_page")
    @AdminPermission
    public ModelAndView createPage(){
        ModelAndView modelAndView = new ModelAndView("/admin/user/create");
        modelAndView.addObject("CONTROLLER_NAME","user");
        modelAndView.addObject("ACTION_NAME","create");
        return modelAndView;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @AdminPermission
    public String create(UserModel userModel) throws BusinessException,
            UnsupportedEncodingException, NoSuchAlgorithmException {
        userModel.setLatitude(new BigDecimal("0.0"));
        userModel.setLongitude(new BigDecimal("0.0"));
        userModel.setCreateAt(new Date());
        userModel.setUpdatedAt(new Date());
        userService.create(userModel);
        return "redirect:/admin/user/index";
    }

    @RequestMapping("/delete")
    @ResponseBody
    @AdminPermission
    public CommonRes delete(Long id) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        return CommonRes.create(userService.deleteById(id));
    }
}
