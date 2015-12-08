package com.jiuyi.vggle.dto.coupon;

import java.io.Serializable;
import java.util.Date;

import com.jiuyi.vggle.dto.BaseDto;

public class CouPonDto extends BaseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8334905602946076060L;

	/** 优惠券ID. */
	private String couponId;

	/** 用户ID. */
	private Integer userId;

	/** 优惠券金额. */
	private Double couponAmount;

	/** 创建日期. */
	private Date createDate;

	/** 有效期. */
	private Date validity;

	/** 使用日期. */
	private Date useDate;

	/** 优惠券状态. */
	private Integer couponStatus;

	/** 优惠券来源. */
	private Integer source;

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Double getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(Double couponAmount) {
		this.couponAmount = couponAmount;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getValidity() {
		return validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	public Date getUseDate() {
		return useDate;
	}

	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}

	public Integer getCouponStatus() {
		return couponStatus;
	}

	public void setCouponStatus(Integer couponStatus) {
		this.couponStatus = couponStatus;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

}
