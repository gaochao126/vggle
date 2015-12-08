package com.jiuyi.vggle.dto;

/**
 * @description 请求实体类
 * @author zhb
 * @createTime 2015年4月21日
 */
public class RequestDto {
    /** 请求命令. */
    private String cmd;

    /** token. */
    private String token;

    /** 请求参数. */
    private Object params;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }
}