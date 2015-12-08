package com.jiuyi.vggle.dto.Discuss;

import java.io.Serializable;
import java.util.Date;

import com.jiuyi.vggle.dto.BaseDto;

public class DiscussDto extends BaseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5651938077271658717L;

	/** 评论ID. */
	private String disId;

	/** 用户ID. */
	private Integer userId;

	/** 商品ID. */
	private String commodityId;

	/** 用户名字. 手机号. */
	private String userName;

	/** 评论时间. */
	private Date disTime;

	/** 评论内容. */
	private String disMess;

	/** 商品评分. */
	private Integer commodScore;

	/** 服务评分. */
	private Integer serviceScore;

	/** 物流评分. */
	private Integer transScore;

	/** 评论状态. */
	private Integer disStatus;

	/** 商品图片地址. */
	private String imgSrc;

	/** 商品名字. */
	private String commodName;

	/** 商品类型. */
	private Integer type;

	public String getDisId() {
		return disId;
	}

	public void setDisId(String disId) {
		this.disId = disId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getDisTime() {
		return disTime;
	}

	public void setDisTime(Date disTime) {
		this.disTime = disTime;
	}

	public String getDisMess() {
		return disMess;
	}

	public void setDisMess(String disMess) {
		this.disMess = disMess;
	}

	public Integer getCommodScore() {
		return commodScore;
	}

	public void setCommodScore(Integer commodScore) {
		this.commodScore = commodScore;
	}

	public Integer getServiceScore() {
		return serviceScore;
	}

	public void setServiceScore(Integer serviceScore) {
		this.serviceScore = serviceScore;
	}

	public Integer getTransScore() {
		return transScore;
	}

	public void setTransScore(Integer transScore) {
		this.transScore = transScore;
	}

	public Integer getDisStatus() {
		return disStatus;
	}

	public void setDisStatus(Integer disStatus) {
		this.disStatus = disStatus;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public String getCommodName() {
		return commodName;
	}

	public void setCommodName(String commodName) {
		this.commodName = commodName;
	}

}
