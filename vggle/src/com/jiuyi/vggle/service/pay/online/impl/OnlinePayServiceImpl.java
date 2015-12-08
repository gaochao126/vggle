package com.jiuyi.vggle.service.pay.online.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.pay.OnlineReqBean;
import com.jiuyi.vggle.common.util.Crypto;
import com.jiuyi.vggle.common.util.Enumerate;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.coin.CoinDao;
import com.jiuyi.vggle.dao.commodity.CommodityDao;
import com.jiuyi.vggle.dao.coupon.CouPonDao;
import com.jiuyi.vggle.dao.order.OrderDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.coin.CoinDto;
import com.jiuyi.vggle.dto.commodity.params.ClothParams;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;
import com.jiuyi.vggle.dto.coupon.CouPonDto;
import com.jiuyi.vggle.dto.order.OrderDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.pay.online.OnlinePayService;
import com.jiuyi.vggle.service.pay.quick.impl.ShortcutPayServiceImpl;

@Service
public class OnlinePayServiceImpl implements OnlinePayService {
	/** 利用内存存储用户付款时提交的礼品卡或者优惠券ID==用户在支付成功后修改数据库数据 */
	private static Map<String, OnlineReqBean> uid_OnlineMess = new HashMap<String, OnlineReqBean>();

	private final static Logger logger = Logger.getLogger(ShortcutPayServiceImpl.class);
	@Autowired
	private OrderDao orderDao;

	@Autowired
	private CommodityDao commodityDao;

	@Autowired
	private CoinDao coinDao;

	@Autowired
	private CouPonDao couPonDao;
	/**
	 * 1.浏览器 网银支付-->支付平台
	 * 
	 * @param onlineReqBean
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-11
	 */
	@Override
	public ResponseDto FromBrowToPay(OnlineReqBean onlineReqBean) throws Exception {
		/** step1. 空值判断 */
		if (onlineReqBean == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 校验必输字段不能为空 订单号和金额. */
		if (!Util.isNotEmpty(onlineReqBean.getOrderIds()) || onlineReqBean.getAmountsum() == null || onlineReqBean.getAmountsum().equals("")) {
			throw new BusinessException("订单ID集合或者现金金额不能为空");
		}

		/** step3. 判断商城取货地址不能为空 */
		if (!Util.isNotEmpty(onlineReqBean.getMerurl()) || !Util.isNotEmpty(onlineReqBean.getMerbank())) {
			throw new BusinessException("支付银行或者商城取货页面地址不能为空");
		}

		/** step4. 判断支付银行类型不能为空. */
		if (onlineReqBean.getBankcardtype() == null || onlineReqBean.getBankcardtype().equals("")) {
			throw new BusinessException("银行卡类型不能为空");
		}

		/** step5. 校验用户ID不能为空. */
		if (onlineReqBean.getUserId() == null || onlineReqBean.getUserId().equals("")) {
			throw new BusinessException("用户ID不能为空");
		}

		/** step6:校验订单是否存在. */
		String orderNo = Util.getUniqueSn();
		onlineReqBean.setMerorderid(orderNo);
		List<OrderDto> orders = new ArrayList<OrderDto>();

		JSONArray jsonArray = JSONArray.parseArray(onlineReqBean.getOrderIds());
		if (jsonArray == null || jsonArray.size() == 0) {
			throw new BusinessException("未发现相关订单信息");
		}

		for (int n = 0; n < jsonArray.size(); n++) {
			OrderDto queryOrderDto = new OrderDto();
			queryOrderDto.setOrderId(jsonArray.get(n).toString());
			queryOrderDto.setOrderNo(orderNo);
			orderDao.updataOrderNoById(queryOrderDto);
			orders.add(orderDao.queryOrderByOrderId(queryOrderDto));
		}

		if (orders == null || orders.equals("") || orders.size() == 0) {
			throw new BusinessException("订单不存在");
		}

		/** step8:校验支付金额是正确. */
		Double orderAmount = 0.0;
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getOrderStatus() != 1) {
				throw new BusinessException("订单中存在已经支付的商品");
			}

			// 通过商品ID和商品类型type得到商品价格
			if (orders.get(i).getType() == 1) {
				FoodParams foodParams = new FoodParams();
				foodParams.setFoodId(orders.get(i).getCommodityId());
				// 查询绿色食品表
				FoodParams food = commodityDao.queryFoodById(foodParams);
				// 判断金额
				orderAmount += food.getPrice() * food.getDiscount() * orders.get(i).getOrderCount();
			}
			// 判断服装鞋帽系列产品
			if (orders.get(i).getType() == 2) {
				ClothParams clothParams = new ClothParams();
				clothParams.setClothId(orders.get(i).getCommodityId());
				// 查询服装鞋帽表
				ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
				// 判断金额
				orderAmount += cloth.getPrice() * cloth.getDiscount() * orders.get(i).getOrderCount();
			}
		}

		// 设置订单总金额
		onlineReqBean.setOrderAmount(orderAmount);

		/** step9. 判断支付使用的币种 . 0,未使用现金; 1,使用现金账户和钱包; 2,只使用现金 */

		Double postAmount = 0.0;
		Double couponAmount = 0.0;
		Double coinAmount = 0.0;
		logger.info("ShortcutReqBean.QP0002 useCahs：" + onlineReqBean.getUseCahs());

		/** 如果是钱包账户和礼品卡. */
		logger.info("");
		if (onlineReqBean.getUseCahs() == 1) {

			/** 判断是否使用优惠券. */
			if (onlineReqBean.getWalletCouponId() != null && !onlineReqBean.getWalletCouponId().equals("")) {

				/** 根据优惠券ID得到优惠券金额以及判断优惠券是否过期 */
				CouPonDto couPonDto = new CouPonDto();
				couPonDto.setCouponId(onlineReqBean.getWalletCouponId());
				CouPonDto couPon = couPonDao.queryCouPonByCouPonId(couPonDto);
				if (couPon == null) {
					throw new BusinessException("没有找到相关优惠券");
				}

				if (couPon.getCouponStatus() != 0) {
					throw new BusinessException("您的优惠券已经使用过");
				}

				if (new Date().after(couPon.getValidity())) {
					throw new BusinessException("您的优惠券已经过期了");
				}
				couponAmount += couPon.getCouponAmount();
			}

			/** 判断是否使用礼品卡. */
			if (onlineReqBean.getWalletCoinId() != null && !onlineReqBean.getWalletCoinId().equals("")) {
				/** 根据礼品卡ID得到礼品卡金额并判断是否过期 */
				CoinDto coinDto = new CoinDto();
				coinDto.setCoinId(onlineReqBean.getWalletCoinId());
				CoinDto coin = coinDao.queryCoinByCoinId(coinDto);
				if (coin == null) {
					throw new BusinessException("没有找到相关礼品卡");
				}

				if (coin.getCoinStatus() != 1) {
					throw new BusinessException("该礼品卡已经使用过");
				}

				if (new Date().after(coin.getValidity())) {
					throw new BusinessException("您的礼品卡已经过期了");
				}
				coinAmount += coin.getAmount();
			}

			/** 获得接收到的总金额. */
			postAmount = couponAmount + coinAmount + Double.parseDouble(onlineReqBean.getAmountsum());
		}

		/** 只有现金支付 */
		if (onlineReqBean.getUseCahs() == 2) {
			postAmount = Double.parseDouble(onlineReqBean.getAmountsum());
		}

		/** step10. 判断金额是否一致 */
		if (postAmount < orderAmount) {
			throw new BusinessException("支付金额不对，可能您的订单商品价格已更新，请核对");
		}


		/** step11. 设置参数 */
		/** 设置商户编号. */
		onlineReqBean.setMerchantid(Enumerate.TRUE_MERCHANTID);
		/** 设置商品种类. */
		onlineReqBean.setSubject(Enumerate.SUBJECT);
		/** 设置币种. */
		onlineReqBean.setCurrencytype("01");
		/** 设置是否自动跳转到取货页面. */
		onlineReqBean.setAutojump("1");
		/** 设置跳转取货页面等待时间. */
		onlineReqBean.setWaittime("5");
		/** 设置是否通知商户服务器. */
		onlineReqBean.setInformmer("1");
		/** 设置通知商户服务器地址. */
		onlineReqBean.setInformurl("http://222.180.171.34:51107/vggle/RecivePay");
		/** 设置是否商户确认返回. */
		onlineReqBean.setConfirm("1");
		/** 设置支付类型. 0即时到账 */
		onlineReqBean.setTradetype("0");
		/** 设置是否在商户选择银行卡. 1在商户端选择银行 */
		onlineReqBean.setBankInput("1");
		/** 设置接口版本. */
		onlineReqBean.setInterfaces("5.00");
		/** 设置商品详情地址. */
		onlineReqBean.setPdtdetailurl("http://115.29.33.165:80");
		/** 设置备注. */
		onlineReqBean.setRemark("物谷网交易");
		/** 设置商品名称. */
		onlineReqBean.setPdtdnm("物谷商城");
		/** 设置秘钥. */
		onlineReqBean.setMerkey(Enumerate.MERKEY);

		/**
		 * step6. 拼接加密字符串.
		 * 终端post数据有：merorderid、amountsum、merurl、merbank、bankcardtype
		 */

		String mac_src = 
				"merchantid=" + Enumerate.TRUE_MERCHANTID 
				+ "&merorderid=" + onlineReqBean.getMerorderid() 
				+ "&amountsum=" + onlineReqBean.getAmountsum() 
				+ "&subject=" + onlineReqBean.getSubject() 
				+ "&currencytype=" + onlineReqBean.getCurrencytype() 
				+ "&autojump=" + onlineReqBean.getAutojump() 
				+ "&waittime=" + onlineReqBean.getWaittime() 
				+ "&merurl=" + onlineReqBean.getMerurl()
				+ "&informmer=" + onlineReqBean.getInformmer() 
				+ "&informurl=" + onlineReqBean.getInformurl() 
				+ "&confirm=" + onlineReqBean.getConfirm()
				+ "&merbank=" + onlineReqBean.getMerbank()
				+ "&tradetype=" + onlineReqBean.getTradetype() 
				+ "&bankInput=" + onlineReqBean.getBankInput() 
				+ "&interface=" + onlineReqBean.getInterfaces()
				+ "&bankcardtype=" + onlineReqBean.getBankcardtype()
 + "&pdtdetailurl=" + onlineReqBean.getPdtdetailurl()
				+ "&merkey=" + Enumerate.MERKEY;

		/** step12. 调用签名函数生成签名串 */
		String mac = Crypto.GetMessageDigest(mac_src, "MD5");
		logger.info("FromBrowToPay.FromBrowsToPay@@@merorderid:" + onlineReqBean.getMerorderid() + ":amountsum:" + onlineReqBean.getAmountsum() + ":merurl:" + onlineReqBean.getMerurl() + ":merbank:" + onlineReqBean.getMerurl() + ":bankcardtype" + onlineReqBean.getBankcardtype());
		System.out.println(mac);
		onlineReqBean.setMac(mac);

		/** step13. 将用户支付的优惠券礼品卡信息放在内存中临时存储 */
		addPayMess(onlineReqBean);

		/** step14. 返回结果 */
		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("onlineReqBean", onlineReqBean);
		dataMap.put("payurl", Enumerate.ONLINE_PAY);
		responseDto.setDetail(dataMap);
		responseDto.setResultDesc("网银支付信息");
		return responseDto;
	}

	/**
	 * 2.将用户支付信息添加到内存中
	 * 
	 * @param onlineReqBean
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-12
	 */
	@Override
	public void addPayMess(OnlineReqBean onlineReqBean) throws Exception {
		uid_OnlineMess.put(onlineReqBean.getUserId().toString(), onlineReqBean);
	}

	/**
	 * 3.查询内存中的用户信息
	 * 
	 * @param onlineReqBean
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-12
	 */
	@Override
	public OnlineReqBean queryPayMess(OnlineReqBean onlineReqBean) throws Exception {
		if (!uid_OnlineMess.containsKey(onlineReqBean.getUserId())) {
			return null;
		}
		return uid_OnlineMess.get(onlineReqBean.getUserId());
	}
}
