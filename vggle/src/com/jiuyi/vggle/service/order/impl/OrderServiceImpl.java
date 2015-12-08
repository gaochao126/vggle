package com.jiuyi.vggle.service.order.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.jiuyi.vggle.common.dict.CacheContainer;
import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.util.Enumerate;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.address.AddressDao;
import com.jiuyi.vggle.dao.coin.CoinDao;
import com.jiuyi.vggle.dao.commodity.CommodityDao;
import com.jiuyi.vggle.dao.commodity.image.ImageDao;
import com.jiuyi.vggle.dao.coupon.CouPonDao;
import com.jiuyi.vggle.dao.order.OrderDao;
import com.jiuyi.vggle.dao.user.UserDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.TokenDto;
import com.jiuyi.vggle.dto.address.AddressDto;
import com.jiuyi.vggle.dto.coin.CoinDto;
import com.jiuyi.vggle.dto.commodity.image.ClothImg;
import com.jiuyi.vggle.dto.commodity.params.ClothParams;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;
import com.jiuyi.vggle.dto.coupon.CouPonDto;
import com.jiuyi.vggle.dto.order.OrderDto;
import com.jiuyi.vggle.dto.shopcar.ShopCarDto;
import com.jiuyi.vggle.dto.user.UserDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.order.OrderService;
import com.jiuyi.vggle.service.shopcar.impl.ShopcarServiceImpl;

@Service
public class OrderServiceImpl implements OrderService {
	private final static Logger logger = Logger.getLogger(OrderServiceImpl.class);
	@Autowired
	private OrderDao orderDao;

	@Autowired
	private CommodityDao commodityDao;

	@Autowired
	private AddressDao addressDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private CoinDao coinDao;

	@Autowired
	private CouPonDao couPonDao;
	
	@Autowired
	private ImageDao imageDao;

	/**
	 * 1.从购物车添加订单
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	@Override
	public ResponseDto addOrder(OrderDto orderDto) throws Exception {

		/** step1. 空值判断. */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断收货地址不能为空 */
		if (orderDto.getReciveAddress() == null || orderDto.getReciveAddress().equals("")) {
			throw new BusinessException("收货地址不能为空");// 抛出异常
		}

		/** step3. 根据token得到用户ID */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		orderDto.setUserId(token.getUserDto().getId());

		/** step4. 根据购物车ID集合创建订单对象. */
		String orderNo = Util.getUniqueSn();// 生成一个订单编号，付款时操作

		/** step5. 从购物车获得用户要购买的购物车商品集合. */
		ShopcarServiceImpl shopcarService = new ShopcarServiceImpl();
		ShopCarDto shopCarDto = new ShopCarDto();
		shopCarDto.setUserId(orderDto.getUserId());
		List<ShopCarDto> cars = shopcarService.queryCarBuyOkServ(shopCarDto);

		/** 根据地址ID得到地址对象. */
		AddressDto addr = new AddressDto();
		addr.setAddrId(orderDto.getReciveAddress());
		AddressDto addressDto = addressDao.queryAddrByAddrId(addr);
		if (addressDto == null || addressDto.equals("")) {
			throw new BusinessException("未找到相关地址");
		}
		String reciveAddress = addressDto.getProvince() + addressDto.getCity() + addressDto.getArea() + addressDto.getStreet() + "；" + "邮编" + addressDto.getCode() + "收货人" + addressDto.getPersonName() + "联系方式" + addressDto.getPersonPhone();

		// 声明一个集合用于存储订单ID
		List<String> orderId = new ArrayList<String>();
		for (int i = 0; i < cars.size(); i++) {

			/** 设置订单ID. */
			orderDto.setOrderId(Util.getUniqueSn());
			orderId.add(orderDto.getOrderId());

			/** 设置订单编号. */
			orderDto.setOrderNo(orderNo);
			/** 设置商品ID. */
			orderDto.setCommodityId(cars.get(i).getCommodityId());
			/** 设置订单数量. */
			orderDto.setOrderCount(cars.get(i).getBuyCount());
			/** 设置订购日期. */
			orderDto.setOrderTime(Util.getDateTime());
			/** 设置订单状态. 1未付款 */
			orderDto.setOrderStatus(1);
			/** 设置评论状态. */
			orderDto.setDiscussStatus(0);
			/** 设置退款状态. */
			orderDto.setRefundStatus(0);
			/** 设置订单删除状态. */
			orderDto.setDeleteStatus(0);
			/** 设置商品Type */
			orderDto.setType(cars.get(i).getType());
			/** 设置订单图片地址. */
			orderDto.setImageSrc(cars.get(i).getImageName());
			/** 设置收货地址. */
			orderDto.setReciveAddress(reciveAddress);
			/** 设置商家ID. */
			orderDto.setCompanyId(cars.get(i).getCompanyId());

			/** 设置颜色图片ID. */
			if (cars.get(i).getType() == 2) {
				orderDto.setColorImgId(cars.get(i).getColorImgId());
			}
			/** 设置尺码. */
			if (cars.get(i).getType() == 2) {
				orderDto.setSize(cars.get(i).getSize());
			}

			/** step6. 添加订单. */
			orderDao.addOrderDto(orderDto);

			/** 添加成功后删除购物车. */
			ShopCarDto shop = new ShopCarDto();
			if (cars.get(i).getCarId() != null || !cars.get(i).getCarId().equals("") || cars.get(i).getUserId() != null || !cars.get(i).getUserId().equals("")) {
				shop.setCarId(cars.get(i).getCarId());
				shop.setUserId(cars.get(i).getUserId());
				logger.info("OrderServiceImpl.addOrder. 删除购物车开始");
				shopcarService.deleteCarServ(shop);
				logger.info("OrderServiceImpl.addOrder. 删除购物车成功");
			}
		}

		/** step7. 返回结果 */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("添加成功");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("ids", orderId);
		dataMap.put("orderNo", orderNo);
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 2.根据用户ID获得用户订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @date 2015-5-27
	 * @author gc
	 */
	@Override
	public ResponseDto queryOrderDtoByUserId(OrderDto orderDto) throws Exception {
		/** step1. 空值判断 */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断用户ID和订单状态. 根据token得到用户ID */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		orderDto.setUserId(token.getUserDto().getId());

		if (orderDto.getOrderStatus() == null || orderDto.getOrderStatus().equals("")) {
			throw new BusinessException("无效订单状态");
		}

		/** step3. 根据orderStatus获得用户订单 */

		List<OrderDto> userOrder = new ArrayList<OrderDto>();
		if (orderDto.getOrderStatus() == 0) {
			orderDto.setDeleteStatus(0);// 设置订单删除状态未删除
			userOrder = orderDao.queryOrderDtoByUserId(orderDto);

		} else {
			orderDto.setDeleteStatus(0);
			userOrder = orderDao.queryOrderDtoByUserIdAndOrderStatus(orderDto);
		}

		/** 计算订单总金额 */
		Double amount = 0.0;
		for (int i = 0; i < userOrder.size(); i++) {
			/** 通过商品ID和商品类型type得到商品价格 */
			if (userOrder.get(i).getType() == 1) {
				FoodParams foodParams = new FoodParams();
				foodParams.setFoodId(userOrder.get(i).getCommodityId());
				// 查询绿色食品表
				FoodParams food = commodityDao.queryFoodById(foodParams);
				userOrder.get(i).setCommodityName(food.getFoodName());
				amount += food.getPrice() * food.getDiscount() * userOrder.get(i).getOrderCount();
				userOrder.get(i).setImageSrc(Enumerate.FOOD_IMG_SRC + userOrder.get(i).getImageSrc());
				userOrder.get(i).setOrderAmount(food.getPrice() * food.getDiscount() * userOrder.get(i).getOrderCount());
			}

			/** 如果该商品是服装鞋帽 */
			if (userOrder.get(i).getType() == 2) {
				ClothParams clothParams = new ClothParams();
				clothParams.setClothId(userOrder.get(i).getCommodityId());
				// 查询服装鞋帽表
				ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
				userOrder.get(i).setCommodityName(cloth.getClothName());
				amount += cloth.getPrice() * cloth.getDiscount() * userOrder.get(i).getOrderCount();
				userOrder.get(i).setImageSrc(Enumerate.CLOTH_IMG_SRC + userOrder.get(i).getImageSrc());
				userOrder.get(i).setOrderAmount(cloth.getPrice() * cloth.getDiscount() * userOrder.get(i).getOrderCount());
			}
		}

		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("用户订单");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("orderList", userOrder);
		dataMap.put("amount", Util.Rounding(amount));
		dataMap.put("message", "[amount:用户所查询订单总金额];[orderAmount:单笔订单金额]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 3.确认收货
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	@Override
	public ResponseDto updateOrderStatus(OrderDto orderDto) throws Exception {
		/** step1. 空值判断 */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断订单ID是否为空 */
		if (orderDto.getOrderId() == null || orderDto.getOrderId().equals("")) {
			throw new BusinessException("无效订单ID");
		}

		/** step3. 根据token得到用户对象 */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}


		/** step4. 查询订单是否存在 */
		OrderDto order = orderDao.queryOrderByOrderId(orderDto);
		if (order == null) {
			throw new BusinessException("订单不存在");
		}

		/** step5. 判断订单是否属于登录用户 */
		if (order.getUserId() != token.getUserDto().getId()) {
			throw new BusinessException("该订单不属于您，无法操作");
		}

		/** step6. 确认收货 */
		orderDto.setOrderStatus(4);
		orderDto.setReciveTime(Util.getDateTime());
		orderDao.updateOrderReciveStatus(orderDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("成功确认收货");
		return responseDto;
	}

	/**
	 * 4.用户删除（隐藏）订单（修改订单删除状态）
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	@Override
	public ResponseDto updateOrderDeleteStatus(OrderDto orderDto) throws Exception {
		/** step1. 空值判断 */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断订单ID是否为空 */
		if (orderDto.getOrderId() == null || orderDto.getOrderId().equals("")) {
			throw new BusinessException("无效订单");
		}

		/** step3. 根据token得到用户对象 */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}

		/** step4. 查询订单是否存在 */
		OrderDto order = orderDao.queryOrderByOrderId(orderDto);
		if (order == null) {
			throw new BusinessException("订单不存在");
		}

		/** step5. 判断订单是否属于登录用户 */
		if (order.getUserId() != token.getUserDto().getId()) {
			throw new BusinessException("该订单不属于您，无法操作");
		}

		/** step6. 删除订单 */
		orderDto.setDeleteStatus(1);
		orderDao.updateOrderDeleteStatus(orderDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("删除成功");
		return responseDto;
	}

	/**
	 * 5.用户选择退款
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	@Override
	public ResponseDto updateOrderRefundStatus(OrderDto orderDto) throws Exception {
		/** step1. 空值判断 */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断订单ID是否为空 */
		if (orderDto.getOrderId() == null || orderDto.getOrderId().equals("")) {
			throw new BusinessException("无效订单");
		}

		/** step3. 根据订单ID得到订单对象. */
		OrderDto order = orderDao.queryOrderByOrderId(orderDto);
		if (order == null) {
			throw new BusinessException("订单不存在");
		}

		if (order.getOrderStatus() == 1) {
			throw new BusinessException("您还没有付款");
		}

		/** step4. 根据token得到用户对象 */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}

		/** step5. 判断订单是否属于登录用户 */
		if (order.getUserId() != token.getUserDto().getId()) {
			throw new BusinessException("该订单不属于您，无法操作");
		}

		/** step6. 用户申请退款 */
		orderDto.setRefundStatus(1);
		orderDto.setRefundTime(Util.getDateTime());
		orderDao.updateOrderRefundStatus(orderDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("申请成功，请等待");
		return responseDto;
	}

	/**
	 * 6.根据订单号以及用户ID得到相关的商品付款信息
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-28
	 */
	@Override
	public ResponseDto queryOrderNews(OrderDto orderDto) throws Exception {
		/** step1. 空值判断 */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}
		
		/** step4. 根据token得到用户对象 */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		orderDto.setUserId(token.getUserDto().getId());

		/** step2. 校验订单编号 */
		if (!Util.isNotEmpty(orderDto.getOrderIds())) {
			throw new BusinessException("用户ID或订单ID集合不能为空");
		}
		
		/** step3. 根据订单编号得到订单集合. */
		List<OrderDto> orders = new ArrayList<OrderDto>();

		// ====================================
		// ====================================
		// ====================================
		JSONArray jsonArray = JSONArray.parseArray(orderDto.getOrderIds());
		System.out.println(orderDto.getOrderIds() + "---");
		if (jsonArray == null || jsonArray.size() == 0) {
			throw new BusinessException("未发现相关订单信息");
		}
		System.out.println(jsonArray.size() + "===");
		for (int n = 0; n < jsonArray.size(); n++) {
			OrderDto queryOrderDto = new OrderDto();
			queryOrderDto.setOrderId(jsonArray.get(n).toString());
			System.out.println(jsonArray.get(n));
			// 查询订单并添加到集合
			orders.add(orderDao.queryOrderByOrderId(queryOrderDto));
		}

		/** step4. 计算订单总金额 */
		int orderCount = 0;
		Double allAmount = 0.0;
		String reciveAddr = "";
		if (orders == null || orders.equals("")) {
			throw new BusinessException("未发现相应订单信息");
		}
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getType() == 1) {
				FoodParams foodParams = new FoodParams();
				foodParams.setFoodId(orders.get(i).getCommodityId());
				FoodParams food = commodityDao.queryFoodById(foodParams);
				orders.get(i).setImageSrc(Enumerate.FOOD_IMG_SRC + orders.get(i).getImageSrc());
				allAmount += food.getPrice() * food.getDiscount() * orders.get(i).getOrderCount();
			}

			if (orders.get(i).getType() == 2) {
				ClothParams clothParams = new ClothParams();
				clothParams.setClothId(orders.get(i).getCommodityId());
				ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
				orders.get(i).setImageSrc(Enumerate.CLOTH_IMG_SRC + orders.get(i).getImageSrc());
				allAmount += cloth.getPrice() * cloth.getDiscount() * orders.get(i).getOrderCount();
			}

			orderCount += orders.get(i).getOrderCount();
			reciveAddr = orders.get(i).getReciveAddress();
		}
		
		/** step5. 获得用户礼品卡 */
		UserDto userDto = new UserDto();
		userDto.setId(orderDto.getUserId());
		UserDto user = userDao.queryUserById(userDto);
		CoinDto coinDto = new CoinDto();
		coinDto.setCoinPhone(user.getPhone());
		coinDto.setCoinStatus(1);
		List<CoinDto> coins = coinDao.queryCoinByPhone(coinDto);
		// 查询用户最大金额礼品卡
		CoinDto maxCoin = coinDao.queryCoinMaxAmount(coinDto);

		/** step6. 获得用户优惠券 */
		CouPonDto couPonDto = new CouPonDto();
		couPonDto.setUserId(orderDto.getUserId());
		List<CouPonDto> couPons = couPonDao.queryCouPonByUserId(couPonDto);
		CouPonDto maxCoupon = couPonDao.queryCouPonMaxAmount(couPonDto);
		
		/** step7. 定义一个变量来赋值最大礼品卡和优惠券总额. */
		Double maxAmount = 0.0;
		if (maxCoin != null) {
			maxAmount += maxCoin.getAmount();
		}
		if (maxCoupon != null) {
			maxAmount += maxCoupon.getCouponAmount();
		}

		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("count", orderCount);
		dataMap.put("coins", coins);
		dataMap.put("couPons", couPons);
		dataMap.put("info", "最大面值礼品卡和优惠券可抵" + maxAmount + "元");
		dataMap.put("orderNo", orderDto.getOrderNo());
		dataMap.put("reciveAddr", reciveAddr);
		dataMap.put("allAmount", Util.Rounding(allAmount));
		dataMap.put("maxAmount", Util.Rounding(maxAmount));
		responseDto.setDetail(dataMap);
		responseDto.setResultDesc("获得用户订单相关支付信息");
		return responseDto;
	}

	/**
	 * 7.根据评论状态得到订单
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-28
	 */
	@Override
	public ResponseDto queryOrderByDiscuss(OrderDto orderDto) throws Exception {
		/** step1. 空值判断 */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 根据token得到用户对象 */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		orderDto.setUserId(token.getUserDto().getId());

		/** step3. 评论状态不能为空 */
		if (orderDto.getDiscussStatus() == null || orderDto.getDiscussStatus().equals("")) {
			throw new BusinessException("用户ID或评论状态不能为空");
		}

		/** step4. 根据评论状态得到订单 */
		// 设置删除状态为0
		orderDto.setDeleteStatus(0);
		List<OrderDto> orders = orderDao.queryOrderByNoDiscuss(orderDto);

		/** 计算订单总金额 */
		Double amount = 0.0;
		for (int i = 0; i < orders.size(); i++) {
			/** 通过商品ID和商品类型type得到商品价格 */
			if (orders.get(i).getType() == 1) {
				FoodParams foodParams = new FoodParams();
				foodParams.setFoodId(orders.get(i).getCommodityId());
				// 查询绿色食品表
				FoodParams food = commodityDao.queryFoodById(foodParams);
				orders.get(i).setCommodityName(food.getFoodName());
				amount += food.getPrice() * food.getDiscount() * orders.get(i).getOrderCount();
				orders.get(i).setImageSrc(Enumerate.FOOD_IMG_SRC + orders.get(i).getImageSrc());
				orders.get(i).setOrderAmount(food.getPrice() * food.getDiscount() * orders.get(i).getOrderCount());
			}

			/** 如果该商品是服装鞋帽 */
			if (orders.get(i).getType() == 2) {
				ClothParams clothParams = new ClothParams();
				clothParams.setClothId(orders.get(i).getCommodityId());
				// 查询服装鞋帽表
				ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
				orders.get(i).setCommodityName(cloth.getClothName());
				amount += cloth.getPrice() * cloth.getDiscount() * orders.get(i).getOrderCount();
				orders.get(i).setImageSrc(Enumerate.CLOTH_IMG_SRC + orders.get(i).getImageSrc());
				orders.get(i).setOrderAmount(cloth.getPrice() * cloth.getDiscount() * orders.get(i).getOrderCount());
			}
		}

		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("用户订单");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("orderList", orders);
		dataMap.put("amount", Util.Rounding(amount));
		dataMap.put("message", "[amount:用户所查询订单总金额];[orderAmount:单笔订单金额]");
		responseDto.setDetail(dataMap);
		return responseDto;

	}

	/**
	 * 8.立即购买提交订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @date 2015-7-13
	 * @author gc
	 * 
	 */
	@Override
	public ResponseDto atoncePayOrder(OrderDto orderDto) throws Exception {

		/** step1. 空值判断. */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 根据token得到用户对象 */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		orderDto.setUserId(token.getUserDto().getId());

		/** step3. 判断收货地址不能为空 */
		if (orderDto.getReciveAddress() == null || orderDto.getReciveAddress().equals("")) {
			throw new BusinessException("收货地址不能为空");// 抛出异常
		}

		/** step4. 判断订购商品的数量和type. */
		if (orderDto.getOrderCount() == null || orderDto.getOrderCount().equals("") || orderDto.getType() == null || orderDto.getType().equals("")) {
			throw new BusinessException("商品订购数量或商品Type不能为空");
		}

		/** step5. 判断商品ID不能为空. */
		if (!Util.isNotEmpty(orderDto.getCommodityId())) {
			throw new BusinessException("商品ID不能为空");
		}

		/** step6. 生成一个订单编号，付款时操作. */
		String orderNo = Util.getUniqueSn();//

		/** step7. 根据地址ID得到地址对象. */
		AddressDto addr = new AddressDto();
		addr.setAddrId(orderDto.getReciveAddress());
		AddressDto addressDto = addressDao.queryAddrByAddrId(addr);
		if (addressDto == null || addressDto.equals("")) {
			throw new BusinessException("未找到相关地址");
		}
		String reciveAddress = addressDto.getProvince() + addressDto.getCity() + addressDto.getArea() + addressDto.getStreet() + "；" + "邮编：" + addressDto.getCode() + "收货人：" + addressDto.getPersonName() + "联系方式：" + addressDto.getPersonPhone();

		List<String> ids = new ArrayList<String>();

		/** 设置订单ID. */
		orderDto.setOrderId(Util.getUniqueSn());

		/** 设置订购日期. */
		orderDto.setOrderTime(Util.getDateTime());
		/** 设置订单状态. 1未付款 */
		orderDto.setOrderStatus(1);
		/** 设置评论状态. */
		orderDto.setDiscussStatus(0);
		/** 设置退款状态. */
		orderDto.setRefundStatus(0);
		/** 设置订单删除状态. */
		orderDto.setDeleteStatus(0);
		
		Double amount = 0.0;
		/** 设置订单中图片地址名. */
		if(orderDto.getType()==1){
			FoodParams foodParams = new FoodParams();
			foodParams.setFoodId(orderDto.getCommodityId());
			FoodParams food = commodityDao.queryFoodById(foodParams);
			orderDto.setImageSrc(food.getIndexSrc());
			amount = food.getPrice() * food.getDiscount() * orderDto.getOrderCount();
		}
		
		if(orderDto.getType()==2){
			ClothParams clothParams = new ClothParams();
			clothParams.setClothId(orderDto.getCommodityId());
			ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
			amount = cloth.getPrice() * cloth.getDiscount() * orderDto.getOrderCount();

			if(Util.isNotEmpty(orderDto.getColorImgId())){
				ClothImg clothImg = new ClothImg();
				clothImg.setImageId(orderDto.getColorImgId());
				ClothImg cImg = imageDao.queryClothImgByImgId(clothImg);
				orderDto.setImageSrc(cImg.getImageName());
			}else{
				orderDto.setImageSrc(cloth.getClothImg());
			}
		}

		/** 设置收货地址. */
		orderDto.setReciveAddress(reciveAddress);

		/** step8. 添加订单. */
		orderDao.addOrderDto(orderDto);


		/** step9. 返回结果 */
		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> dataMap = new HashMap<String, Object>();

		/** step10. 获得用户礼品卡金额. */

		UserDto userDto = new UserDto();
		userDto.setId(orderDto.getUserId());
		UserDto user = userDao.queryUserById(userDto);

		/** 通过用户手机号得到礼品卡集合 . */
		CoinDto coinDto = new CoinDto();
		coinDto.setCoinPhone(user.getPhone());
		CoinDto coin = coinDao.queryCoinMaxAmount(coinDto);

		Double coinAmount = 0.0;
		if (coin == null || coin.equals("")) {
			coinAmount = 0.0;
		} else {
			/** 最大礼品卡金额. */
			coinAmount = coin.getAmount();
		}

		Double couponAmount = 0.0;
		/** 查询用户最大优惠券. */
		CouPonDto couponDto = new CouPonDto();
		couponDto.setUserId(orderDto.getId());
		CouPonDto coupon = couPonDao.queryCouPonMaxAmount(couponDto);
		if (coupon == null || coupon.equals("")) {
			couponAmount = 0.0;
		} else {
			couponAmount = coupon.getCouponAmount();
		}

		ids.add(orderDto.getOrderId());
		dataMap.put("orderNo", orderNo);
		dataMap.put("ids", ids);
		dataMap.put("amount", amount);
		dataMap.put("info", "您的最大优惠券和礼品卡可抵消" + Util.Rounding(couponAmount + coinAmount) + "元");
		responseDto.setResultDesc("添加成功");
		responseDto.setDetail(dataMap);
		return responseDto;
	}
}
