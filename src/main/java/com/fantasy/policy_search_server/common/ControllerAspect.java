package com.fantasy.policy_search_server.common;

import com.fantasy.policy_search_server.utils.EmailUtil;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Configuration
@Log4j2
public class ControllerAspect {
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private HttpServletResponse httpServletResponse;

    @Around("(execution(* com.fantasy.policy_search_server.controller.admin.*.*(..)) || execution(* com.fantasy.policy_search_server.controller.*.*(..))) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object adminControllerBeforeValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String remoteAddr = request.getRemoteAddr();
        String methodName = joinPoint.getSignature().getName();
        String requestUrl = getRequestUrl(joinPoint);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = formatter.format(new Date());
        Object[] args = joinPoint.getArgs();
        StringBuilder argsStr = new StringBuilder();
        for (Object obj: args) {
            if (!(obj instanceof BindingResult))
                argsStr.append(obj.toString());
        }
        EmailUtil.save("["+currentTime+"]" + " " + "<span style=\"color: #5a952a;\">"+remoteAddr+"</span>" + "\t" + methodName + "\t" +"<span style=\"color: #00a3a3;\">"+requestUrl+"</span>" + "\t" + "{"+argsStr+"}");

        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        AdminPermission adminPermission = method.getAnnotation(AdminPermission.class);
        if(adminPermission == null) {
            //公共方法
            Object resultObject = joinPoint.proceed();
            return resultObject;
        }
        //判断当前管理员是否登录
        HttpSession session = httpServletRequest.getSession(true);
        String email = (String) session.getAttribute(session.getId());
        if(email == null){
            if(adminPermission.produceType().equals("text/html")) {
                httpServletResponse.sendRedirect("/admin/admin/login_page");
                return null;
            } else {
                CommonError commonError= new CommonError(EmBusinessError.ADMIN_SHOULD_LOGIN);
                return CommonRes.create(commonError,"fail");
            }
        } else {
            Object resultObject = joinPoint.proceed();
            return resultObject;
        }
    }

    private String getRequestUrl(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequestMapping requestMapping = AnnotationUtils.getAnnotation(method, RequestMapping.class);
        if (requestMapping != null && requestMapping.value().length > 0) {
            return requestMapping.value()[0];
        } else {
            return null;
        }
    }
}
