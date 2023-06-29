package com.fantasy.policy_search_server.common;

import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonRes doError(HttpServletRequest servletRequest,
                             HttpServletResponse httpServletResponse,
                             Exception ex) {
        if (ex instanceof BusinessException) {
            return CommonRes.create(((BusinessException)ex).getCommonError(), "failed");
        } else if (ex instanceof NoHandlerFoundException) {
            CommonError commonError = new CommonError(EmBusinessError.NO_HANDLER_FOUND);
            return CommonRes.create(commonError, "failed");
        } else if (ex instanceof MissingServletRequestParameterException) {
            CommonError commonError = new CommonError(EmBusinessError.BIND_EXCEPTION_ERROR);
            return CommonRes.create(commonError, "failed");
        } else {
            ex.printStackTrace();
            CommonError commonError = new CommonError(EmBusinessError.UNKNOWN_ERROR);
            return CommonRes.create(commonError, "failed");
        }
    }
}
