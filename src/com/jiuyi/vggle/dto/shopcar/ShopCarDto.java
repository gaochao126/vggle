package com.jiuyi.vggle.dto.shopcar;

import java.io.Serializable;

import com.jiuyi.vggle.dto.BaseDto;

public class ShopCarDto extends BaseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2370520653700944967L;
	
	/** 用户ID. */
	private Integer userId;

	/** 购物车ID. */
	private String carId;

	/** 购买商品名称. */
	private String commodityName;

	/** 商品厂家. */
	private String commodityFactor;

	/** 商品单价. */
	private Double commodityPrice;

	/** 商品折扣. */
	private Double discount;

	/** 购买商品的数量. */
	private Integer buyCount;

	/** 购买的商品金额. */
	private Double amount;

	/** 购买的商品ID. */
	private String commodityId;

	/** 购物车商品状态. */
	private Integer carStatus;

	/** 商品大小尺码. */
	private String size;

	/** 商品颜色地址Id . */
	private String colorImgId;

	/** 颜色图片地址. */
	private String colorImgSrc;

	/** 颜色名称. */
	private String colorName;

	/** 图片完整地址. */
	private String imageSrc;

	/** 图片名称. */
	private String imageName;

	/** 商品Type. */
	private Integer type;

	/** 商家ID */
	private String companyId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getCommodityFactor() {
		return commodityFactor;
	}

	public void setCommodityFactor(String commodityFactor) {
		this.commodityFactor = commodityFactor;
	}

	public Double getCommodityPrice() {
		return commodityPrice;
	}

	public void setCommodityPrice(Double commodityPrice) {
		this.commodityPrice = commodityPrice;
	}

	public Integer getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(Integer buyCount) {
		this.buyCount = buyCount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}

	public Integer getCarStatus() {
		return carStatus;
	}

	public void setCarStatus(Integer carStatus) {
		this.carStatus = carStatus;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getImageSrc() {
		return imageSrc;
	}

	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColorImgId() {
		return colorImgId;
	}

	public void setColorImgId(String colorImgId) {
		this.colorImgId = colorImgId;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getColorImgSrc() {
		return colorImgSrc;
	}

	public void setColorImgSrc(String colorImgSrc) {
		this.colorImgSrc = colorImgSrc;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

}
