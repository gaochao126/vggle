package com.jiuyi.vggle.dto;

import java.io.Serializable;

/**
 * @description 响应实全类
 * @author zhb
 * @createTime 2015年4月21日
 */
public class ResponseDto implements Serializable{
    /** serialVersionUID. */
    private static final long serialVersionUID = -5086935669581293324L;

    /** 请求命令. */
    private String cmd;

    /** 结果码. */
    private int resultCode;

    /** 结果描述. */
    private String resultDesc = "成功";

    /** 数据详情. */
    private Object detail;


	public ResponseDto() {
	}

	public ResponseDto(String cmd, int resultCode, String resultDesc, Object detail) {
		this.cmd = cmd;
		this.resultCode = resultCode;
		this.resultDesc = resultDesc;
		this.detail = detail;
	}

	public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }


}