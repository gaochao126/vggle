package com.jiuyi.vggle.dto.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.jiuyi.vggle.dto.BaseDto;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;

/**
 * 
 * @author gaochao
 * @date 2015-4-23
 */
public class OrderDto extends BaseDto implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6789754025423664956L;

	/** 订单id. */
	private String orderId;

	/** 购物车ID. */
	private List<String> carIds;

	/** 订单编号. */
	private String orderNo;

	/** 商品id. */
	private String commodityId;

	/** 商品名字. */
	private String commodityName;

	/** 用户id. */
	private Integer userId;

	/** 商品类型. 主分类 */
	private Integer type;

	/** 订单数量. */
	private List<Integer> counts;

	/** 单个数量. */
	private Integer orderCount;

	/** 订单金额. */
	private Double orderAmount;

	/** 订单时间. */
	private Date orderTime;

	/** 付款时间. */
	private Date payTime;

	/** 订单状态. 0,所有订单; 1,未付款; 2,已付款未发货; 3,已发货未收货; 4,已收货 */
	private Integer orderStatus;

	/** 发货时间. */
	private Date shipmentsTime;

	/** 标识码. */
	private Integer code;

	/** 收货时间. */
	private Date reciveTime;

	/** 退款申请时间. */
	private Date refundTime;

	/** 评论状态. 0.未评论 1.评论 */
	private Integer discussStatus;

	/** 退款状态. 0.未退款 1.申请退款 2.受理退款 3.受理失败 */
	private Integer refundStatus;

	/** 收货地址. */
	private String reciveAddress;

	/** 快递种类. */
	private String deliveKind;

	/** 发票状态 0.不需要发票 1.需要发票 */
	private Integer invoiceStatus;

	/** 发票抬头. */
	private String invoiceHead;

	/** 订单留言. */
	private String orderMessage;

	/** 删除状态 0.不删除 1.删除 */
	private Integer deleteStatus;

	/** 商家ID. */
	private String companyId;

	/** 颜色图片地址. */
	private String colorImgSrc;

	/** 颜色图片ID. */
	private String colorImgId;

	/** 大小尺码. */
	private String size;

	/** 订单金额. */
	private List<Double> amounts;

	/** 订单ID集合 */
	private String orderIds;

	/** ================================ */

	/** 消费记录ID. */
	private String hisId;

	/***/
	/** 订单现金金额. */
	private Double cahsAmount;

	/** 礼品卡ID */
	private String walletCoinId;

	/** 礼品卡金额. */
	private Double walletCoinAmount;

	/** 优惠券. */
	private String walletCouponId;

	/** 物谷账户金额. */
	private Double walletVggleAmount;

	/** 是否使用现金账户. 0,未使用; 1,使用 现金和钱包; 2,使用现金 */
	private Integer useCahs;

	/** 创建一个商品对象. 粮油干货系列 */
	private FoodParams foodParams;

	/** 现金账户. */
	private Double cashAmount;


	/** 订单图片. */
	private String imageSrc;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<String> getCarIds() {
		return carIds;
	}

	public void setCarIds(List<String> carIds) {
		this.carIds = carIds;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<Integer> getCounts() {
		return counts;
	}

	public void setCounts(List<Integer> counts) {
		this.counts = counts;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getShipmentsTime() {
		return shipmentsTime;
	}

	public void setShipmentsTime(Date shipmentsTime) {
		this.shipmentsTime = shipmentsTime;
	}

	public Integer getDiscussStatus() {
		return discussStatus;
	}

	public void setDiscussStatus(Integer discussStatus) {
		this.discussStatus = discussStatus;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getReciveAddress() {
		return reciveAddress;
	}

	public void setReciveAddress(String reciveAddress) {
		this.reciveAddress = reciveAddress;
	}

	public String getDeliveKind() {
		return deliveKind;
	}

	public void setDeliveKind(String deliveKind) {
		this.deliveKind = deliveKind;
	}

	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getInvoiceHead() {
		return invoiceHead;
	}

	public void setInvoiceHead(String invoiceHead) {
		this.invoiceHead = invoiceHead;
	}

	public String getOrderMessage() {
		return orderMessage;
	}

	public void setOrderMessage(String orderMessage) {
		this.orderMessage = orderMessage;
	}

	public Integer getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Double getCahsAmount() {
		return cahsAmount;
	}

	public void setCahsAmount(Double cahsAmount) {
		this.cahsAmount = cahsAmount;
	}

	public List<Double> getAmounts() {
		return amounts;
	}

	public void setAmounts(List<Double> amounts) {
		this.amounts = amounts;
	}

	public String getHisId() {
		return hisId;
	}

	public void setHisId(String hisId) {
		this.hisId = hisId;
	}

	public String getWalletCoinId() {
		return walletCoinId;
	}

	public void setWalletCoinId(String walletCoinId) {
		this.walletCoinId = walletCoinId;
	}

	public Double getWalletCoinAmount() {
		return walletCoinAmount;
	}

	public void setWalletCoinAmount(Double walletCoinAmount) {
		this.walletCoinAmount = walletCoinAmount;
	}

	public String getWalletCouponId() {
		return walletCouponId;
	}

	public void setWalletCouponId(String walletCouponId) {
		this.walletCouponId = walletCouponId;
	}

	public Double getWalletVggleAmount() {
		return walletVggleAmount;
	}

	public void setWalletVggleAmount(Double walletVggleAmount) {
		this.walletVggleAmount = walletVggleAmount;
	}

	public Integer getUseCahs() {
		return useCahs;
	}

	public void setUseCahs(Integer useCahs) {
		this.useCahs = useCahs;
	}

	public FoodParams getFoodParams() {
		return foodParams;
	}

	public void setFoodParams(FoodParams foodParams) {
		this.foodParams = foodParams;
	}

	public Double getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(Double cashAmount) {
		this.cashAmount = cashAmount;
	}

	public String getImageSrc() {
		return imageSrc;
	}

	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}

	public Date getReciveTime() {
		return reciveTime;
	}

	public void setReciveTime(Date reciveTime) {
		this.reciveTime = reciveTime;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public String getColorImgSrc() {
		return colorImgSrc;
	}

	public void setColorImgSrc(String colorImgSrc) {
		this.colorImgSrc = colorImgSrc;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getColorImgId() {
		return colorImgId;
	}

	public void setColorImgId(String colorImgId) {
		this.colorImgId = colorImgId;
	}

	public String getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(String orderIds) {
		this.orderIds = orderIds;
	}

}
