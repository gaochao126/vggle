package com.jiuyi.vggle.dto.commodity.image;

import java.io.Serializable;

import com.jiuyi.vggle.dto.BaseDto;

public class ClothImg extends BaseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5025573767811430991L;

	/** 图片ID. */
	private String imageId;

	/** 商品ID. */
	private String clothId;

	/** 颜色名字. */
	private String colorName;

	/** 图片名字. */
	private String imageName;

	/** 图片类型. */
	private Integer imageType;

	/** 商品尺码. */
	private String size;

	/** 商品库存. */
	private Integer sku;

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getClothId() {
		return clothId;
	}

	public void setClothId(String clothId) {
		this.clothId = clothId;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Integer getImageType() {
		return imageType;
	}

	public void setImageType(Integer imageType) {
		this.imageType = imageType;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Integer getSku() {
		return sku;
	}

	public void setSku(Integer sku) {
		this.sku = sku;
	}

}
