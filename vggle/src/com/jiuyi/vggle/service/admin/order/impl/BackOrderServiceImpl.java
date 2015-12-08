package com.jiuyi.vggle.service.admin.order.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.vggle.common.dict.CacheContainer;
import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.util.Enumerate;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.commodity.CommodityDao;
import com.jiuyi.vggle.dao.order.OrderDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.TokenDto;
import com.jiuyi.vggle.dto.commodity.params.ClothParams;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;
import com.jiuyi.vggle.dto.order.OrderDto;
import com.jiuyi.vggle.dto.user.UserDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.admin.order.BackOrderService;

@Service
public class BackOrderServiceImpl implements BackOrderService {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private CommodityDao commodityDao;

	/**
	 * 1.订单 状态 查询订单
	 *
	 * @return
	 * @throws Exception
	 * @date 2015-6-29
	 * @author gc
	 */
	@Override
	public ResponseDto queryOrderByCompanyIdOrderStatus(OrderDto orderDto) throws Exception {
		/** step1: judge null */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		orderDto.setUserId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		orderDto.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: judge orderStatus */
		if (orderDto.getOrderStatus() == null || orderDto.getOrderStatus().equals("")) {
			throw new BusinessException("订单状态不能为空");
		}

		/** step5: permission */
		UserDto userDto = new UserDto();
		userDto.setId(orderDto.getUserId());

		if (!CacheContainer.user_cmd(userDto).contains(orderDto.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step6: query order by status */
		Double amount = 0.0;
		int size = 0;

		List<OrderDto> orders = new ArrayList<OrderDto>();

		if (orderDto.getOrderStatus() == 0) {
			orders = orderDao.queryAllOrderByCompanyId(orderDto);
		} else {
			orders = orderDao.queryOrderByCompanyIdAndOrderStatus(orderDto);
		}

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

		size = orders.size();

		/** step4: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("用户订单");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("orderList", orders);
		dataMap.put("count", size);
		dataMap.put("amount", Util.Rounding(amount));
		dataMap.put("message", "[amount:用户所查询订单总金额];[orderAmount:单笔订单金额]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 2.订单 ID 查询订单(用于查询用户单笔订单)
	 *
	 * @return
	 * @throws Exception
	 * @date 2015-6-29
	 * @author gc
	 */
	@Override
	public ResponseDto queryOrderByCompanyIdOrderId(OrderDto orderDto) throws Exception {
		/** step1: judge null */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		orderDto.setUserId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		orderDto.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: judge orderStatus */
		if (!Util.isNotEmpty(orderDto.getOrderId())) {
			throw new BusinessException("订单ID不能为空");
		}

		/** step5: permission */
		UserDto userDto = new UserDto();
		userDto.setId(orderDto.getUserId());
		if (!CacheContainer.user_cmd(userDto).contains(orderDto.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step5: query order by no */
		Double amount = 0.0;
		List<OrderDto> orders = orderDao.queryOrderByCompanyIdAndOrderId(orderDto);
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

		/** step4: result */
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
	 * 3.评论状态 查询订单
	 *
	 * @return
	 * @throws Exception
	 * @date 2015-6-29
	 * @author gc
	 */
	@Override
	public ResponseDto queryOrderByCompanyIdDiscuss(OrderDto orderDto) throws Exception {
		/** step1: judge null */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		orderDto.setUserId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		orderDto.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: judge discussStatus */
		if (orderDto.getDiscussStatus() == null || orderDto.getDiscussStatus().equals("")) {
			throw new BusinessException("评论状态不能为空");
		}

		/** step5: permission */
		UserDto userDto = new UserDto();
		userDto.setId(orderDto.getUserId());
		if (!CacheContainer.user_cmd(userDto).contains(orderDto.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step6: query order bu discuss */
		Double amount = 0.0;
		List<OrderDto> orders = orderDao.queryOrderByCompanyIdAndDiscussStatus(orderDto);

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

		/** step4: result */
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
	 * 4.删除状态——查询订单
	 *
	 * @return
	 * @throws Exception
	 * @date 2015-6-29
	 * @author gc
	 */
	@Override
	public ResponseDto queryOrderByCompanyIdDelete(OrderDto orderDto) throws Exception {
		/** step1: judge null */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		orderDto.setUserId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		orderDto.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: judge orderStatus */
		if (orderDto.getDeleteStatus() == null || orderDto.getDeleteStatus().equals("")) {
			throw new BusinessException("删除状态不能为空");
		}

		/** step5: permission */
		UserDto userDto = new UserDto();
		userDto.setId(orderDto.getUserId());
		if (!CacheContainer.user_cmd(userDto).contains(orderDto.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step6: query order by delete status */
		Double amount = 0.0;
		List<OrderDto> orders = orderDao.queryOrderByCompanyIdAndDeleteStatus(orderDto);
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

		/** step7: result */
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
	 * 5.根据付款日期查询订单
	 *
	 * @param orderDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto queryOrderByCompanyIdPayDate(OrderDto orderDto) throws Exception {
		/** step1: judge null */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		orderDto.setUserId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		orderDto.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: judge code */
		if (orderDto.getCode() == null || orderDto.getCode().equals("")) {
			throw new BusinessException("日期标识码不能为空");
		}

		/** step5: permission */
		UserDto userDto = new UserDto();
		userDto.setId(orderDto.getUserId());
		if (!CacheContainer.user_cmd(userDto).contains(orderDto.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step3: query order by date */
		Double amount = 0.0;
		List<OrderDto> orders = new ArrayList<OrderDto>();

		/** code: 1 最近一天的, 2最近三天的, 3最近一周的, 4最近一个月的, 5最近三个月, 6最近半年 */
		if (orderDto.getCode() == 1) {
			Calendar curr = Calendar.getInstance();
			curr.set(Calendar.DATE, curr.get(Calendar.DATE) - 1);
			orderDto.setPayTime(curr.getTime());
		}

		if (orderDto.getCode() == 2) {
			Calendar curr = Calendar.getInstance();
			curr.set(Calendar.DATE, curr.get(Calendar.DATE) - 3);
			orderDto.setPayTime(curr.getTime());
		}

		if (orderDto.getCode() == 3) {
			Calendar curr = Calendar.getInstance();
			curr.set(Calendar.DATE, curr.get(Calendar.DATE) - 7);
			orderDto.setPayTime(curr.getTime());
		}

		if (orderDto.getCode() == 4) {
			Calendar curr = Calendar.getInstance();
			curr.set(Calendar.MONTH, curr.get(Calendar.MONTH) - 1);
			orderDto.setPayTime(curr.getTime());
		}

		if (orderDto.getCode() == 5) {
			Calendar curr = Calendar.getInstance();
			curr.set(Calendar.MONTH, curr.get(Calendar.MONTH) - 3);
			orderDto.setPayTime(curr.getTime());
		}

		if (orderDto.getCode() == 6) {
			Calendar curr = Calendar.getInstance();
			curr.set(Calendar.MONTH, curr.get(Calendar.MONTH) - 6);
			orderDto.setPayTime(curr.getTime());
		}
		System.out.println(orderDto.getPayTime());
		orders = orderDao.queryOrderByCompanyIdAndPayTime(orderDto);

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

		/** step4: result */
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
	 * 6.修改订单（确认发货）
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-7
	 */
	@Override
	public ResponseDto alterOrderStatusByCompanyId(OrderDto orderDto) throws Exception {
		/** step1: judge null */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		orderDto.setUserId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		orderDto.setCompanyId(token.getUserDto().getCompanyId());

		/** step3: judge companyId orderId */
		if (!Util.isNotEmpty(orderDto.getOrderId())) {
			throw new BusinessException("订单ID不能为空");
		}

		/** step4: permission */
		UserDto userDto = new UserDto();
		userDto.setId(orderDto.getUserId());
		if (!CacheContainer.user_cmd(userDto).contains(orderDto.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step3: exe alter */
		orderDto.setShipmentsTime(Util.getDateTime());
		orderDto.setOrderStatus(3);
		orderDao.updateOrderShipmentsTimeAndStatus(orderDto);

		/** step4: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("用户订单");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("message", "发货成功");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 7.删除订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @DATE 2015-7-17
	 * @author gc
	 */
	@Override
	public ResponseDto deleteOrderByCompanyId(OrderDto orderDto) throws Exception {
		/** step1: judge null. */
		if (orderDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: judge orderId */
		if (!Util.isNotEmpty(orderDto.getOrderId()) || !Util.isNotEmpty(orderDto.getCompanyId())) {
			throw new BusinessException("订单ID或商家ID不能为空");
		}

		/** step4: judge userId */
		TokenDto token = CacheContainer.getToken(orderDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		orderDto.setUserId(token.getUserDto().getId());

		/** step5: permission */
		UserDto userDto = new UserDto();
		userDto.setId(orderDto.getUserId());
		if (!CacheContainer.user_cmd(userDto).contains(orderDto.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}
		
		/** step6: query order by id */
		OrderDto order = orderDao.queryOrderByOrderId(orderDto);
		if(order ==null){
			throw new BusinessException("订单不存在");
		}
		if(order.getDeleteStatus()==0){
			throw new BusinessException("该订单不可删除");
		}
		
		/** step7: exe delete */
		orderDao.deleteOrderByCompanyId(orderDto);
		
		/** step8: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("订单删除结果");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("message", "删除成功");
		responseDto.setDetail(dataMap);
		return responseDto;
	}
}
