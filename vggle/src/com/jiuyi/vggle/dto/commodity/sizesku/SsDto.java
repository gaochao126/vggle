package com.jiuyi.vggle.dto.commodity.sizesku;

import java.io.Serializable;

import com.jiuyi.vggle.dto.BaseDto;

public class SsDto extends BaseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3127446328603444551L;

	/** ID */
	private String ssId;

	/** 图片ID */
	private String imageId;

	/** 商品ID */
	private String commodityId;

	/** 尺码数组 */
	private String sizeArry;

	/** 库存数组. */
	private String skuArry;

	/** 尺码 */
	private String size;

	/** 库存 */
	private Integer sku;

	public String getSsId() {
		return ssId;
	}

	public void setSsId(String ssId) {
		this.ssId = ssId;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
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

	public String getSizeArry() {
		return sizeArry;
	}

	public void setSizeArry(String sizeArry) {
		this.sizeArry = sizeArry;
	}

	public String getSkuArry() {
		return skuArry;
	}

	public void setSkuArry(String skuArry) {
		this.skuArry = skuArry;
	}

}
