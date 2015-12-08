package com.jiuyi.vggle.dto.coin;

import java.io.Serializable;
import java.util.Date;

import com.jiuyi.vggle.dto.BaseDto;

public class CoinDto extends BaseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7199758074311143776L;

	/** no */
	private Integer no;

	/** type.类型 0.内部 1.外部 */
	private Integer type;

	/** ID. */
	private String coinId;

	/** 礼券密码. */
	private String coinPass;

	/** 用户ID. */
	private String customerId;

	/** 金额. */
	private Double amount;

	/** 创建时间. */
	private Date createTime;

	/** 有效期. */
	private Date validity;

	/** 使用日期. */
	private Date useTime;

	/** 状态. 0.已经激活; 1.未使用; 2.已经使用; 10;未激活 */
	private Integer coinStatus;

	/** 所属手机号. */
	private String coinPhone;

	/** 用户手机号. */
	private String userPhone;

	/** 礼品卡种类，0.虚拟卡和 1.实体卡 . */
	private Integer source;

	/** 数量. */
	private Integer num;

	/** 起始数值 */
	private Integer begin;

	/** 终止数值 */
	private Integer end;

	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}

	public String getCoinPass() {
		return coinPass;
	}

	public void setCoinPass(String coinPass) {
		this.coinPass = coinPass;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getValidity() {
		return validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	public Date getUseTime() {
		return useTime;
	}

	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}



	public String getCoinPhone() {
		return coinPhone;
	}

	public void setCoinPhone(String coinPhone) {
		this.coinPhone = coinPhone;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getCoinStatus() {
		return coinStatus;
	}

	public void setCoinStatus(Integer coinStatus) {
		this.coinStatus = coinStatus;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getBegin() {
		return begin;
	}

	public void setBegin(Integer begin) {
		this.begin = begin;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

}
