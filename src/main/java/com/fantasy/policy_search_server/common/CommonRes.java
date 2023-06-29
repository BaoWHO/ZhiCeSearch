package com.fantasy.policy_search_server.common;

public class CommonRes {
    // success或者failed
    private String status;
    // 若status=success，表明返回对应json数据；若status=fail，则返回通用错误码对应格式
    private Object data;
    public static CommonRes create(Object result) {
        return CommonRes.create(result, "success");
    }
    public static CommonRes create(Object result, String status) {
        CommonRes commonRes = new CommonRes();
        commonRes.setStatus(status);
        commonRes.setData(result);
        return commonRes;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}
