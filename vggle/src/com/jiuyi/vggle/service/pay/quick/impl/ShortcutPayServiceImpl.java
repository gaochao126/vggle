package com.jiuyi.vggle.service.pay.quick.impl;

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
import com.jiuyi.vggle.common.pay.ShortcutPayEnum;
import com.jiuyi.vggle.common.pay.ShortcutPayUtil;
import com.jiuyi.vggle.common.pay.ShortcutReqBean;
import com.jiuyi.vggle.common.pay.ShortcutRespBean;
import com.jiuyi.vggle.common.util.Enumerate;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.coin.CoinDao;
import com.jiuyi.vggle.dao.commodity.CommodityDao;
import com.jiuyi.vggle.dao.commodity.size.SizeDao;
import com.jiuyi.vggle.dao.coupon.CouPonDao;
import com.jiuyi.vggle.dao.order.OrderDao;
import com.jiuyi.vggle.dao.pay.BankDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.coin.CoinDto;
import com.jiuyi.vggle.dto.commodity.params.ClothParams;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;
import com.jiuyi.vggle.dto.commodity.sizesku.SsDto;
import com.jiuyi.vggle.dto.coupon.CouPonDto;
import com.jiuyi.vggle.dto.order.OrderDto;
import com.jiuyi.vggle.dto.pay.BankDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.pay.quick.ShortcutPayService;



/**
 * @description 快捷支付业务层实现
 * @author zhb
 * @createTime 2015年5月7日
 */
@Service
public class ShortcutPayServiceImpl implements ShortcutPayService {
    private static final String SUCCESS = "C000000000";
	private final static Logger logger = Logger.getLogger(ShortcutPayServiceImpl.class);

    @Autowired
    private BankDao bankDao;

    @Autowired
    private OrderDao orderDao;

	@Autowired
	private CommodityDao commodityDao;

	@Autowired
	private CoinDao coinDao;

	@Autowired
	private CouPonDao couPonDao;

	@Autowired
	private SizeDao sizeDao;

    /**
	 * 1.查询快捷支付列表
	 * 
	 * @description 查询银行列表
	 * @param bankDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    @Override
	public ResponseDto queryQuickBanks(BankDto bankDto) throws Exception {
		List<BankDto> banks = bankDao.queryQuickBanks();
        if (banks != null && !banks.isEmpty()) {
            for (BankDto bank : banks) {
				bank.setLogoUrl(bank.getLogoUrl() != null ? Enumerate.BANK_SRC + bank.getLogoUrl() : null);
            }
        }

        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultDesc("获取银行列表成功");

		responseDto.setDetail(banks);
        return responseDto;
    }

    /**
	 * 2.查询网银支付列表
	 * 
	 * @description 查询银行列表
	 * @param bankDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	@Override
	public ResponseDto queryOnlineBanks(BankDto bankDto) throws Exception {
		List<BankDto> banks = bankDao.queryOnlineBanks();
		if (banks != null && !banks.isEmpty()) {
			for (BankDto bank : banks) {
				bank.setLogoUrl(bank.getLogoUrl() != null ? Enumerate.BANK_SRC + bank.getLogoUrl() : null);
			}
		}

		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("获取银行列表成功");
		responseDto.setDetail(banks);
		return responseDto;
	}

	/**
	 * 3.@description 消费交易QP0001 (商户->快捷支付平台)
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    @Override
	public ResponseDto QP0001(ShortcutReqBean shortcutReqBean) throws Exception {
        /** step1:空异常处理. */
        if (shortcutReqBean == null) {
            throw new BusinessException(Constants.DATA_ERROR);
        }

		/** step2. 校验订单号和银行号不能为空 */
		if (!Util.isNotEmpty(shortcutReqBean.getBankNo()) || !Util.isNotEmpty(shortcutReqBean.getMerOrderId())) {
			throw new BusinessException("银行号或者订单ID集合不能为空");
		}

		/** step3. 校验校验支付金额和手机号不能为空. */
		if (!Util.isNotEmpty(shortcutReqBean.getAmount()) || !Util.isNotEmpty(shortcutReqBean.getPhoneNo())) {
			throw new BusinessException("银行号和支付金额不能为空");
		}

		/** step4. 校验短信验证码不能为空. */
		if (!Util.isNotEmpty(shortcutReqBean.getPhoneVerCode())) {
			throw new BusinessException("短信验证码不能为空");
		}

		/** step5:根据ID集合查询订单. */
		OrderDto queryOrderDto = new OrderDto();
		queryOrderDto.setOrderNo(shortcutReqBean.getMerOrderId());
		List<OrderDto> orders = orderDao.queryOrderByOrderNo(queryOrderDto);


		if (orders == null || orders.equals("") || orders.size() == 0) {
            throw new BusinessException("订单不存在");
        }


		/** step7:校验支付金额是正确. */
		Double orderAmount = 0.0;
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getOrderStatus() != 1) {
				throw new BusinessException("订单已经支付，订单中存在已经支付的商品");
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
		/** step8. 判断支付使用的币种 . 0,未使用现金; 1,使用现金账户和钱包; 2,只使用现金 */
		Double postAmount = 0.0;
		Double coinAmount = 0.0;
		Double couponAmount = 0.0;
		Double cahsAmount = 0.0;
		/** 如果是现金和礼品卡支付. */
		CoinDto coin = new CoinDto();
		if (shortcutReqBean.getUseCahs() == 1) {

			/** 判断是否使用优惠券. */
			if (shortcutReqBean.getWalletCouponId() != null && !shortcutReqBean.getWalletCouponId().equals("")) {

				/** 根据优惠券ID得到优惠券金额以及判断优惠券是否过期 */
				CouPonDto couPonDto = new CouPonDto();
				couPonDto.setCouponId(shortcutReqBean.getWalletCouponId());
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
				couponAmount = couPon.getCouponAmount();
			}

			/** 判断是否使用礼品卡. */
			if (shortcutReqBean.getWalletCoinId() != null && !shortcutReqBean.getWalletCoinId().equals("")) {
				/** 根据礼品卡ID得到礼品卡金额并判断是否过期 */
				CoinDto coinDto = new CoinDto();
				coinDto.setCoinId(shortcutReqBean.getWalletCoinId());
				coin = coinDao.queryCoinByCoinId(coinDto);
				if (coin == null) {
					throw new BusinessException("没有找到相关礼品卡");
				}

				if (coin.getCoinStatus() != 1) {
					throw new BusinessException("该礼品卡已经使用过");
				}

				if (new Date().after(coin.getValidity())) {
					throw new BusinessException("您的礼品卡已经过期了");
				}
				coinAmount = coin.getAmount();
			}


			/** 获得接收到的总金额. */
			cahsAmount = Double.parseDouble(shortcutReqBean.getAmount());

			postAmount = coinAmount + couponAmount + cahsAmount;
		}


		/** 如果只有现金支付 */
		if (shortcutReqBean.getUseCahs() == 2) {
			postAmount = Double.parseDouble(shortcutReqBean.getAmount());
		}

		if (postAmount < orderAmount) {
			throw new BusinessException("支付金额不对，可能您的订单商品价格已更新，请核对");
        }


		/** step10. 用一个Integer来储存用户ID. */
		Integer userId = Integer.parseInt(shortcutReqBean.getCustId());
		String orderNo = shortcutReqBean.getMerOrderId();

		/** step11:调用支付接口. */
        shortcutReqBean.setToken(null);
        ShortcutRespBean resp = ShortcutPayUtil.execute(shortcutReqBean, ShortcutPayEnum.QP0001);
		logger.info("ShortcutReqBean.QP0001." + resp.getRespXml() + "===" + resp.getRespMsg());

		/** step12:更新订单支付状态为已付款. */
        if (SUCCESS.equals(resp.getRespCode())) {
			OrderDto order = new OrderDto();
			order.setOrderNo(shortcutReqBean.getMerOrderId());
			order.setOrderStatus(2);
			order.setPayTime(Util.getDateTime());
			orderDao.updateOrderStatusByOrderNo(order);// 此处需要修改*****************

			/** 如果使用了现金和钱包支付,添加消费记录. */
			/** 添加订单消费记录表. */
			OrderDto orderDto = new OrderDto();
			if (shortcutReqBean.getUseCahs() == 1) {

				/** 修改优惠券状态. */
				if (Util.isNotEmpty(shortcutReqBean.getWalletCouponId())) {
					CouPonDto couPonDto = new CouPonDto();
					couPonDto.setCouponId(shortcutReqBean.getWalletCouponId());
					couPonDto.setCouponStatus(2);
					couPonDao.updateCouPonStatus(couPonDto);
				}

				/** 修改礼品卡金额和状态. */
				if (Util.isNotEmpty(shortcutReqBean.getWalletCoinId())) {
					CoinDto coinDto = new CoinDto();
					coinDto.setCoinId(shortcutReqBean.getWalletCoinId());
					coinDto.setAmount(coinAmount);
					coinDto.setCoinStatus(2);
					coinDao.updateCoinAmountdownSome(coinDto);
					coinDao.updateCoinStatusById(coinDto);
				}

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

				if (Util.isNotEmpty(shortcutReqBean.getWalletCoinId())) {
					/** 设置礼品卡ID. */
					orderDto.setWalletCoinId(shortcutReqBean.getWalletCoinId());
					/** 设置礼品卡金额. */
					orderDto.setWalletCoinAmount(coinAmount);
				}
				if (Util.isNotEmpty(shortcutReqBean.getWalletCoinId())) {
					/** 设置优惠券ID */
					orderDto.setWalletCouponId(shortcutReqBean.getWalletCouponId());
				}

				/** 设置支付方式. */
				orderDto.setUseCahs(1);

				orderDao.addHis(orderDto);
			}

			/** 如果只使用了现金. */
			if (shortcutReqBean.getUseCahs() == 2) {

				/** 设置消费记录ID. */
				orderDto.setHisId(Util.getUniqueSn());
				/** 设置用户ID. */
				orderDto.setUserId(userId);
				/** 设置订单编号. */
				orderDto.setOrderNo(orderNo);
				/** 设置付款时间 */
				orderDto.setOrderTime(Util.getDateTime());
				/** 设置现金. */
				orderDto.setCahsAmount(Double.parseDouble(shortcutReqBean.getAmount()));
				/** 设置支付方式. */
				orderDto.setUseCahs(2);
				orderDao.addHis(orderDto);
			}
        }

		/** step13. 更改商品库存 和销量 */
		OrderDto orderSku = new OrderDto();
		String addr = "";
		orderSku.setOrderNo(orderNo);
		List<OrderDto> orderSkus = orderDao.queryOrderByOrderNo(orderSku);
		for (int i = 0; i < orderSkus.size(); i++) {
			if (orderSkus.get(i).getType() == 1) {
				FoodParams food = new FoodParams();
				food.setFoodId(orderSkus.get(i).getCommodityId());
				food.setFoodSku(orderSkus.get(i).getOrderCount());
				food.setFoodSales(orderSkus.get(i).getOrderCount());
				commodityDao.updataFoodSku(food);
				commodityDao.updateFoodSales(food);
			}
			// 此处需要修改
			if (orderSkus.get(i).getType() == 2) {
				ClothParams clothParams = new ClothParams();
				clothParams.setClothId(orderSkus.get(i).getCommodityId());
				clothParams.setClothSales(orderSkus.get(i).getOrderCount());
				SsDto ssDto = new SsDto();
				ssDto.setImageId(orderSkus.get(i).getColorImgId());
				ssDto.setSize(orderSkus.get(i).getSize());
				ssDto.setSku(orderSkus.get(i).getOrderCount());
				sizeDao.updataSkuByImageIdAmdSizeDown(ssDto);// 执行减少库存方法
				commodityDao.updateClothSales(clothParams);
			}
		}
		addr = orderSkus.get(0).getReciveAddress();
		/** step14:返回结果. */
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultCode(SUCCESS.equals(resp.getRespCode()) ? 0 : 1);
        responseDto.setResultDesc(resp.getRespMsg());
        Map<String, Object> detail = new HashMap<String, Object>();
		detail.put("amount", shortcutReqBean.getAmount());
		detail.put("coinAmount", shortcutReqBean.getWalletCoinAmount());
		detail.put("reciveAddress", addr);
		if (shortcutReqBean.getWalletCouponId() != null && !shortcutReqBean.getWalletCouponId().equals("")) {
			detail.put("couponAmount", Util.Rounding(couponAmount));
		}
		if (shortcutReqBean.getWalletCoinId() != null && !shortcutReqBean.getWalletCoinId().equals("")) {
			detail.put("coinAmount", Util.Rounding(coinAmount));
		}
        responseDto.setDetail(detail);
        return responseDto;
    }

    /**
	 * 4.@description 快捷支付手机动态鉴权QP0002(商户->快捷支付平台 )
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    @Override
	public ResponseDto QP0002(ShortcutReqBean shortcutReqBean) throws Exception {
        /** step1:空异常处理. */
		if (shortcutReqBean == null) {
            throw new BusinessException(Constants.DATA_ERROR);
        }

		/** step2. 校验订单号和银行号不能为空 */
		if (!Util.isNotEmpty(shortcutReqBean.getOrderIds()) || !Util.isNotEmpty(shortcutReqBean.getBankNo())) {
			throw new BusinessException("银行号或者订单ID集合不能为空");
		}

		/** step3. 校验校验支付金额和手机号不能为空. */
		if (!Util.isNotEmpty(shortcutReqBean.getAmount()) || !Util.isNotEmpty(shortcutReqBean.getPhoneNo())) {
			throw new BusinessException("银行号和支付金额不能为空");
		}

		/** step4. 校验用户ID不能为空. */
		if (!Util.isNotEmpty(shortcutReqBean.getCustId())) {
			throw new BusinessException("用户ID不能为空");
		}

		/** step5:根据ID集合查询订单. */
		List<OrderDto> orders = new ArrayList<OrderDto>();
		String orderNo = Util.getUniqueSn();
		shortcutReqBean.setMerOrderId(orderNo);

		// ====================================
		// ====================================
		// ====================================
		JSONArray jsonArray = JSONArray.parseArray(shortcutReqBean.getOrderIds());
		if (jsonArray == null || jsonArray.size() == 0) {
			throw new BusinessException("未发现相关订单信息");
		}

		for (int n = 0; n < jsonArray.size(); n++) {
			OrderDto queryOrderDto = new OrderDto();
			queryOrderDto.setOrderId(jsonArray.get(n).toString());
			queryOrderDto.setOrderNo(orderNo);

			// 修改订单编号
			orderDao.updataOrderNoById(queryOrderDto);
			// 查询订单并添加到集合
			orders.add(orderDao.queryOrderByOrderId(queryOrderDto));
		}

		if (orders == null || orders.equals("") || orders.size() == 0) {
            throw new BusinessException("订单不存在");
        }


		/** step7:校验支付金额是正确. */
		Double orderAmount = 0.0;
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getOrderStatus() != 1) {
				throw new BusinessException("订单已经支付，订单中存在已经支付的商品");
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

		/** step8. 判断支付使用的币种 . 0,未使用现金; 1,使用现金账户和钱包; 2,只使用现金 */

		Double postAmount = 0.0;
		logger.info("ShortcutReqBean.QP0002 useCahs：" + shortcutReqBean.getUseCahs());
		/** 如果是钱包账户和礼品卡. */
		logger.info("");
		if (shortcutReqBean.getUseCahs() == 1) {

			/** 判断是否使用优惠券. */
			if (shortcutReqBean.getWalletCouponId() != null && !shortcutReqBean.getWalletCouponId().equals("")) {

				/** 根据优惠券ID得到优惠券金额以及判断优惠券是否过期 */
				CouPonDto couPonDto = new CouPonDto();
				couPonDto.setCouponId(shortcutReqBean.getWalletCouponId());
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
				postAmount += couPon.getCouponAmount();
			}

			/** 判断是否使用礼品卡. */
			if (shortcutReqBean.getWalletCoinId() != null && !shortcutReqBean.getWalletCoinId().equals("")) {
				/** 根据礼品卡ID得到礼品卡金额并判断是否过期 */
				CoinDto coinDto = new CoinDto();
				coinDto.setCoinId(shortcutReqBean.getWalletCoinId());
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
				postAmount += coin.getAmount();
			}

			/** 获得接收到的总金额. */
			postAmount += Double.parseDouble(shortcutReqBean.getAmount());
		}

		/** 如果只有现金支付 */
		if (shortcutReqBean.getUseCahs() == 2) {
			postAmount = Double.parseDouble(shortcutReqBean.getAmount());
		}

		if (postAmount < orderAmount) {
			throw new BusinessException("支付金额不对，可能您的订单商品价格已更新，请核对");
        }

		/** step9:调用支付接口. */
		ShortcutRespBean resp = ShortcutPayUtil.execute(shortcutReqBean, ShortcutPayEnum.QP0002);

		/** step10:返回结果. */
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultCode(SUCCESS.equals(resp.getRespCode()) ? 0 : 1);
		logger.info("ShortcutReqBean.QP0002." + resp.getRespXml() + "===" + resp.getRespMsg());
        responseDto.setResultDesc(resp.getRespMsg());
        Map<String, Object> detail = new HashMap<String, Object>();
        responseDto.setDetail(detail);
        detail.put("merOrderId", resp.getMerOrderId());
        detail.put("phoneToken", resp.getPhoneToken());
		return responseDto;
    }

    /**
	 * 5.@description 关闭快捷支付QP0003（商户->快捷支付平台）
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    @Override
	public ResponseDto QP0003(ShortcutReqBean shortcutReqBean) throws Exception {
        /** step1:空异常处理. */
        if (shortcutReqBean == null) {
            throw new BusinessException(Constants.DATA_ERROR);
        }

        /** step2:调用支付接口. */
        shortcutReqBean.setToken(null);
        shortcutReqBean.setMerOrderId(System.currentTimeMillis() + "");
        ShortcutRespBean resp = ShortcutPayUtil.execute(shortcutReqBean, ShortcutPayEnum.QP0003);

        /** step3:返回结果. */
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultCode(SUCCESS.equals(resp.getRespCode()) ? 0 : 1);
        responseDto.setResultDesc(resp.getRespMsg());
        Map<String, Object> detail = new HashMap<String, Object>();
        responseDto.setDetail(detail);
        return responseDto;
    }

    /**
	 * 6.@description 快捷支付客户卡信息查询QP0004(商户->快捷支付平台)
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    @Override
	public ResponseDto QP0004(ShortcutReqBean shortcutReqBean) throws Exception {
        /** step1:空异常处理. */
        if (shortcutReqBean == null) {
            throw new BusinessException(Constants.DATA_ERROR);
        }

		/** step2:校验关键参数不能为空. */
		if (shortcutReqBean.getCustId() == null || shortcutReqBean.getCustId().equals("")) {
			throw new BusinessException("用户ID不能为空");
        }

		/** step3:调用支付接口. */
        ShortcutRespBean resp = ShortcutPayUtil.execute(shortcutReqBean, ShortcutPayEnum.QP0004);

		for (int i = 0; i < resp.getCardInfos().size(); i++) {
			BankDto bankDto = new BankDto();
			bankDto.setBankNo(resp.getCardInfos().get(i).getBankNo());
			BankDto bank = bankDao.queryOnlineBanksByBankNo(bankDto);
			resp.getCardInfos().get(i).setBankLink(Enumerate.BANK_SRC + bank.getLogoUrl());
		}

		/** step3:返回结果.00000000000000000 */
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultCode(SUCCESS.equals(resp.getRespCode()) ? 0 : 1);
        responseDto.setResultDesc(resp.getRespMsg());
        Map<String, Object> detail = new HashMap<String, Object>();
        responseDto.setDetail(detail);
        detail.put("cardNum", resp.getCardNum());
        detail.put("cardInfos", resp.getCardInfos());
        return responseDto;
    }

    /**
	 * 7.@description 退货交易QP0005(商户->快捷支付平台，暂无)
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    @Override
	public ResponseDto QP0005(ShortcutReqBean shortcutReqBean) throws Exception {
        return null;
    }

    /**
	 * 8.@description 快捷支付交易流水查询QP0006(商户->快捷支付平台)
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    @Override
	public ResponseDto QP0006(ShortcutReqBean shortcutReqBean) throws Exception {
        return null;
    }

    /**
	 * 9.@description 快捷支付卡信息查询QP0007(商户->快捷支付平台)查询银行卡信息
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    @Override
	public ResponseDto QP0007(ShortcutReqBean shortcutReqBean) throws Exception {
        /** step1:空异常处理. */
        if (shortcutReqBean == null) {
            throw new BusinessException(Constants.DATA_ERROR);
        }

		/** step2.校验关键参数不能为空. */
		if (!Util.isNotEmpty(shortcutReqBean.getCardNo())) {
			throw new BusinessException("银行卡号不能为空");
		}

		/** step3:调用支付接口. */
        ShortcutRespBean resp = ShortcutPayUtil.execute(shortcutReqBean, ShortcutPayEnum.QP0007);

		/** step4:返回结果. */
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultCode(SUCCESS.equals(resp.getRespCode()) ? 0 : 1);
        responseDto.setResultDesc(resp.getRespMsg());
        Map<String, Object> detail = new HashMap<String, Object>();
        responseDto.setDetail(detail);
        detail.put("cardType", resp.getCardType());
        detail.put("bankNo", resp.getBankNo());

		/** step5. 通过银行号获得银行图标 */
		BankDto bankDto = new BankDto();
		bankDto.setBankNo(resp.getBankNo());
		BankDto bank = bankDao.queryOnlineBanksByBankNo(bankDto);
		if (bank != null) {
			detail.put("bankSrc", Enumerate.BAND_SMALL_SRC + bank.getSmallImg());
		}

        detail.put("bankNm", resp.getBankNm());
        return responseDto;
    }

    /**
	 * 10.@description 一键支付交易QP0008 (商户->快捷支付平台)
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    @Override
	public ResponseDto QP0008(ShortcutReqBean shortcutReqBean) throws Exception {
        return null;
    }

    /**
	 * 11.@description 一键支付限额查询QP0009 (商户->快捷支付平台)
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    @Override
	public ResponseDto QP0009(ShortcutReqBean shortcutReqBean) throws Exception {
        return null;
    }

	/**
	 * 12.使用物谷钱包支付
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-9
	 */
	@Override
	public ResponseDto vggleWalletPay(ShortcutReqBean shortcutReqBean) throws Exception {
		/** step1. 空值判断 */
		if (shortcutReqBean == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 校验必输字段不能为空 */
		if (!Util.isNotEmpty(shortcutReqBean.getWalletCoinId()) && !Util.isNotEmpty(shortcutReqBean.getWalletCouponId())) {
			throw new BusinessException("礼品卡和优惠券至少必选一种");
		}

		/** step3. 判断订单ID集合不能为空 */
		if (!Util.isNotEmpty(shortcutReqBean.getOrderIds())) {
			throw new BusinessException("订单ID集合不能为空");
		}

		/** step4:校验订单是否存在. */
		List<OrderDto> orders = new ArrayList<OrderDto>();
		String orderNo = Util.getUniqueSn();
		shortcutReqBean.setMerOrderId(orderNo);

		// ====================================

		JSONArray jsonArray = JSONArray.parseArray(shortcutReqBean.getOrderIds());
		if (jsonArray == null || jsonArray.size() == 0) {
			throw new BusinessException("未发现相关订单信息");
		}

		for (int n = 0; n < jsonArray.size(); n++) {
			OrderDto queryOrderDto = new OrderDto();
			queryOrderDto.setOrderId(jsonArray.get(n).toString());
			queryOrderDto.setOrderNo(orderNo);

			// 修改订单编号
			orderDao.updataOrderNoById(queryOrderDto);
			// 查询订单并添加到集合
			orders.add(orderDao.queryOrderByOrderId(queryOrderDto));
		}


		/** step5:校验支付金额是正确. */
		Double orderAmount = 0.0;
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getOrderStatus() != 1) {
				throw new BusinessException("订单已经支付，订单中存在已经支付的商品");
			}

			// 通过商品ID和商品类型type得到商品价格
			if (orders.get(i).getType() == 1) {
				FoodParams foodParams = new FoodParams();
				foodParams.setFoodId(orders.get(i).getCommodityId());
				// 查询绿色食品表
				FoodParams food = commodityDao.queryFoodById(foodParams);
				orderAmount = food.getPrice() * food.getDiscount() * orders.get(i).getOrderCount();
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
		logger.info("ShortcutPayServiceImpl.vggleWalletPay  订单计算金额结果：" + orderAmount);
		/** step6. 更改礼品卡或优惠券数据库金额. */
		Double postAmount = 0.0;
		Double cpAmount = 0.0;
		Double coinAmount = 0.0;

		/** 判断是否使用优惠券. */
		CouPonDto couPon = new CouPonDto();
		if (shortcutReqBean.getWalletCouponId() != null && !shortcutReqBean.getWalletCouponId().equals("")) {

			/** 根据优惠券ID得到优惠券金额以及判断优惠券是否过期 */
			CouPonDto couPonDto = new CouPonDto();
			couPonDto.setCouponId(shortcutReqBean.getWalletCouponId());
			couPon = couPonDao.queryCouPonByCouPonId(couPonDto);
			if (couPon == null) {
				throw new BusinessException("没有找到相关优惠券");
			}

			if (couPon.getCouponStatus() != 0) {
				throw new BusinessException("您的优惠券已经使用过");
			}

			if (new Date().after(couPon.getValidity())) {
				throw new BusinessException("您的优惠券已经过期了");
			}

			cpAmount = couPon.getCouponAmount();
			logger.info("ShortcutPayServiceImpl.vggleWalletPay  使用了优惠券：优惠券金额：" + couPon.getCouponAmount());
		}

		logger.info("ShortcutPayServiceImpl.vggleWalletPay  接收到的优惠券礼品卡总金额：" + postAmount);

		/** 判断优惠券是否已经满足所支付的金额， 优先使用优惠， 如果优惠券金额已经满足支付，我们将忽略礼品卡，我们将设置礼品卡ID为空字符串. */
		if (cpAmount >= orderAmount) {
			shortcutReqBean.setWalletCoinId("");
		}

		/** 判断是否使用礼品卡. */
		CoinDto coin = new CoinDto();

		if (shortcutReqBean.getWalletCoinId() != null && !shortcutReqBean.getWalletCoinId().equals("")) {

			/** 根据礼品卡ID得到礼品卡金额并判断是否过期 */
			CoinDto coinDto = new CoinDto();
			coinDto.setCoinId(shortcutReqBean.getWalletCoinId());
			coin = coinDao.queryCoinByCoinId(coinDto);
			if (coin == null || coin.equals("")) {
				throw new BusinessException("没有找到相关礼品卡");
			}

			if (coin.getCoinStatus() != 1) {
				throw new BusinessException("该礼品卡已经使用过");
			}

			if (new Date().after(coin.getValidity())) {
				throw new BusinessException("您的礼品卡已经过期了");
			}

			coinAmount = coin.getAmount();
			logger.info("ShortcutPayServiceImpl.vggleWalletPay  使用了礼品卡：礼品卡金额：" + coin.getAmount());
		}
		
		postAmount = cpAmount + coinAmount;

		/** step7. 判断价格是否正确 */
		if (postAmount < orderAmount) {
			throw new BusinessException("支付金额不对，可能您的订单商品价格已更新，请核对");
		}

		/** step8. 进行支付 */
		/** 如果礼品卡和优惠券金额正好全部使用. */
		Double downAmount = 0.0;
		Double couponAmount = 0.0;
		if (postAmount.equals(orderAmount)) {
			CouPonDto couPonDto = new CouPonDto();
			logger.info("ShortcutPayServiceImpl.vggleWalletPay  订单金额和接收金额相同：");
			if (shortcutReqBean.getWalletCouponId() != null && !shortcutReqBean.getWalletCouponId().equals("")) {
				couPonDto.setCouponId(shortcutReqBean.getWalletCouponId());
				couPonDto.setCouponStatus(1);
				couPonDao.updateCouPonStatus(couPonDto);
				couponAmount = couPonDto.getCouponAmount();
			}
			if (shortcutReqBean.getWalletCoinId() != null && !shortcutReqBean.getWalletCoinId().equals("")) {
				CoinDto coinDto = new CoinDto();
				downAmount = orderAmount - couponAmount;
				logger.info("ShortcutPayServiceImpl.vggleWalletPay orderAmount:" + orderAmount + ";couponAmount:" + couponAmount + ";downAmount:" + downAmount);
				coinDto.setCoinId(shortcutReqBean.getWalletCoinId());
				coinDao.updateCoinAmountdownByCoinId(coinDto);
			}
		}

		/** 如果优惠券和礼品卡金额大于支付金额 */
		if (postAmount > orderAmount) {
			CouPonDto couPonDto = new CouPonDto();
			logger.info("ShortcutPayServiceImpl.vggleWalletPay  接收金额大于订单金额：");
			if (shortcutReqBean.getWalletCouponId() != null && !shortcutReqBean.getWalletCouponId().equals("")) {
				couPonDto.setCouponId(shortcutReqBean.getWalletCouponId());
				couPonDto.setCouponStatus(1);
				couPonDao.updateCouPonStatus(couPonDto);
				couponAmount = couPonDto.getCouponAmount();
			}

			if (shortcutReqBean.getWalletCoinId() != null && !shortcutReqBean.getWalletCoinId().equals("")) {
				CoinDto coinDto = new CoinDto();
				downAmount = orderAmount - couponAmount;
				coinDto.setAmount(downAmount);
				coinDto.setCoinId(shortcutReqBean.getWalletCoinId());
				coinDao.updateCoinAmountdownSome(coinDto);
			}
		}

		/** step9. 更改订单状态为已付款 */
		OrderDto orderDto = new OrderDto();
		orderDto.setOrderNo(shortcutReqBean.getMerOrderId());
		orderDto.setOrderStatus(2);
		orderDto.setPayTime(Util.getDateTime());
		orderDao.updateOrderStatusByOrderNo(orderDto);

		/** step10. 添加消费记录 */
		OrderDto hisOrder = new OrderDto();
		/** 设置消费记录ID. */
		hisOrder.setHisId(Util.getUniqueSn());
		/** 设置用户ID. */
		hisOrder.setUserId(Integer.parseInt(shortcutReqBean.getCustId()));
		/** 设置订单编号. */
		hisOrder.setOrderNo(shortcutReqBean.getMerOrderId());
		/** 设置付款时间 */
		hisOrder.setOrderTime(Util.getDateTime());
		/** 设置现金. */
		hisOrder.setCahsAmount(0.0);

		if (shortcutReqBean.getWalletCoinId() != null && !shortcutReqBean.getWalletCoinId().equals("")) {
			/** 设置礼品卡ID. */
			hisOrder.setWalletCoinId(shortcutReqBean.getWalletCoinId());
			/** 设置礼品卡金额. */
			hisOrder.setWalletCoinAmount(downAmount);
		}
		if (shortcutReqBean.getWalletCouponId() != null && !shortcutReqBean.getWalletCouponId().equals("")) {
			/** 设置优惠券ID */
			hisOrder.setWalletCouponId(shortcutReqBean.getWalletCouponId());
		}
		/** 设置支付方式. */
		hisOrder.setUseCahs(0);
		orderDao.addHis(hisOrder);

		/** step11. 更改商品库存和销量 */
		OrderDto orderSku = new OrderDto();
		String addr = "";
		orderSku.setOrderNo(shortcutReqBean.getMerOrderId());
		List<OrderDto> orderSkus = orderDao.queryOrderByOrderNo(orderSku);
		for (int i = 0; i < orderSkus.size(); i++) {
			if (orderSkus.get(i).getType() == 1) {
				FoodParams food = new FoodParams();
				food.setFoodId(orderSkus.get(i).getCommodityId());
				food.setFoodSku(orderSkus.get(i).getOrderCount());
				food.setFoodSales(orderSkus.get(i).getOrderCount());
				commodityDao.updataFoodSku(food);
				commodityDao.updateFoodSales(food);
			}

			if (orderSkus.get(i).getType() == 2) {
				ClothParams clothParams = new ClothParams();
				clothParams.setClothId(orderSkus.get(i).getCommodityId());
				clothParams.setClothSales(orderSkus.get(i).getOrderCount());

				SsDto ssDto = new SsDto();
				ssDto.setImageId(orderSkus.get(i).getColorImgId());
				ssDto.setSize(orderSkus.get(i).getSize());
				ssDto.setSku(orderSkus.get(i).getOrderCount());
				sizeDao.updataSkuByImageIdAmdSizeDown(ssDto);// 执行减少库存方法
				commodityDao.updateClothSales(clothParams);
			}
		}

		// 此处地址可能存在问题
		addr = orderSkus.get(0).getReciveAddress();

		/** step13. 返回结果. */
		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> detail = new HashMap<String, Object>();
		detail.put("reciveAddress", addr);
		if (shortcutReqBean.getWalletCouponId() != null && !shortcutReqBean.getWalletCouponId().equals("")) {
			detail.put("couponAmount", couponAmount);
		}
		if (shortcutReqBean.getWalletCoinId() != null && !shortcutReqBean.getWalletCoinId().equals("")) {
			detail.put("coinAmount", Util.Rounding(downAmount));
		}
		detail.put("orderAmount", Util.Rounding(orderAmount));

		detail.put("message", "优惠券和礼品卡共计抵消" + Util.Rounding(orderAmount));

		responseDto.setDetail(detail);
		responseDto.setResultDesc("支付成功");

		return responseDto;
	}


}