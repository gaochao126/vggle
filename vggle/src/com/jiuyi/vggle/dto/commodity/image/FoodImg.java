package com.jiuyi.vggle.dto.commodity.image;

import java.io.Serializable;

import com.jiuyi.vggle.dto.BaseDto;

public class FoodImg extends BaseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4567507622368502815L;

	/** ID. */
	private String imageId;

	/** 商品ID. */
	private String foodId;

	/** 图片名字. */
	private String ImageUrl;

	/** 图片类型.1,首页展示图片;2,详情展示图片;3,详情信息展示图片 */
	private int imageType;

	/** 图片完整地址. */
	private String indexSrc;

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getFoodId() {
		return foodId;
	}

	public void setFoodId(String foodId) {
		this.foodId = foodId;
	}

	public String getImageUrl() {
		return ImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}

	public int getImageType() {
		return imageType;
	}

	public void setImageType(int imageType) {
		this.imageType = imageType;
	}

	public String getIndexSrc() {
		return indexSrc;
	}

	public void setIndexSrc(String indexSrc) {
		this.indexSrc = indexSrc;
	}
	
}
