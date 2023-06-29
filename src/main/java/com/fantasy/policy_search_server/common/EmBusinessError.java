package com.fantasy.policy_search_server.common;

import lombok.Data;

public enum EmBusinessError {
    // 通用错误类型10000开头
    NO_OBJECT_FOUND(10001, "请求对象不存在"),
    UNKNOWN_ERROR(10002, "未知错误"),
    NO_HANDLER_FOUND(10003, "找不到执行的路径操作"),
    BIND_EXCEPTION_ERROR(10004, "请求参数错误"),

    // 用户服务相关的错误类型20000开头
    REGISTER_DUP_FAIL(20001, "用户已存在"),
    PARAMETER_VALIDATION_ERROR(20002, "请求参数校验失败"),
    LOGIN_FAIL(20003, "手机号或密码错误"),
    DO_NOT_LOGIN(20004, "未登录"),
    USER_DO_NOT_EXIST(20005, "用户不存在"),
    TOKEN_EXPIRED(20006, "token已过期"),
    TOKEN_CHECK_ERROR(20007, "token验证失败"),

    // admin相关问题
    ADMIN_SHOULD_LOGIN(30001, "管理员需要先登录"),

    // 品类相关
    CATEGORY_NAME_DUPLICATED(40001, "品类名已存在"),

    // 文档相关
    POLICY_DO_NOT_EXIT(50001, "政策文档不存在"),

    // 地区相关
    REGION_DO_NOT_EXIT(60001, "地区不存在");


    private Integer errCode;
    private String errMsg;
    EmBusinessError(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
    public Integer getErrCode() {
        return errCode;
    }
    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }
    public String getErrMsg() {
        return errMsg;
    }
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
