package com.gy.fileserver.enums;

/**
 * 响应状态码
 */
public enum ResponseCodeEnum {

    SUCCESS(0,"success"),
    FAIL(1,"exception"),
    NOT_FOUND(404,"not found"),
    NOT_AUTHED(403,"无权限，访问拒绝"),
    PARAM_INVAILD(400,"提交参数非法");

    private Integer code;
    private String msg;

    private ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
