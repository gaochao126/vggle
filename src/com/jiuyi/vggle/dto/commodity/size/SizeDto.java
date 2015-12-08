package com.jiuyi.vggle.dto.commodity.size;

import java.io.Serializable;

import com.jiuyi.vggle.dto.BaseDto;

public class SizeDto extends BaseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5750717884950280868L;

	/** 尺码ID. */
	private String sizeId;

	/** 商品ID. */
	private String clothId;

	/** 尺码名字. */
	private String sizeName;

	public String getSizeId() {
		return sizeId;
	}

	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}

	public String getClothId() {
		return clothId;
	}

	public void setClothId(String clothId) {
		this.clothId = clothId;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

}
