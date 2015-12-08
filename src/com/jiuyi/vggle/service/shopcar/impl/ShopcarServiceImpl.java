package com.jiuyi.vggle.service.shopcar.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.vggle.common.dict.CacheContainer;
import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.util.Enumerate;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.coin.CoinDao;
import com.jiuyi.vggle.dao.commodity.CommodityDao;
import com.jiuyi.vggle.dao.commodity.image.ImageDao;
import com.jiuyi.vggle.dao.commodity.size.SizeDao;
import com.jiuyi.vggle.dao.coupon.CouPonDao;
import com.jiuyi.vggle.dao.user.UserDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.TokenDto;
import com.jiuyi.vggle.dto.coin.CoinDto;
import com.jiuyi.vggle.dto.commodity.image.ClothImg;
import com.jiuyi.vggle.dto.commodity.params.ClothParams;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;
import com.jiuyi.vggle.dto.commodity.sizesku.SsDto;
import com.jiuyi.vggle.dto.coupon.CouPonDto;
import com.jiuyi.vggle.dto.shopcar.ShopCarDto;
import com.jiuyi.vggle.dto.user.UserDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.shopcar.ShopcarService;

@Service
public class ShopcarServiceImpl implements ShopcarService {
	private final static Logger logger = Logger.getLogger(ShopcarServiceImpl.class);

	@Autowired
	private CommodityDao commodityDao;

	@Autowired
	private ImageDao imageDao;

	@Autowired
	private CoinDao coinDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private CouPonDao couPonDao;

	@Autowired
	private SizeDao sizeDao;

	private static ConcurrentHashMap<Integer, List<ShopCarDto>> uid_ShopCar = new ConcurrentHashMap<Integer, List<ShopCarDto>>();

	/**
	 * 1. 用户添加购物车
	 * 
	 * @param shopCarDto
	 */
	@Override
	public ResponseDto addShop(ShopCarDto shopCarDto) throws Exception {
		/** step1. 空值判断 */
		if (shopCarDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2： 根据token得到用户对象. */
		TokenDto token = CacheContainer.getToken(shopCarDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		shopCarDto.setUserId(token.getUserDto().getId());

		/** step3. 用户ID、商品ID,空值判断 */
		if (!Util.isNotEmpty(shopCarDto.getCommodityId())) {
			throw new BusinessException("商品ID不能为空");
		}

		/** step4. 购买数量和金额判断 */
		if (shopCarDto.getBuyCount() == null || shopCarDto.getBuyCount().equals("")) {
			throw new BusinessException("商品购买数量不能为空");
		}

		/** step5. 商品type不能为空 */
		if (shopCarDto.getType() == null || shopCarDto.getType().equals("")) {
			throw new BusinessException("商品type不能为空");
		}


		/** step5. 根据Type 和 商品ID——> 得到绿色食品系列 */
		int sku = 0;
		if (shopCarDto.getType() == 1) {
			FoodParams food = new FoodParams();
			FoodParams foodParams = new FoodParams();
			foodParams.setFoodId(shopCarDto.getCommodityId());
			food = commodityDao.queryFoodById(foodParams);
			sku = food.getFoodSku();

			/** 设置购物车商品名称. */
			shopCarDto.setCommodityName(food.getFoodName());

			/** 设置商品厂家名称. */
			shopCarDto.setCommodityFactor(food.getFactoryName());

			/** 设置购物车ID. */
			shopCarDto.setCarId(Util.getUniqueSn());

			/** 设置购物车状态. */
			shopCarDto.setCarStatus(0);

			/** 设置购物车商品图片地址. */
			shopCarDto.setImageSrc(Enumerate.FOOD_IMG_SRC + food.getIndexSrc());

			/** 设置购物车商品图片名称. */
			shopCarDto.setImageName(food.getIndexSrc());

			/** 设置商家ID. */
			shopCarDto.setCompanyId(food.getCompanyId());
		}

		/** step5. 根据Type 和 商品ID——> 得到服装系列 */
		if (shopCarDto.getType() == 2) {
			ClothParams cloth = new ClothParams();
			ClothParams clothParams = new ClothParams();
			clothParams.setClothId(shopCarDto.getCommodityId());
			cloth = commodityDao.queryClothByClothId(clothParams);

			// 获得该服装类商品的库存
			SsDto ssDto = new SsDto();
			ssDto.setCommodityId(shopCarDto.getCommodityId());
			ssDto.setImageId(shopCarDto.getColorImgId());
			ssDto.setSize(shopCarDto.getSize());
			SsDto ss = sizeDao.queryByImageIdAndSize(ssDto);
			if (ss != null) {
				sku = ss.getSku();
			}

			/** 设置购物车商品名称. */
			shopCarDto.setCommodityName(cloth.getClothName());

			/** 设置商品厂家名称. */
			shopCarDto.setCommodityFactor(cloth.getFactoryName());

			/** 设置购物车ID. */
			shopCarDto.setCarId(Util.getUniqueSn());

			/** 设置购物车状态. */
			shopCarDto.setCarStatus(0);

			/** 设置商家ID. */
			shopCarDto.setCompanyId(cloth.getCompanyId());

			/** 设置购物车商品图片名称. */
			/** 查询商品颜色地址. */
			ClothImg clothImg = new ClothImg();
			clothImg.setImageId(shopCarDto.getColorImgId());
			ClothImg cImg = imageDao.queryClothImgByImgId(clothImg);
			shopCarDto.setColorName(cImg.getColorName());
			shopCarDto.setImageName(cImg.getImageName());
			shopCarDto.setImageSrc(Enumerate.CLOTH_IMG_SRC + cImg.getImageName());
		}

		/** step4. 判断库存 */
		if (!uid_ShopCar.containsKey(shopCarDto.getUserId())) {
			if (shopCarDto.getBuyCount() > sku) {
				throw new BusinessException("库存不足");
			}
		} else {
			// 查询该用户购物车中改商品的数量
			int prodSku = 0;
			for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId().equals(shopCarDto.getCommodityId())) {
					prodSku = uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
					break;
				}
			}
			if (prodSku + shopCarDto.getBuyCount() > sku) {
				throw new BusinessException("库存不足");
			}
		}
		/** step5. 添加购物车 */
		int result = 0;
		if (!uid_ShopCar.containsKey(shopCarDto.getUserId())) {
			List<ShopCarDto> shopCars = new ArrayList<ShopCarDto>();
			shopCars.add(shopCarDto);
			uid_ShopCar.putIfAbsent(shopCarDto.getUserId(), shopCars);
		} else {
			synchronized (uid_ShopCar.get(shopCarDto.getUserId())) {
				for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {

					/** 如果购物车存在该商品 */
					if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId().equals(shopCarDto.getCommodityId())) {

						/** 改变购物车数量 */
						uid_ShopCar.get(shopCarDto.getUserId()).get(i).setBuyCount(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount() + shopCarDto.getBuyCount());
						result = 1;
						break;
					}
				}
				if (result == 0) {
					/** 如果购物车不存在该商 */
					uid_ShopCar.get(shopCarDto.getUserId()).add(shopCarDto);
				}
			}
		}
		/** step6. 查询数量 */
		int num = 0;
		Double amount = 0.0;
		for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
			num += uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();

			/** 如果该件商品是绿色食品 */
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 1) {
				FoodParams foodParams = new FoodParams();
				foodParams.setFoodId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
				FoodParams food = commodityDao.queryFoodById(foodParams);
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarStatus() == 0) {
					amount += food.getPrice() * food.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
				}
			}

			/** 如果该件商品是服装鞋帽 */
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 2) {
				ClothParams clothParams = new ClothParams();
				clothParams.setClothId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
				ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarStatus() == 0) {
					amount += cloth.getPrice() * cloth.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
				}
			}
		}

		/** step7. 创建结果对象. */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("添加成功");
		Map<String, Object> dataMap = new HashMap<String, Object>();

		/** step8. 获得用户礼品卡金额. */
		/** 通过用户ID得到用户对象. */
		UserDto userDto = new UserDto();
		userDto.setId(shopCarDto.getUserId());
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
		couponDto.setUserId(shopCarDto.getId());
		CouPonDto coupon = couPonDao.queryCouPonMaxAmount(couponDto);
		if (coupon == null || coupon.equals("")) {
			couponAmount = 0.0;
		} else {
			couponAmount = coupon.getCouponAmount();
		}

		dataMap.put("info", "您的最大面值礼品卡和优惠券可抵" + Util.Rounding(coinAmount + couponAmount) + "元");

		dataMap.put("count", num);
		dataMap.put("amount", Util.Rounding(amount));
		dataMap.put("coinAmount", Util.Rounding(coinAmount));
		dataMap.put("message", "[count:购物车商品数量]; [amount:购物车选中的商品总价]; [coinAmount:最大一张物谷币价格]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 2.通过用户ID查询用户购物车对象
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public ResponseDto queryShopCarByUserId(ShopCarDto shopCarDto) throws Exception {
		/** step1. 空值判断 */
		if (shopCarDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 根据token得到用户对象. */
		TokenDto token = CacheContainer.getToken(shopCarDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		shopCarDto.setUserId(token.getUserDto().getId());

		/** step3. 判断用户购物车是否为空. */
		if (!uid_ShopCar.containsKey(shopCarDto.getUserId())) {
			throw new BusinessException("购物车暂时没有任何商品");
		}
		/** step4. 查询用户购物车对象. */
		List<ShopCarDto> shopcar = uid_ShopCar.get(shopCarDto.getUserId());

		/** step5. 计算购物车中的金额. */
		Double amount = 0.0;
		for (int i = 0; i < shopcar.size(); i++) {
			/** 通过商品ID得到商品价格. 查询绿色食品表 */
			if (shopcar.get(i).getType() == 1) {
				FoodParams foodParams = new FoodParams();
				foodParams.setFoodId(shopcar.get(i).getCommodityId());
				FoodParams food = commodityDao.queryFoodById(foodParams);
				if (shopcar.get(i).getCarStatus() == 0) {
					amount += food.getPrice() * food.getDiscount() * shopcar.get(i).getBuyCount();
				}
				shopcar.get(i).setCommodityPrice(food.getPrice());
				shopcar.get(i).setDiscount(food.getDiscount());
				shopcar.get(i).setAmount(food.getPrice() * food.getDiscount() * shopcar.get(i).getBuyCount());
			}

			/** 通过商品ID得到商品价格. 查询绿色食品表 */
			if (shopcar.get(i).getType() == 2) {
				ClothParams clothParams = new ClothParams();
				clothParams.setClothId(shopcar.get(i).getCommodityId());
				ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
				if (shopcar.get(i).getCarStatus() == 0) {
					amount += cloth.getPrice() * cloth.getDiscount() * shopcar.get(i).getBuyCount();
				}
				shopcar.get(i).setCommodityPrice(cloth.getPrice());
				shopcar.get(i).setDiscount(cloth.getDiscount());
				shopcar.get(i).setAmount(cloth.getPrice() * cloth.getDiscount() * shopcar.get(i).getBuyCount());
			}

		}

		/** step6.判断用户的购物车商品是否是全选. */
		int checkResult = 0;
		for (int i = 0; i < shopcar.size(); i++) {
			if (shopcar.get(i).getCarStatus() == 0) {
				checkResult = 0;
			} else {
				checkResult = 1;
				break;
			}
		}

		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("用户购物车");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("userCar", shopcar);
		dataMap.put("allAmount", Util.Rounding(amount));
		dataMap.put("checkResult", checkResult);
		dataMap.put("message", "[userCar:用户购物车集合]; [allAmount:总价]; [checkResult:是否全选状态码,0全选,1不全选]; [commodityPrice:单价]; [amount:小计]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 3.购物车添加数量
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public ResponseDto upShopCar(ShopCarDto shopCarDto) throws Exception {
		/** step1. 空值判断 */
		if (shopCarDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 根据token得到用户对象. */
		TokenDto token = CacheContainer.getToken(shopCarDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}

		shopCarDto.setUserId(token.getUserDto().getId());

		/** step3. 用户ID、购物车ID,空值判断 */
		if (!Util.isNotEmpty(shopCarDto.getCarId())) {
			throw new BusinessException("用户ID或者购物车ID不能为空");
		}

		/** step4. 购买数量判断用商品type分类 */
		if (shopCarDto.getBuyCount() == null || shopCarDto.getBuyCount().equals("") || shopCarDto.getType() == null || shopCarDto.getType().equals("")) {
			throw new BusinessException("商品购买数量或者商品type类型不能为空");
		}

		/** step5. 通过商品ID查询商品对象 得到库存 */
		Integer sku = 0;
		if (shopCarDto.getType() == 1) {
			FoodParams foodParams = new FoodParams();
			foodParams.setFoodId(shopCarDto.getCommodityId());
			FoodParams food = commodityDao.queryFoodById(foodParams);
			sku = food.getFoodSku();
		}

		if (shopCarDto.getType() == 2) {
			// 获取服装类商品库存
			// 先通过购物车ID得到购物车对象
			ShopCarDto thisCar = new ShopCarDto();
			thisCar = queryShopCarById(shopCarDto);

			// 开始通过颜色图片ID和尺码得到库存
			SsDto ssDto = new SsDto();
			ssDto.setCommodityId(shopCarDto.getCommodityId());
			ssDto.setImageId(thisCar.getColorImgId());
			ssDto.setSize(thisCar.getSize());

			SsDto ss = sizeDao.queryByImageIdAndSize(ssDto);
			if (ss != null) {
				sku = ss.getSku();
			}

		}

		/** step6. 查询购物车中该商品的数量判断是否超过库存 . */
		if (!uid_ShopCar.containsKey(shopCarDto.getUserId())) {
			if (shopCarDto.getBuyCount() > sku) {
				throw new BusinessException("库存不足");
			}
		} else {
			// 查询该用户购物车中改商品的数量
			int prodSku = 0;
			for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId().equals(shopCarDto.getCommodityId())) {
					prodSku = uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
					break;
				}
			}
			if (prodSku + shopCarDto.getBuyCount() > sku) {
				throw new BusinessException("库存不足");
			}
		}

		/** step7. 添加数量 */
		// 声明一个价格变量,用于存贮修改商品的小计
		Double price = 0.0;

		for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarId().equals(shopCarDto.getCarId())) {
				uid_ShopCar.get(shopCarDto.getUserId()).get(i).setBuyCount(shopCarDto.getBuyCount() + uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount());
				/** 得到绿色食品价格. */
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 1) {
					FoodParams foodParams = new FoodParams();
					foodParams.setFoodId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
					FoodParams food = commodityDao.queryFoodById(foodParams);
					price = food.getPrice() * food.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
				}
				/** 得到服装鞋帽价格. */
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 2) {
					ClothParams clothParams = new ClothParams();
					clothParams.setClothId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
					ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
					price = cloth.getPrice() * cloth.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
				}

				break;
			}
		}

		/** step8. 获得购物车数量 */
		int num = 0;
		Double amount = 0.0;
		for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
			num += uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();

			/** 该商品是绿色食品时的价格 */
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 1) {
				FoodParams foodParams = new FoodParams();
				foodParams.setFoodId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
				FoodParams food = commodityDao.queryFoodById(foodParams);
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarStatus() == 0) {
					amount += food.getPrice() * food.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
				}
			}

			/** 该商品时服装鞋帽时的价格 */
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 2) {
				ClothParams clothParams = new ClothParams();
				clothParams.setClothId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
				ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarStatus() == 0) {
					amount += cloth.getPrice() * cloth.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
				}
			}
		}
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("增加成功");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("count", num);
		dataMap.put("amount", Util.Rounding(amount));
		dataMap.put("price", Util.Rounding(price));
		dataMap.put("message", "[count:购物车商品总数量]; [amount:总价]; [price:当前商品的小计]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 4.购物车减少数量
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public ResponseDto downShopCar(ShopCarDto shopCarDto) throws Exception {
		/** step1. 空值判断 */
		if (shopCarDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 商品ID,空值判断 */
		TokenDto token = CacheContainer.getToken(shopCarDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		shopCarDto.setUserId(token.getUserDto().getId());

		/** step3. 购买数量判断 */
		if (shopCarDto.getBuyCount() == null || shopCarDto.getBuyCount().equals("")) {
			throw new BusinessException("商品购买数量不能为空");
		}

		/** step4. 减少数量 */
		// 声明一个价格变量,用于存贮修改商品的小计
		Double price = 0.0;
		for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarId().equals(shopCarDto.getCarId())) {
				if (uid_ShopCar.get(shopCarDto.getUserId()).size() == 0) {
					throw new BusinessException("商品数量已经为0了");
				}
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount() == 1) {
					throw new BusinessException("该商品已经是最低数量了");
				}
				uid_ShopCar.get(shopCarDto.getUserId()).get(i).setBuyCount(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount() - shopCarDto.getBuyCount());

				/** 该商品是绿色食品时 */
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 1) {
					FoodParams foodParams = new FoodParams();
					foodParams.setFoodId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
					FoodParams food = commodityDao.queryFoodById(foodParams);
					price = food.getPrice() * food.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
				}

				/** 该商品是服装鞋帽时 */
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 2) {
					ClothParams clothParams = new ClothParams();
					clothParams.setClothId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
					ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
					price = cloth.getPrice() * cloth.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
				}
				break;
			}
		}

		/** step6. 获得购物车数量 */
		int num = 0;
		Double amount = 0.0;
		for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
			num += uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
			/** 当该商品是绿色食品时 */
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 1) {
				FoodParams foodParams = new FoodParams();
				foodParams.setFoodId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
				FoodParams food = commodityDao.queryFoodById(foodParams);
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarStatus() == 0) {
					amount += food.getPrice() * food.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
				}
			}

			/** 该商品时服装鞋帽时的价格 */
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 2) {
				ClothParams clothParams = new ClothParams();
				clothParams.setClothId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
				ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarStatus() == 0) {
					amount += cloth.getPrice() * cloth.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
				}
			}
		}

		/** step7. 获得当前修改的商品价格. */

		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("count", num);
		dataMap.put("amount", Util.Rounding(amount));
		dataMap.put("price", Util.Rounding(price));
		dataMap.put("message", "[num:购物车商品总数量]; [amount:总价]; [price:当前商品的小计]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 5.查询购物车数量
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto queryBuyCount(ShopCarDto shopCarDto) throws Exception {
		/** step1. 空值判断 */
		if (shopCarDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 用户ID空值判断. */
		TokenDto token = CacheContainer.getToken(shopCarDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		shopCarDto.setUserId(token.getUserDto().getId());

		/** step3. 查询数量 */
		int num = 0;

		if (uid_ShopCar.containsKey(shopCarDto.getUserId()) && uid_ShopCar.get(shopCarDto.getUserId()) != null) {
			for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
				num += uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
			}
		}

		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("购物车数量");
		responseDto.setDetail(num);
		return responseDto;
	}

	/**
	 * 6.删除购物车
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto deleteCar(ShopCarDto shopCarDto) throws Exception {
		/** step1. 空值判断 */
		if (shopCarDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 用户ID和购物车ID 空值判断. */
		if (shopCarDto.getUserId() == null || shopCarDto.getUserId().equals("") || shopCarDto.getCarId() == null || shopCarDto.getCarId().trim().equals(""))
		{
			throw new BusinessException("用户ID或购物车ID为空");
		}

		/** step3. 删除购物车 */
		int result = 0;

		for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
			logger.info("ShopcarServiceImpl.deleteCar:-进入普通删除购物车方法，找到需要删除的购物车对象");
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarId().equals(shopCarDto.getCarId())) {
				uid_ShopCar.get(shopCarDto.getUserId()).remove(i);
				result = 1;
				break;
			}
		}

		if (result == 0) {
			throw new BusinessException("无效购物车ID");
		}

		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> dataMap = new HashMap<String, Object>();

		dataMap.put("message", "删除成功");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 7.通过购物车ID得到购物车对象
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 * 
	 */
	@Override
	public ShopCarDto queryShopCarById(ShopCarDto shopCarDto) throws Exception {
		ShopCarDto shopCar = new ShopCarDto();
		for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarId().equals(shopCarDto.getCarId())) {
				shopCar = uid_ShopCar.get(shopCarDto.getUserId()).get(i);
				break;
			}
		}
		return shopCar;
	}

	/**
	 * 8.修改购物车状态
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 * 
	 */
	@Override
	public ResponseDto updateCarStatus(ShopCarDto shopCarDto) throws Exception {
		/** step1. 空值判断 */
		if (shopCarDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 用户、购物车ID 空值判断. */
		if (shopCarDto.getCarId() == null || shopCarDto.getCarId().trim().equals("") || shopCarDto.getUserId()==null || shopCarDto.getUserId().equals("")) {
			throw new BusinessException("用户ID和购物车ID不能为空");
		}
		
		/** step3. 判断购物车carStatus不能为空. */
		if (shopCarDto.getCarStatus() == null || shopCarDto.getCarStatus().equals("")) {
			throw new BusinessException("购物车状态不能为空");
		}

		/** step4. 更改购物车状态. */
		int result = 0;
		for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarId().equals(shopCarDto.getCarId())) {
				uid_ShopCar.get(shopCarDto.getUserId()).get(i).setCarStatus(shopCarDto.getCarStatus());
				result = 1;
				break;
			}
		}
		if (result == 0) {
			throw new BusinessException("无效购物车ID");
		}

		/** step5. 计算总金额. */
		Double amount = 0.0;
		for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
			logger.info("ShopCarServiceImpl.updateCarStatus. 修改购物车状态开始计算购物车总价");
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarStatus() == 0) {
				logger.info("ShopCarServiceImpl.updateCarStatus. 当购物车是选中的情况下才进行金额累加");
				/** 该商品是绿色食品时 */
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 1) {
					logger.info("ShopCarServiceImpl.updateCarStatus. 购物车商品type=1");
					FoodParams foodParams = new FoodParams();
					foodParams.setFoodId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
					FoodParams food = commodityDao.queryFoodById(foodParams);
					if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarStatus() == 0) {
						amount += food.getPrice() * food.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
					}
				}

				/** 该商品是服装鞋帽时 */
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 2) {
					logger.info("ShopCarServiceImpl.updateCarStatus. 购物车商品type=2");
					ClothParams clothParams = new ClothParams();
					clothParams.setClothId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
					ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
					if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarStatus() == 0) {
						amount += cloth.getPrice() * cloth.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
					}
				}
			}
		}

		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("更改成功,获得购物车状态,和本记录价格小计");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("carStatus", shopCarDto.getCarStatus());
		dataMap.put("amount", Util.Rounding(amount));
		dataMap.put("message", "[carStatus:购物车当前商品状态]; [amount:总价]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 10.获得购物车选中购买的商品
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 * 
	 */
	@Override
	public ResponseDto queryCarBuyOk(ShopCarDto shopCarDto) throws Exception {
		/** step1. 空值判断 */
		if (shopCarDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 用户ID 空值判断. */
		if (shopCarDto.getUserId() == null || shopCarDto.getUserId().equals("")) {
			throw new BusinessException("用户ID不能为空");
		}

		/** step3. 判断用户购物车是否为空. */
		if(!uid_ShopCar.containsKey(shopCarDto.getUserId())){
			throw new BusinessException("您的购物车目前没有商品");
		}

		/** step4. 通过循环获得购买的商品 */
		Double amount = 0.0;
		int num = 0;
		List<ShopCarDto> userCars = new ArrayList<ShopCarDto>();
		for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarStatus() == 0) {

				/** 如果是绿色食品时 */
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 1) {
					FoodParams foodParams = new FoodParams();
					foodParams.setFoodId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
					FoodParams food = commodityDao.queryFoodById(foodParams);
					amount += food.getPrice() * food.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
					uid_ShopCar.get(shopCarDto.getUserId()).get(i).setCommodityPrice(food.getPrice());
					uid_ShopCar.get(shopCarDto.getUserId()).get(i).setDiscount(food.getDiscount());
					uid_ShopCar.get(shopCarDto.getUserId()).get(i).setAmount(food.getPrice() * food.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount());
				}

				/** 如果该商品是服装鞋帽时 */
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 2) {
					ClothParams clothParams = new ClothParams();
					clothParams.setClothId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
					ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
					amount += cloth.getPrice() * cloth.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
					uid_ShopCar.get(shopCarDto.getUserId()).get(i).setCommodityPrice(cloth.getPrice());
					uid_ShopCar.get(shopCarDto.getUserId()).get(i).setDiscount(cloth.getDiscount());
					uid_ShopCar.get(shopCarDto.getUserId()).get(i).setAmount(cloth.getPrice() * cloth.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount());
				}
				userCars.add(uid_ShopCar.get(shopCarDto.getUserId()).get(i));
				num += uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
			}
		}

		/** step5. 返回结果. */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("获取成功");
		Map<String, Object> dataMap = new HashMap<String, Object>();

		/** step6. 获得用户礼品卡金额. */
		UserDto userDto = new UserDto();
		userDto.setId(shopCarDto.getUserId());
		UserDto user = userDao.queryUserById(userDto);

		/** step7. 通过用户手机号得到礼品卡集合 . */
		CoinDto coinDto = new CoinDto();
		coinDto.setCoinPhone(user.getPhone());
		CoinDto coin = coinDao.queryCoinMaxAmount(coinDto);
		Double coinAmount = 0.0;
		if (coin == null || coin.equals("")) {
			coinAmount = 0.0;
		} else {
			/** 最大礼品卡金额. */
			coinAmount = coin.getAmount();
			logger.info("ShopCarServiceImpl.queryCarBuyOk.coinAmount:" + coinAmount);
		}

		/** step8. 获得优惠券 */
		Double couponAmount = 0.0;
		CouPonDto couPonDto = new CouPonDto();
		couPonDto.setUserId(shopCarDto.getUserId());
		CouPonDto couPon = couPonDao.queryCouPonMaxAmount(couPonDto);
		if (couPon == null || couPon.equals("")) {
			couponAmount = 0.0;
		} else {
			couponAmount = couPon.getCouponAmount();
		}

		dataMap.put("info", "您的面值最大礼品卡和优惠券可抵" + coinAmount + couponAmount + "元");
		dataMap.put("userCar", userCars);
		dataMap.put("allAmount", Util.Rounding(amount));
		dataMap.put("count", num);
		dataMap.put("coinAmount", Util.Rounding(coinAmount));
		dataMap.put("maxCoupon", Util.Rounding(couponAmount));
		dataMap.put("message", "[userCar:用户购买的商品购物车集合]; [allAmount:总价]; [commodityPrice:商品单价]; [amount:小计]; ");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 11.服务器端使用 。获得购物车选中购买的商品
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 * 
	 */
	@Override
	public List<ShopCarDto> queryCarBuyOkServ(ShopCarDto shopCarDto) throws Exception {
		/** step1. 空值判断 */
		if (shopCarDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 用户ID 空值判断. */
		if (shopCarDto.getUserId() == null || shopCarDto.getUserId().equals("")) {
			throw new BusinessException("用户ID不能为空");
		}

		/** step3. 判断用户购物车是否为空. */
		if (!uid_ShopCar.containsKey(shopCarDto.getUserId())) {
			throw new BusinessException("您的购物车目前没有商品");
		}

		/** step4. 通过循环获得购买的商品 */
		List<ShopCarDto> userCars = new ArrayList<ShopCarDto>();
		for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarStatus() == 0) {
				userCars.add(uid_ShopCar.get(shopCarDto.getUserId()).get(i));
			}
		}


		return userCars;
	}

	/**
	 * 11.全选按钮
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 * 
	 */
	@Override
	public ResponseDto checkAll(ShopCarDto shopCarDto) throws Exception {
		/** step1. 空值判断 */
		if (shopCarDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 用户ID 空值判断. */
		if (shopCarDto.getUserId() == null || shopCarDto.getUserId().equals("")) {
			throw new BusinessException("用户ID不能为空");
		}

		/** step3. 判断用户购物车是否为空. */
		if (!uid_ShopCar.containsKey(shopCarDto.getUserId())) {
			throw new BusinessException("您的购物车目前没有商品");
		}

		Double allAmount = 0.0;
		/** step4. 如果status是0，代表全选 */
		if (shopCarDto.getCarStatus() == 0) {
			for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {

				/** 当该商品是绿色食品表时 */
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 1) {
					FoodParams foodParams = new FoodParams();
					foodParams.setFoodId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
					FoodParams food = commodityDao.queryFoodById(foodParams);
					allAmount += food.getPrice() * food.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
				}

				/** 当该商品是服装鞋帽时 */
				if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getType() == 2) {
					ClothParams clothParams = new ClothParams();
					clothParams.setClothId(uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCommodityId());
					ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
					allAmount += cloth.getPrice() * cloth.getDiscount() * uid_ShopCar.get(shopCarDto.getUserId()).get(i).getBuyCount();
				}
				uid_ShopCar.get(shopCarDto.getUserId()).get(i).setCarStatus(0);
			}
		}

		/** step5. 如果status是1, 代表全不选 */
		if (shopCarDto.getCarStatus() == 1) {
			for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
				uid_ShopCar.get(shopCarDto.getUserId()).get(i).setCarStatus(1);
			}
		}

		/** step6. 返回结果. */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("设置成功");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("allAmount", Util.Rounding(allAmount));
		dataMap.put("carStatus", shopCarDto.getCarStatus());
		dataMap.put("message", "[allAmount:总价]; [carStatus:接收到的状态码]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 12.删除购物车服务器使用
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @author gc
	 * @date 2015-5-26
	 */
	@Override
	public void deleteCarServ(ShopCarDto shopCarDto) throws Exception {
		logger.info("ShopCarServiceImpl.deleteShopCar:进入添加订单后删除购物车方法");
		/** step1. 空值判断 */
		if (shopCarDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 用户ID和购物车ID 空值判断. */
		if (shopCarDto.getUserId() == null || shopCarDto.getUserId().equals("") || shopCarDto.getCarId() == null || shopCarDto.getCarId().trim().equals("")) {
			throw new BusinessException("用户ID或购物车ID为空");
		}

		/** step3. 删除购物车 */
		int result = 0;

		for (int i = 0; i < uid_ShopCar.get(shopCarDto.getUserId()).size(); i++) {
			if (uid_ShopCar.get(shopCarDto.getUserId()).get(i).getCarId().equals(shopCarDto.getCarId())) {
				logger.info("ShopCarServiceImpl.deleteShopCar:找到需要删除的购物车ID，对集合进行remove");
				uid_ShopCar.get(shopCarDto.getUserId()).remove(i);
				result = 1;
				break;
			}
		}

		if (result == 0) {
			throw new BusinessException("无效购物车ID");
		}
	}

	/**
	 * 13.立即购买按钮操作
	 * 
	 * @param shopCarDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-2
	 */
	@Override
	public ResponseDto atOncePay(ShopCarDto shopCarDto) throws Exception {
		/** step1. 空值判断 */
		if (shopCarDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 用户ID、商品ID,空值判断 */
		if (shopCarDto.getUserId() == null || shopCarDto.getUserId().equals("") || shopCarDto.getCommodityId() == null || shopCarDto.getCommodityId().equals("")) {
			throw new BusinessException("用户ID或者商品ID不能为空");
		}

		/** step3. 购买数量和金额判断 */
		if (shopCarDto.getBuyCount() == null || shopCarDto.getBuyCount().equals("")) {
			throw new BusinessException("商品购买数量不能为空");
		}

		/** step4. 根据Type 和 商品ID——> 得到绿色食品系列 */
		int sku = 0;
		if (shopCarDto.getType() == 1) {
			FoodParams food = new FoodParams();
			FoodParams foodParams = new FoodParams();
			foodParams.setFoodId(shopCarDto.getCommodityId());
			food = commodityDao.queryFoodById(foodParams);
			sku = food.getFoodSku();

			/** 设置购物车商品名称. */
			shopCarDto.setCommodityName(food.getFoodName());

			/** 设置商品厂家名称. */
			shopCarDto.setCommodityFactor(food.getFactoryName());

			/** 设置购物车商品图片地址. */
			shopCarDto.setImageSrc(Enumerate.FOOD_IMG_SRC + food.getIndexSrc());

			/** 设置购物车商品图片名称. */
			shopCarDto.setImageName(food.getIndexSrc());
		}

		/** step5. 根据Type 和 商品ID——> 得到服装系列 */
		if (shopCarDto.getType() == 2) {
			ClothParams cloth = new ClothParams();
			ClothParams clothParams = new ClothParams();
			clothParams.setClothId(shopCarDto.getCommodityId());
			cloth = commodityDao.queryClothByClothId(clothParams);

			// 获得该服装类商品的库存
			SsDto ssDto = new SsDto();
			ssDto.setCommodityId(shopCarDto.getCommodityId());
			ssDto.setImageId(shopCarDto.getColorImgId());
			ssDto.setSize(shopCarDto.getSize());
			SsDto ss = sizeDao.queryByImageIdAndSize(ssDto);
			if (ss != null) {
				sku = ss.getSku();
			}

			/** 设置购物车商品名称. */
			shopCarDto.setCommodityName(cloth.getClothName());

			/** 设置商品厂家名称. */
			shopCarDto.setCommodityFactor(cloth.getFactoryName());

			/** 设置购物车商品图片地址. */
			shopCarDto.setImageSrc(Enumerate.CLOTH_IMG_SRC + cloth.getClothImg());

			/** 设置购物车商品图片名称. */
			shopCarDto.setImageName(cloth.getClothImg());

			/** 查询商品颜色地址. */

			ClothImg clothImg = new ClothImg();
			clothImg.setImageId(shopCarDto.getColorImgId());
			ClothImg cImg = imageDao.queryClothImgByImgId(clothImg);
			shopCarDto.setColorName(cImg.getColorName());
			shopCarDto.setImageSrc(Enumerate.CLOTH_IMG_SRC + cImg.getImageName());
		}

		/** step4. 判断库存 */
		if (!uid_ShopCar.containsKey(shopCarDto.getUserId())) {
			if (shopCarDto.getBuyCount() > sku) {
				throw new BusinessException("库存不足");
			}
		}

		/** step6. 查询数量 */

		Double amount = 0.0;

		/** 如果该件商品是绿色食品 */
		if (shopCarDto.getType() == 1) {
			FoodParams foodParams = new FoodParams();
			foodParams.setFoodId(shopCarDto.getCommodityId());
			FoodParams food = commodityDao.queryFoodById(foodParams);
			shopCarDto.setCommodityPrice(food.getPrice());
			amount = food.getPrice() * food.getDiscount() * shopCarDto.getBuyCount();

		}

		/** 如果该件商品是服装鞋帽 */
		if (shopCarDto.getType() == 2) {
			ClothParams clothParams = new ClothParams();
			clothParams.setClothId(shopCarDto.getCommodityId());
			ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
			shopCarDto.setCommodityPrice(cloth.getPrice());
			amount = cloth.getPrice() * cloth.getDiscount() * shopCarDto.getBuyCount();

		}

		/** step7. 创建结果对象. */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("立即购买对象");
		Map<String, Object> dataMap = new HashMap<String, Object>();

		/** step8. 获得用户礼品卡金额. */

		UserDto userDto = new UserDto();
		userDto.setId(shopCarDto.getUserId());
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
		couponDto.setUserId(shopCarDto.getId());
		CouPonDto coupon = couPonDao.queryCouPonMaxAmount(couponDto);
		if (coupon == null || coupon.equals("")) {
			couponAmount = 0.0;
		} else {
			couponAmount = coupon.getCouponAmount();
		}

		dataMap.put("shopcar", shopCarDto);
		dataMap.put("amount", amount);
		dataMap.put("info", "您的最大优惠券和礼品卡可抵消" + Util.Rounding(couponAmount + coinAmount) + "元");
		responseDto.setDetail(dataMap);
		return responseDto;

	}
}
