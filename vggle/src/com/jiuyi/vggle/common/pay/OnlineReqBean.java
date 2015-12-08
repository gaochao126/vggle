package com.jiuyi.vggle.common.pay;

import java.io.Serializable;

public class OnlineReqBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4312319156354022228L;

	/** 商户编号. */
	private String merchantid;
	
	/** 订单编号. */
	private String merorderid;
	
	/** 订单金额. */
	private String amountsum;
	
	/** 商品种类. */
	private String subject;
	
	/** 币种. 缺省01代表人民币*/
	private String currencytype;

	/** 自动跳转页面. 是否自动跳转到取货页面：0→不跳转；1→跳转； 若在商城端选择银行，autojump为1，且waittime=0。 */
	private String autojump;

	/** 跳转等待时间. */
	private String waittime;

	/** 商城取货URL. */
	private String merurl;

	/** 是否通知商户. 是否将订单的状态通知给商户：0→不通知；1→通知；推荐上送1 */
	private String informmer;
	
	/** 商户通知服务器URL. */
	private String informurl;
	
	/** 商户返回确认. 商户是否响应平台的确认信息：0→不返回；1→返回；*/
	private String confirm;
	
	/** 支付银行. */
	private String merbank;

	/** 支付类型. */
	private String tradetype;

	/** 是否在商户端选择银行. 0→在宝易互通选择银行；1→在商户端选择银行 */
	private String bankInput;

	/** 接口版本. */
	private String interfaces;

	/** 支付银行类型. */
	private String bankcardtype;

	/** 商品详情地址. */
	private String pdtdetailurl;

	/** 加密串. */
	private String mac;

	/** 备注. */
	private String remark;

	/** 商品名称. */
	private String pdtdnm;

	/** 秘钥. */
	private String merkey;

	/***
	 * 附加平台信息 ===========================================
	 * 
	 * @return
	 */
	/** 用户ID. */
	private Integer userId;

	/** 礼品卡ID */
	private String walletCoinId;

	/** 优惠券ID. */
	private String walletCouponId;

	/** 用户账户金额. */
	private Double walletAmount;

	/** 订单总金额. */
	private Double orderAmount;

	/** 优惠券金额. */
	private Double couponAmount;

	/** 礼品卡金额. */
	private Double coinAmount;

	/** 订单ID集合 */
	private String orderIds;

	/** 是否使用现金账户. 0,未使用; 1,使用现金账户和钱包; 2,只使用现金 */
	private Integer useCahs;

	public String getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}

	public String getMerorderid() {
		return merorderid;
	}

	public void setMerorderid(String merorderid) {
		this.merorderid = merorderid;
	}


	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCurrencytype() {
		return currencytype;
	}

	public void setCurrencytype(String currencytype) {
		this.currencytype = currencytype;
	}

	public String getAutojump() {
		return autojump;
	}

	public void setAutojump(String autojump) {
		this.autojump = autojump;
	}

	public String getMerurl() {
		return merurl;
	}

	public void setMerurl(String merurl) {
		this.merurl = merurl;
	}

	public String getInformmer() {
		return informmer;
	}

	public void setInformmer(String informmer) {
		this.informmer = informmer;
	}

	public String getInformurl() {
		return informurl;
	}

	public void setInformurl(String informurl) {
		this.informurl = informurl;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public String getMerbank() {
		return merbank;
	}

	public void setMerbank(String merbank) {
		this.merbank = merbank;
	}

	public String getTradetype() {
		return tradetype;
	}

	public void setTradetype(String tradetype) {
		this.tradetype = tradetype;
	}

	public String getBankInput() {
		return bankInput;
	}

	public void setBankInput(String bankInput) {
		this.bankInput = bankInput;
	}

	public String getBankcardtype() {
		return bankcardtype;
	}

	public void setBankcardtype(String bankcardtype) {
		this.bankcardtype = bankcardtype;
	}

	public String getPdtdetailurl() {
		return pdtdetailurl;
	}

	public void setPdtdetailurl(String pdtdetailurl) {
		this.pdtdetailurl = pdtdetailurl;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPdtdnm() {
		return pdtdnm;
	}

	public void setPdtdnm(String pdtdnm) {
		this.pdtdnm = pdtdnm;
	}

	public String getMerkey() {
		return merkey;
	}

	public void setMerkey(String merkey) {
		this.merkey = merkey;
	}

	public String getWalletCoinId() {
		return walletCoinId;
	}

	public void setWalletCoinId(String walletCoinId) {
		this.walletCoinId = walletCoinId;
	}

	public String getWalletCouponId() {
		return walletCouponId;
	}

	public void setWalletCouponId(String walletCouponId) {
		this.walletCouponId = walletCouponId;
	}

	public Double getWalletAmount() {
		return walletAmount;
	}

	public void setWalletAmount(Double walletAmount) {
		this.walletAmount = walletAmount;
	}

	public Integer getUseCahs() {
		return useCahs;
	}

	public void setUseCahs(Integer useCahs) {
		this.useCahs = useCahs;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAmountsum() {
		return amountsum;
	}

	public void setAmountsum(String amountsum) {
		this.amountsum = amountsum;
	}

	public String getWaittime() {
		return waittime;
	}

	public void setWaittime(String waittime) {
		this.waittime = waittime;
	}

	public String getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(String interfaces) {
		this.interfaces = interfaces;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Double getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(Double couponAmount) {
		this.couponAmount = couponAmount;
	}

	public Double getCoinAmount() {
		return coinAmount;
	}

	public void setCoinAmount(Double coinAmount) {
		this.coinAmount = coinAmount;
	}

	public String getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(String orderIds) {
		this.orderIds = orderIds;
	}

}
