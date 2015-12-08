package com.jiuyi.vggle.dto.commodity.params;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.jiuyi.vggle.dto.BaseDto;
import com.jiuyi.vggle.dto.commodity.image.FoodImg;

public class FoodParams extends BaseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8659034312825669181L;
	
	/** 商品ID. */
	private String foodId = "";

	/** 商品名称. */
	private String foodName = "";

	/** 商品拼音. */
	private String foodPinyin = "";

	/** 商品类别. */
	private Integer type;

	/** 颜色图片ID. */
	private String colorImgId = "";

	/** 二级分类. */
	private Integer secondId;

	/** 三级分类. */
	private Integer thirdId = 0;

	/** 商家名称. */
	private String businessName = "";

	/** 商品描述. */
	private String foodDetail = "";

	/** 商品销量. */
	private Integer foodSales = 0;

	/** 商品图片链接. */
	private String indexSrc = "";

	/** 粮油干货图片对象. */
	private List<FoodImg> FoodImg;

	/** 生成许可证编号. */
	private String prodLicenum = "";

	/** 厂名. */
	private String factoryName = "";

	/** 厂址. */
	private String factoryAddr = "";

	/** 厂家联系方式. */
	private String contact = "";

	/** 保质期. */
	private String keepDate = "";

	/** 生产日期. */
	private String creatDate = "";

	/** 食品添加剂 是、否. */
	private String foodAdditive = "";

	/** 包装方式. */
	private String packWay = "";

	/** 配料. */
	private String burden = "";

	/** 品牌. */
	private String brand = "";

	/** 规格. */
	private String format = "";

	/** 储存方法. */
	private String storeWay = "";

	/** 是否进口. */
	private String inlet = "";

	/** 商品条形码. */
	private String barcode = "";

	/** 商品价格. */
	private Double price = 0.0;

	/** 库存. */
	private Integer foodSku = 0;

	/** 折扣. */
	private Double discount = 1.0;

	/** 删除状态. */
	private Integer deleteStatus = 0;

	/** 商品所属商家ID. */
	private String companyId = "";

	/** 上架时间. */
	private Date onvggleTime = new Date();

	// ============附加参数
	/** 销量. */
	private Integer sales;

	/** 库存. */
	private Integer sku;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSecondId() {
		return secondId;
	}

	public void setSecondId(Integer secondId) {
		this.secondId = secondId;
	}

	public Integer getThirdId() {
		return thirdId;
	}

	public void setThirdId(Integer thirdId) {
		this.thirdId = thirdId;
	}

	public String getBusinessName() {
		return businessName;
	}


	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}


	public String getFoodId() {
		return foodId;
	}


	public void setFoodId(String foodId) {
		this.foodId = foodId;
	}


	public String getFoodName() {
		return foodName;
	}


	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}


	public String getFoodPinyin() {
		return foodPinyin;
	}

	public void setFoodPinyin(String foodPinyin) {
		this.foodPinyin = foodPinyin;
	}

	public String getFoodDetail() {
		return foodDetail;
	}


	public void setFoodDetail(String foodDetail) {
		this.foodDetail = foodDetail;
	}


	public Integer getFoodSales() {
		return foodSales;
	}

	public void setFoodSales(Integer foodSales) {
		this.foodSales = foodSales;
	}

	public String getIndexSrc() {
		return indexSrc;
	}


	public void setIndexSrc(String indexSrc) {
		this.indexSrc = indexSrc;
	}


	public List<FoodImg> getFoodImg() {
		return FoodImg;
	}


	public void setFoodImg(List<FoodImg> foodImg) {
		FoodImg = foodImg;
	}

	public String getProdLicenum() {
		return prodLicenum;
	}

	public void setProdLicenum(String prodLicenum) {
		this.prodLicenum = prodLicenum;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getFactoryAddr() {
		return factoryAddr;
	}

	public void setFactoryAddr(String factoryAddr) {
		this.factoryAddr = factoryAddr;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getKeepDate() {
		return keepDate;
	}

	public void setKeepDate(String keepDate) {
		this.keepDate = keepDate;
	}

	public String getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(String creatDate) {
		this.creatDate = creatDate;
	}

	public String getFoodAdditive() {
		return foodAdditive;
	}

	public void setFoodAdditive(String foodAdditive) {
		this.foodAdditive = foodAdditive;
	}

	public String getPackWay() {
		return packWay;
	}

	public void setPackWay(String packWay) {
		this.packWay = packWay;
	}

	public String getBurden() {
		return burden;
	}

	public void setBurden(String burden) {
		this.burden = burden;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getStoreWay() {
		return storeWay;
	}

	public void setStoreWay(String storeWay) {
		this.storeWay = storeWay;
	}

	public String getInlet() {
		return inlet;
	}

	public void setInlet(String inlet) {
		this.inlet = inlet;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getFoodSku() {
		return foodSku;
	}

	public void setFoodSku(Integer foodSku) {
		this.foodSku = foodSku;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getColorImgId() {
		return colorImgId;
	}

	public void setColorImgId(String colorImgId) {
		this.colorImgId = colorImgId;
	}

	public Integer getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Date getOnvggleTime() {
		return onvggleTime;
	}

	public void setOnvggleTime(Date onvggleTime) {
		this.onvggleTime = onvggleTime;
	}

	public Integer getSales() {
		return sales;
	}

	public void setSales(Integer sales) {
		this.sales = sales;
	}

	public Integer getSku() {
		return sku;
	}

	public void setSku(Integer sku) {
		this.sku = sku;
	}
}
