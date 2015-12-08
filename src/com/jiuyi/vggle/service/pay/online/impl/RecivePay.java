package com.jiuyi.vggle.service.pay.online.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.pay.OnlineReqBean;
import com.jiuyi.vggle.common.util.Crypto;
import com.jiuyi.vggle.common.util.Enumerate;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.coin.CoinDao;
import com.jiuyi.vggle.dao.commodity.CommodityDao;
import com.jiuyi.vggle.dao.commodity.size.SizeDao;
import com.jiuyi.vggle.dao.coupon.CouPonDao;
import com.jiuyi.vggle.dao.order.OrderDao;
import com.jiuyi.vggle.dto.coin.CoinDto;
import com.jiuyi.vggle.dto.commodity.params.ClothParams;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;
import com.jiuyi.vggle.dto.commodity.sizesku.SsDto;
import com.jiuyi.vggle.dto.coupon.CouPonDto;
import com.jiuyi.vggle.dto.order.OrderDto;

/**
 * Servlet implementation class Test
 */
@WebServlet(
urlPatterns = { "/RecivePay" },
		initParams = { 
 @WebInitParam(name = "RecivePay", value = "com.jiuyi.vggle.service.pay.online.impl.RecivePay")
		})
public class RecivePay extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(RecivePay.class);

	private OrderDao orderDao = Constants.applicationContext.getBean(OrderDao.class);

	private CommodityDao commodityDao = Constants.applicationContext.getBean(CommodityDao.class);

	private CoinDao coinDao = Constants.applicationContext.getBean(CoinDao.class);

	private CouPonDao couPonDao = Constants.applicationContext.getBean(CouPonDao.class);

	private SizeDao sizeDao = Constants.applicationContext.getBean(SizeDao.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("进入网银支付，回调方法=====");
		/** step1. 接收返回参数 */
		/** 商户编号 */
		String merchantid = request.getParameter("merchantid");
		/** 订单编号 */
		String orderNo = request.getParameter("merorderid");
		/** 金额 */
		String amountsum = request.getParameter("amountsum");
		/** 币种 */
		String currencytype = request.getParameter("currencytype");
		/** 商品种类 */
		String subject = request.getParameter("subject");
		/** 支付状态 1--支付成功 */
		String state = request.getParameter("state");
		/** 支付银行 */
		String paybank = request.getParameter("paybank");
		/** 发送到银行时间 */
		String banksendtime = request.getParameter("banksendtime");
		/** 返回到商户时间 */
		String merrecvtime = request.getParameter("merrecvtime");
		/** 接口版本 */
		String strInterface = request.getParameter("interface");
		/** 加密数据 */
		String mac_rec = request.getParameter("mac");

		System.out.println(merchantid + "-----");

		/** step2. 校验返回信息 */
		String mac_str = "merchantid=" + merchantid + "&merorderid=" + orderNo + "&amountsum=" + amountsum + "&currencytype=" + currencytype + "&subject=" + subject + "&state=" + state + "&paybank=" + paybank + "&banksendtime=" + banksendtime + "&merrecvtime=" + merrecvtime
 + "&interface="
				+ strInterface + "&merkey=" + Enumerate.MERKEY;

		logger.info("RecivePay. 接收到数据字符串：mac_str:" + mac_str + "========");
		logger.info("RecivePay.接收到的加密数据--mac_rec:--" + mac_rec);
		/** step3. 将接收到的返回结果数据进行加密 */
		String mac = Crypto.GetMessageDigest(mac_str, "MD5");
		logger.info("RecivePay.通过接收到的数据，自行加密传mac:--" + mac);
		/** step4. 处理校验结果 */
		String str = "";
		String addr = "";
		Double postAmount = 0.0;// 接收到的礼品卡，优惠券，现金 总金额
		Double coinAmount = 0.0;// 礼品卡金额
		Double couponAmount = 0.0;// 优惠券金额
		Double cahsAmount = 0.0;// 现金金额
		if (mac.toLowerCase().equals(mac_rec.toLowerCase()) && state.equals("1")) {
			logger.info("RecivePay.--mac与mac_rec相同，并且state= " + state + "===");
			String succ = "";
			succ = "success=true";

			/** step4-1. 创建一个网银支付对象，获取用户支付的礼品卡优惠券信息 */
			OnlinePayServiceImpl onlinePay = new OnlinePayServiceImpl();

			try {
				/** step4-2. 修改用户订单状态为已付款 */
				OrderDto orderDto = new OrderDto();
				orderDto.setOrderStatus(2);
				orderDto.setPayTime(Util.getDateTime());
				orderDto.setOrderNo(orderNo);
				logger.info("RecivePay--开始修改订单状态，为已付款");
				orderDao.updateOrderStatusByOrderNo(orderDto);

				logger.info("RecivePay--根据订单编号查询订单");
				orderDao.queryOrderByOrderNo(orderDto);

				List<OrderDto> payOrder = orderDao.queryOrderByOrderNo(orderDto);

				/** step4-3. 根据用户订单得到用户ID */

				logger.info("RecivePay--获得用户ID--" + payOrder.get(0).getUserId());
				Integer userId = payOrder.get(0).getUserId();

				/** step4-4. 循环订单，修改订单中商品库存 和销量 */
				logger.info("RecivePay==开始通过循环更改库存和销量----");
				for (int i = 0; i < payOrder.size(); i++) {
					// 计算订单总金额

					if (payOrder.get(i).getType() == 1) {
						logger.info("RecivePay--循环订单，该订单是食品类商品");
						FoodParams food = new FoodParams();
						food.setFoodId(payOrder.get(i).getCommodityId());
						food.setFoodSku(payOrder.get(i).getOrderCount());
						food.setFoodSales(payOrder.get(i).getOrderCount());
						logger.info("RecivePay--开始修改食品类库存");
						commodityDao.updataFoodSku(food);
						logger.info("RecivePay--开始修改食品类销量");
						commodityDao.updateFoodSales(food);
					}

					if (payOrder.get(i).getType() == 2) {
						logger.info("RecivePay--循环订单，该订单是服装商品");
						ClothParams clothParams = new ClothParams();
						clothParams.setClothId(payOrder.get(i).getCommodityId());
						clothParams.setClothSales(payOrder.get(i).getOrderCount());
						SsDto ssDto = new SsDto();
						logger.info("RecivePay--一次获得订单颜色图片ID--" + payOrder.get(i).getColorImgId() + ";--size--" + payOrder.get(i).getSize() + "--ordercount--" + payOrder.get(i).getOrderCount());
						ssDto.setImageId(payOrder.get(i).getColorImgId());
						ssDto.setSize(payOrder.get(i).getSize());
						ssDto.setSku(payOrder.get(i).getOrderCount());
						logger.info("RecivePay--开始修改服装类库存");
						sizeDao.updataSkuByImageIdAmdSizeDown(ssDto);// 执行减少库存方法

						logger.info("RecivePay--开始修改服装类销量");
						commodityDao.updateClothSales(clothParams);
					}
				}
				logger.info("RecivePay--开始获得订单地址" + payOrder.get(0).getReciveAddress());
				addr = payOrder.get(0).getReciveAddress();

				/** step4-5. 支付成功判断用户支付方式进行用户相应钱包账户扣除操作 */
				OnlineReqBean onlineReqBean = new OnlineReqBean();
				onlineReqBean.setUserId(userId);
				OnlineReqBean onlineReq = onlinePay.queryPayMess(onlineReqBean);
				
				/** step4-6. 判断支付使用的币种 . 0,未使用现金; 1,使用现金账户和钱包; 2,只使用现金 */

				/** 如果是现金和礼品卡支付. */
				CoinDto coin = new CoinDto();
				logger.info("RecivePay--开始对钱包账户进行操作");
				if (onlineReq.getUseCahs() == 1) {

					logger.info("RecivePay--userCahs=1");

					/** 判断是否使用优惠券. */
					if (onlineReq.getWalletCouponId() != null && !onlineReq.getWalletCouponId().equals("")) {
						logger.info("RecivePay--开始判断是否使用优惠券，该判断结果：使用了优惠券");
						/** 根据优惠券ID得到优惠券金额以及判断优惠券是否过期 */
						CouPonDto couPonDto = new CouPonDto();
						couPonDto.setCouponId(onlineReq.getWalletCouponId());
						logger.info("RecivePay--开始查询优惠券");
						CouPonDto couPon = couPonDao.queryCouPonByCouPonId(couPonDto);
						if (couPon != null) {
							couponAmount += couPon.getCouponAmount();
						}

						/** 修改优惠券状态. */
						CouPonDto couPons = new CouPonDto();
						couPons.setCouponId(onlineReq.getWalletCouponId());
						couPons.setCouponStatus(1);
						logger.info("RecivePay--修改优惠券状态为已经使用");
						couPonDao.updateCouPonStatus(couPons);
					}

					/** 判断是否使用礼品卡. */
					if (onlineReq.getWalletCoinId() != null && !onlineReq.getWalletCoinId().equals("")) {
						logger.info("RecivePay--开始判断是否使用礼品卡，该判断结果：使用了礼品卡--礼品卡ID--" + onlineReq.getWalletCoinId());
						/** 根据礼品卡ID得到礼品卡金额并判断是否过期 */
						CoinDto coinDto = new CoinDto();
						coinDto.setCoinId(onlineReq.getWalletCoinId());
						logger.info("RecivePay--开始查询礼品卡");
						coin = coinDao.queryCoinByCoinId(coinDto);
					
						coinAmount = coin.getAmount();
						
						/** 修改礼品卡金额和状态. */
						coinDto.setAmount(coinAmount);
						coinDto.setCoinStatus(2);
						logger.info("RecivePay--开始修改礼品卡金额以及修改礼品卡状态");
						coinDao.updateCoinAmountdownByCoinId(coinDto);
					}


					logger.info("RecivePay--开始添加消费记录");
					/** 设置消费记录ID. */
					orderDto.setHisId(Util.getUniqueSn());
					/** 设置用户ID. */
					orderDto.setUserId(userId);
					/** 设置订单编号. */
					orderDto.setOrderNo(orderNo);
					/** 设置付款时间 */
					orderDto.setOrderTime(Util.getDateTime());
					/** 设置现金. */
					orderDto.setCahsAmount(cahsAmount);

					if (Util.isNotEmpty(onlineReq.getWalletCoinId())) {
						/** 设置礼品卡ID. */
						orderDto.setWalletCoinId(onlineReq.getWalletCoinId());
						/** 设置礼品卡金额. */
						orderDto.setWalletCoinAmount(coinAmount);
					}
					if (Util.isNotEmpty(onlineReq.getWalletCoinId())) {
						/** 设置优惠券ID */
						orderDto.setWalletCouponId(onlineReq.getWalletCouponId());
					}

					/** 设置支付方式. */
					orderDto.setUseCahs(1);

					orderDao.addHis(orderDto);

					/** 获得接收到的总金额. */
					cahsAmount += Double.parseDouble(amountsum);

					/** 获得总金额 */
					postAmount = coinAmount + couponAmount + cahsAmount;
				}

				/** 只有现金支付 */
				if (onlineReq.getUseCahs() == 2) {
					postAmount = Double.parseDouble(amountsum);
					
					/** 设置消费记录ID. */
					orderDto.setHisId(Util.getUniqueSn());
					/** 设置用户ID. */
					orderDto.setUserId(userId);
					/** 设置订单编号. */
					orderDto.setOrderNo(orderNo);
					/** 设置付款时间 */
					orderDto.setOrderTime(Util.getDateTime());
					/** 设置现金. */
					orderDto.setCahsAmount(postAmount);
					/** 设置支付方式. */
					orderDto.setUseCahs(2);
					
					orderDao.addHis(orderDto);
				}


			} catch (Exception e1) {
				e1.printStackTrace();
			}
			PrintWriter out = null;
			try {
				out = response.getWriter();
			} catch (IOException e) {
				System.out.println("response.getWriter()");
				e.printStackTrace();
			}
			/** 增加用户返回字段success=true */
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("code", 0);
			dataMap.put("address", addr);
			dataMap.put("cahsAmount", cahsAmount);
			dataMap.put("coinAmount", coinAmount);
			dataMap.put("couponAmount", couponAmount);

			try {
				out.println(succ);
				out.flush();
				out.close();
			} catch (NullPointerException e) {

				e.printStackTrace();
			}
		} else {
			str = "失败";
		}
		request.setAttribute("error", str);
	}

}
