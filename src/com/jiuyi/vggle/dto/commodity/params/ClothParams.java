package com.jiuyi.vggle.dto.commodity.params;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.jiuyi.vggle.dto.BaseDto;
import com.jiuyi.vggle.dto.commodity.image.ClothImg;
import com.jiuyi.vggle.dto.commodity.size.SizeDto;

public class ClothParams extends BaseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6652893582485095717L;
	
	/** 服装鞋帽配饰ID. */
	private String clothId = "";
	
	/** 商品名字. */
	private String clothName = "";

	/** 商品拼音. */
	private String clothPinyin = "";

	/** 服装类型. */
	private Integer type;

	/** 二级类型. */
	private Integer secondId;

	/** 三级分类. */
	private Integer thirdId = 0;

	/** 季节分类. */
	private String season = "";

	/** 性别分类. */
	private String sexKind = "";

	/** 商家名称. */
	private String businessName = "";

	/** 商品详情. */
	private String clothDetail = "";

	/** 销量. */
	private Integer clothSales = 0;

	/** 图片名称. */
	private String clothImg = "";

	/** 厂名. */
	private String factoryName = "";

	/** 厂址. */
	private String factoryAddr = "";

	/** 图案. */
	private String pattern = "";

	/** 风格. */
	private String style = "";

	/** 库存. */
	private Integer clothSku = 0;

	/** 价格. */
	private Double price = 0.0;

	/** 折扣. */
	private Double discount = 1.0;

	/** 品牌. */
	private String brand = "";
	
	/** 材质. */
	private String material = "";

	/** 标签. */
	private String labels = "";

	private List<SizeDto> size;

	/** 删除状态. */
	private Integer deleteStatus = 0;

	/** 商品所属商家ID. */
	private String companyId = "";

	/** 上架时间. */
	private Date onvggleTime = new Date();

	// 承载衣服裤子尺码集合
	private List<String> sizeArry;



	/** 图片集合. */
	private List<ClothImg> clothImgs;

	// ============附加参数
	/** 销量. */
	private Integer sales;

	/** 库存. */
	private Integer sku;

	public String getClothId() {
		return clothId;
	}

	public void setClothId(String clothId) {
		this.clothId = clothId;
	}

	public String getClothName() {
		return clothName;
	}

	public void setClothName(String clothName) {
		this.clothName = clothName;
	}

	public String getClothPinyin() {
		return clothPinyin;
	}

	public void setClothPinyin(String clothPinyin) {
		this.clothPinyin = clothPinyin;
	}

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

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getSexKind() {
		return sexKind;
	}

	public void setSexKind(String sexKind) {
		this.sexKind = sexKind;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getClothDetail() {
		return clothDetail;
	}

	public void setClothDetail(String clothDetail) {
		this.clothDetail = clothDetail;
	}

	public Integer getClothSales() {
		return clothSales;
	}

	public void setClothSales(Integer clothSales) {
		this.clothSales = clothSales;
	}

	public String getClothImg() {
		return clothImg;
	}

	public void setClothImg(String clothImg) {
		this.clothImg = clothImg;
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

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Integer getClothSku() {
		return clothSku;
	}

	public void setClothSku(Integer clothSku) {
		this.clothSku = clothSku;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public List<SizeDto> getSize() {
		return size;
	}

	public void setSize(List<SizeDto> size) {
		this.size = size;
	}

	public List<ClothImg> getClothImgs() {
		return clothImgs;
	}

	public void setClothImgs(List<ClothImg> clothImgs) {
		this.clothImgs = clothImgs;
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

	public List<String> getSizeArry() {
		return sizeArry;
	}

	public void setSizeArry(List<String> sizeArry) {
		this.sizeArry = sizeArry;
	}
}
