package com.jiuyi.vggle.dto.commodity;

import java.io.Serializable;
import java.util.List;

import com.jiuyi.vggle.dto.BaseDto;
import com.jiuyi.vggle.dto.commodity.image.FoodImg;

/**
 * 2015-5-23
 * 
 * @author gc 商品信息model
 *
 */
public class BaseParamsDto extends BaseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2536301744285201872L;

	/** 商品ID. */
	private String commodityId;

	/** 商品名称. */
	private String commodityName;

	/** 商品拼音. */
	private String commodityPingyi;

	/** 商品类别. */
	private int type;

	/** 二级分类. */
	private int secondId;

	/** 三级分类. */
	private int thirdId;

	/** 商家名称. */
	private String businessName;

	/** 商品描述. */
	private String commodityDetail;

	/** 商品销量. */
	private String commoditySales;

	/** 商品图片链接. */
	private String indexSrc;

	/** 粮油干货图片对象. */
	private List<FoodImg> FoodImg;


	public String getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getCommodityPingyi() {
		return commodityPingyi;
	}

	public void setCommodityPingyi(String commodityPingyi) {
		this.commodityPingyi = commodityPingyi;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSecondId() {
		return secondId;
	}

	public void setSecondId(int secondId) {
		this.secondId = secondId;
	}

	public int getThirdId() {
		return thirdId;
	}

	public void setThirdId(int thirdId) {
		this.thirdId = thirdId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getCommodityDetail() {
		return commodityDetail;
	}

	public void setCommodityDetail(String commodityDetail) {
		this.commodityDetail = commodityDetail;
	}

	public String getCommoditySales() {
		return commoditySales;
	}

	public void setCommoditySales(String commoditySales) {
		this.commoditySales = commoditySales;
	}

	public List<FoodImg> getFoodImg() {
		return FoodImg;
	}

	public void setFoodImg(List<FoodImg> foodImg) {
		FoodImg = foodImg;
	}


	public String getIndexSrc() {
		return indexSrc;
	}

	public void setIndexSrc(String indexSrc) {
		this.indexSrc = indexSrc;
	}

}
