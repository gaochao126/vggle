package com.jiuyi.vggle.dto.pay;

import com.jiuyi.vggle.dto.BaseDto;


/**
 * @description 银行实体类
 * @author gc
 * @createTime 2015年5月27日
 */
public class BankDto extends BaseDto{
    /** serialVersionUID. */
    private static final long serialVersionUID = 1416636302911016114L;

    /** id. */
	private Integer bankId;

    /** 银行名称. */
    private String bankName;

    /** 银行号. */
    private String bankNo;

	/** 小图标. */
	private String smallImg;

    /** 银行logo地址. */
    private String logoUrl;

    /** 使用状态(0:未使用, 1:使用中). */
	private String status;

	/** 支持类型(0:借贷, 1:借, 2:贷 00贷借 01纯借记). */
	private String supportType;

	/** 支付类型(0:快捷支付, 1:网银支付 ) */
	private Integer kind;

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSupportType() {
		return supportType;
	}

	public void setSupportType(String supportType) {
		this.supportType = supportType;
	}

	public Integer getKind() {
		return kind;
	}

	public void setKind(Integer kind) {
		this.kind = kind;
	}

	public String getSmallImg() {
		return smallImg;
	}

	public void setSmallImg(String smallImg) {
		this.smallImg = smallImg;
	}

}