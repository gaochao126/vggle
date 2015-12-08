package com.jiuyi.vggle.service.admin.commodity.impl;

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
import com.jiuyi.vggle.dao.commodity.CommodityDao;
import com.jiuyi.vggle.dao.commodity.image.ImageDao;
import com.jiuyi.vggle.dao.commodity.size.SizeDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.TokenDto;
import com.jiuyi.vggle.dto.commodity.params.ClothParams;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;
import com.jiuyi.vggle.dto.commodity.sizesku.SsDto;
import com.jiuyi.vggle.dto.user.UserDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.admin.commodity.BackCommodityService;

@Service
public class BackCommodityServiceImpl implements BackCommodityService {

	private final static Logger logger = Logger.getLogger(BackCommodityServiceImpl.class);

	@Autowired
	private CommodityDao commodityDao;

	@Autowired
	private SizeDao sizeDao;

	@Autowired
	private ImageDao imageDao;

	/**
	 * 1.添加服装鞋帽类商品
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-1
	 */
	@Override
	public ResponseDto insertCloth(ClothParams clothParams) throws Exception {
		/** step1: judge null */
		if (clothParams == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(clothParams.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		clothParams.setId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		clothParams.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: judge name pingyin */
		if (!Util.isNotEmpty(clothParams.getClothName()) || !Util.isNotEmpty(clothParams.getClothPinyin())) {
			throw new BusinessException("商品名称或拼音不能为空");
		}

		/** step5: judge type second */
		if(clothParams.getSecondId()==null || clothParams.getSecondId().equals("") || clothParams.getType()==null || clothParams.getType().equals("")){
			throw new BusinessException("商品type  secondId 不能为空");
		}
		
		/** step6： judge season sexKind clothDetail */
		if(!Util.isNotEmpty(clothParams.getSeason())||!Util.isNotEmpty(clothParams.getSexKind())|| !Util.isNotEmpty(clothParams.getClothDetail())){
			throw new BusinessException("商品季节分类或性别分类商品详细描述不能为空");
		}

		/** step7: judge sku discount lables */
		if (!Util.isNotEmpty(clothParams.getLabels()) || clothParams.getDiscount() == null || clothParams.getDiscount().equals("")) {
			throw new BusinessException("商品标签折扣不能为空");
		}

		/** step8: judge price */
		if (clothParams.getPrice() == null || clothParams.getPrice().equals("")) {
			throw new BusinessException("商品价格不能为空");
		}

		/** step9: 判断折扣是否合理 */
		if (clothParams.getDiscount() > 10) {
			throw new BusinessException("您输入的折扣不合理，请输入10折（原价）以内数字");
		}


		/** step10: permission */
		UserDto userDto = new UserDto();
		userDto.setId(clothParams.getId());

		if (!CacheContainer.user_cmd(userDto).contains(clothParams.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step11: insert cloth */
		// 设置初始销量
		clothParams.setClothSales(0);
		// 设置上架时间
		clothParams.setOnvggleTime(Util.getDateTime());
		// 设置默认删除状态
		clothParams.setDeleteStatus(0);
		// 设置公司ID
		clothParams.setCompanyId(token.getUserDto().getCompanyId());

		// 设置商品ID
		if (!Util.isNotEmpty(clothParams.getClothId())) {
			logger.info("BackCommodityServiceImpl.addCloth==商品ID不存在");
			clothParams.setClothId(Util.getUniqueSn());
			Double discount = clothParams.getDiscount() / 10;
			clothParams.setDiscount(discount);
			commodityDao.addCloth(clothParams);
		} else {
			logger.info("BackCommodityServiceImpl.addCloth==商品ID存在");
			Double discount = clothParams.getDiscount() / 10;
			clothParams.setDiscount(discount);
			commodityDao.updateCloth(clothParams);
		}


		/** step12: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("添加成功");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("commodityId", clothParams.getClothId());
		dataMap.put("message", "[commodityId:商品ID]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 2.添加绿色食品类
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @date 2015-7-1
	 * @author gc
	 */
	@Override
	public ResponseDto insertFood(FoodParams foodParams) throws Exception {
		/** step1: judge null */
		if (foodParams == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(foodParams.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		foodParams.setId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		foodParams.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: judge foodName, foodPinyin */
		if (!Util.isNotEmpty(foodParams.getFoodName()) || !Util.isNotEmpty(foodParams.getFoodPinyin())) {
			throw new BusinessException("商品名字或者拼音不能为空");
		}

		/** step5: judge type secondId */
		if (foodParams.getType() == null || foodParams.getType().equals("") || foodParams.getSecondId() == null || foodParams.getSecondId().equals("")) {
			throw new BusinessException("type 或者 secondId 不能为空");
		}

		/** step6: judge foodSales keepDate creatDate */
		if (foodParams.getKeepDate() == null || foodParams.getKeepDate().equals("") || foodParams.getCreatDate() == null || foodParams.getCreatDate().equals("")) {
			throw new BusinessException("保质期和生产日期不能为空");
		}

		/** step7: judge price sku discount */
		if (foodParams.getPrice() == null || foodParams.getPrice().equals("") || foodParams.getFoodSku() == null || foodParams.getFoodSku().equals("") || foodParams.getDiscount() == null || foodParams.getDiscount().equals("")) {
			throw new BusinessException("商品价格或者库存或者折扣不能为空");
		}

		/** step8: 判断折扣是否合法 */
		if (foodParams.getDiscount() > 10) {
			throw new BusinessException("您输入的折扣不合理，请输入10折（原价）以内数字");
		}

		/** step8: permission */
		UserDto userDto = new UserDto();
		userDto.setId(foodParams.getId());

		if (!CacheContainer.user_cmd(userDto).contains(foodParams.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step9: insert food */
		// 设置初始销量
		foodParams.setFoodSales(0);
		// 设置上架时间
		foodParams.setOnvggleTime(Util.getDateTime());
		// 设置删除状态
		foodParams.setDeleteStatus(0);
		// 设置公司ID
		foodParams.setCompanyId(token.getUserDto().getCompanyId());

		// 设置foodID
		if (!Util.isNotEmpty(foodParams.getFoodId())) {
			foodParams.setFoodId(Util.getUniqueSn());
			Double discount = foodParams.getDiscount() / 10;
			foodParams.setDiscount(discount);
			commodityDao.addFood(foodParams);
		} else {
			Double discount = foodParams.getDiscount() / 10;
			foodParams.setDiscount(discount);
			commodityDao.updateFood(foodParams);
		}


		/** step10: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("添加成功");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("commodityId", foodParams.getFoodId());
		dataMap.put("message", "商品ID");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 3.修改服装鞋帽
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @date 2015-7-1
	 * @author gc
	 */
	@Override
	public ResponseDto updateCloth(ClothParams clothParams) throws Exception {

		/** step1: judge null */
		if (clothParams == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(clothParams.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		clothParams.setId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		clothParams.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: judge name pingyin */
		if (!Util.isNotEmpty(clothParams.getClothName()) || !Util.isNotEmpty(clothParams.getClothPinyin())) {
			throw new BusinessException("商品名称或拼音不能为空");
		}

		/** step5: judge type second */
		if (clothParams.getSecondId() == null || clothParams.getSecondId().equals("") || clothParams.getType() == null || clothParams.getType().equals("")) {
			throw new BusinessException("商品type  secondId 不能为空");
		}

		/** step6： judge season sexKind clothDetail */
		if (!Util.isNotEmpty(clothParams.getSeason()) || !Util.isNotEmpty(clothParams.getSexKind()) || !Util.isNotEmpty(clothParams.getClothDetail())) {
			throw new BusinessException("商品季节分类或性别分类商品详细描述不能为空");
		}

		/** step7: judge sku discount lables */
		if (clothParams.getClothSku() == null || clothParams.getClothSku().equals("") || !Util.isNotEmpty(clothParams.getLabels()) || clothParams.getDiscount() == null || clothParams.getDiscount().equals("")) {
			throw new BusinessException("商品库存或折扣或标签不能为空");
		}

		/** step8: permission */
		UserDto userDto = new UserDto();
		userDto.setId(clothParams.getId());

		if (!CacheContainer.user_cmd(userDto).contains(clothParams.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step9: update cloth */
		Double discount = clothParams.getDiscount() / 10;
		clothParams.setDiscount(discount);
		// 设置上架时间
		clothParams.setOnvggleTime(Util.getDateTime());
		// 设置默认删除状态
		clothParams.setDeleteStatus(0);
		// 设置公司ID
		clothParams.setCompanyId(token.getUserDto().getCompanyId());

		commodityDao.updateCloth(clothParams);

		/** step10: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("添加成功");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("commodityId", clothParams.getClothId());
		dataMap.put("message", "商品ID");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 4.修改绿色食品
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @date 2015-7-1
	 * @author gc
	 */
	@Override
	public ResponseDto updateFood(FoodParams foodParams) throws Exception {
		/** step1: judge null */
		if (foodParams == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(foodParams.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		foodParams.setId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		foodParams.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: judge foodName, foodPinyin */
		if (!Util.isNotEmpty(foodParams.getFoodName()) || !Util.isNotEmpty(foodParams.getFoodPinyin())) {
			throw new BusinessException("商品名字或者拼音不能为空");
		}

		/** step5: judge type secondId */
		if (foodParams.getType() == null || foodParams.getType().equals("") || foodParams.getSecondId() == null || foodParams.getSecondId().equals("")) {
			throw new BusinessException("type 或者 secondId 不能为空");
		}

		/** step6: judge foodSales keepDate creatDate */
		if (foodParams.getKeepDate() == null || foodParams.getKeepDate().equals("") || foodParams.getCreatDate() == null || foodParams.getCreatDate().equals("")) {
			throw new BusinessException("保质期和生产日期不能为空");
		}

		/** step7: judge price sku discount */
		if (foodParams.getPrice() == null || foodParams.getPrice().equals("") || foodParams.getFoodSku() == null || foodParams.getFoodSku().equals("") || foodParams.getDiscount() == null || foodParams.getDiscount().equals("")) {
			throw new BusinessException("商品价格或者库存或者折扣不能为空");
		}

		/** step8: permission */
		UserDto userDto = new UserDto();
		userDto.setId(foodParams.getId());

		if (!CacheContainer.user_cmd(userDto).contains(foodParams.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step69: update food */
		Double discount = foodParams.getDiscount() / 10;
		foodParams.setDiscount(discount);
		// 设置上架时间
		foodParams.setOnvggleTime(Util.getDateTime());
		// 设置删除状态
		foodParams.setDeleteStatus(0);
		// 设置公司ID
		foodParams.setCompanyId(token.getUserDto().getCompanyId());

		commodityDao.updateFood(foodParams);

		/** step10: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("修改成功");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("commodityId", foodParams.getFoodId());
		dataMap.put("message", "商品ID");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 5.商家查询属于自己的商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-7
	 */
	@Override
	public ResponseDto queryCommodityByCompanyId(FoodParams foodParams) throws Exception {
		/** step1: judge null */
		if (foodParams == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(foodParams.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		foodParams.setId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		foodParams.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: 判断type. */
		if (foodParams.getType() == null || foodParams.getType().equals("")) {
			throw new BusinessException("type不能为空");
		}

		/** step4: permission */
		UserDto userDto = new UserDto();
		userDto.setId(foodParams.getId());

		if (!CacheContainer.user_cmd(userDto).contains(foodParams.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step5: exe query */
		List<FoodParams> foods = new ArrayList<FoodParams>();
		List<ClothParams> cloth = new ArrayList<ClothParams>();

		/** 查询绿色食品. */
		if (foodParams.getType() == 1) {
			foods = commodityDao.queryFoodByCompanyId(foodParams);
			if (foods != null && foods.size() > 0) {
				for (int i = 0; i < foods.size(); i++) {
					foods.get(i).setIndexSrc(Enumerate.FOOD_IMG_SRC + foods.get(i).getIndexSrc());
				}
			}
		}

		/** 查询服装鞋帽. */
		if (foodParams.getType() == 2) {
			ClothParams clothParams = new ClothParams();
			clothParams.setCompanyId(foodParams.getCompanyId());
			cloth = commodityDao.queryClothByCompanyId(clothParams);
			if (cloth != null && cloth.size() > 0) {
				for (int i = 0; i < cloth.size(); i++) {
					cloth.get(i).setClothImg(Enumerate.CLOTH_IMG_SRC + cloth.get(i).getClothImg());
					// 查询该商品的库存集合
					SsDto ssDto = new SsDto();
					ssDto.setCommodityId(cloth.get(i).getClothId());
					List<SsDto> Ss = sizeDao.querySsByCommodityId(ssDto);
					int sku = 0;
					if (Ss != null) {
						for (int j = 0; j < Ss.size(); j++) {
							sku += Ss.get(j).getSku();
						}
					}
					cloth.get(i).setSku(sku);
				}
			}
		}


		/** step6: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("商品集合");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		if (foodParams.getType() == 1) {
			dataMap.put("food", foods);
			dataMap.put("foodsize", foods.size());
		}
		if (foodParams.getType() == 2) {
			dataMap.put("cloth", cloth);
			dataMap.put("clohtsize", cloth.size());
		}

		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 6.商家查询属于自己的所有商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 * 
	 */
	@Override
	public ResponseDto queryAllCommodityByCompanyId(FoodParams foodParams) throws Exception {
		/** step1: judge null */
		if (foodParams == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(foodParams.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		foodParams.setId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		foodParams.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: permission */
		UserDto userDto = new UserDto();
		userDto.setId(foodParams.getId());

		if (!CacheContainer.user_cmd(userDto).contains(foodParams.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step5: exe query */
		List<FoodParams> foods = new ArrayList<FoodParams>();
		List<ClothParams> cloth = new ArrayList<ClothParams>();

		/** 查询绿色食品. */
		foods = commodityDao.queryFoodByCompanyId(foodParams);
		if (foods != null && foods.size() > 0) {
			for (int i = 0; i < foods.size(); i++) {
				foods.get(i).setIndexSrc(Enumerate.FOOD_IMG_SRC + foods.get(i).getIndexSrc());
			}
		}

		/** 查询服装鞋帽. */
		ClothParams clothParams = new ClothParams();
		clothParams.setCompanyId(foodParams.getCompanyId());
		cloth = commodityDao.queryClothByCompanyId(clothParams);
		if (cloth != null && cloth.size() > 0) {
			for (int i = 0; i < cloth.size(); i++) {
				cloth.get(i).setClothImg(Enumerate.CLOTH_IMG_SRC + cloth.get(i).getClothImg());
				// 查询该商品的库存集合
				SsDto ssDto = new SsDto();
				ssDto.setCommodityId(cloth.get(i).getClothId());
				List<SsDto> Ss = sizeDao.querySsByCommodityId(ssDto);
				int sku = 0;
				if (Ss != null) {
					for (int j = 0; j < Ss.size(); j++) {
						sku += Ss.get(j).getSku();
					}
				}
				cloth.get(i).setSku(sku);
			}
		}

		/** step6: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("商品集合");
		Map<String, Object> dataMap = new HashMap<String, Object>();

		dataMap.put("food", foods);
		dataMap.put("foodsize", foods.size());

		dataMap.put("cloth", cloth);
		dataMap.put("clohtsize", cloth.size());
		dataMap.put("count", foods.size() + cloth.size());
		dataMap.put("message", "[food:绿色食品],[cloth:服装类],[foodsize:食品类商品总件数],[clothsize:服装类商品总件数],[count:所有商品总件数]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 6.商家查询库存为空的商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 * 
	 */
	@Override
	public ResponseDto queryCommodityByCompanyIdAndSkuNull(FoodParams foodParams) throws Exception {
		/** step1: judge null */
		if (foodParams == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(foodParams.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		foodParams.setId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		foodParams.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: permission */
		UserDto userDto = new UserDto();
		userDto.setId(foodParams.getId());

		if (!CacheContainer.user_cmd(userDto).contains(foodParams.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step5: exe query */
		List<FoodParams> foods = new ArrayList<FoodParams>();
		List<ClothParams> cloths = new ArrayList<ClothParams>();

		/** 查询绿色食品. */
		foods = commodityDao.queryFoodByCompanyIdAndSku(foodParams);
		if (foods != null && foods.size() > 0) {
			for (int i = 0; i < foods.size(); i++) {
				foods.get(i).setIndexSrc(Enumerate.FOOD_IMG_SRC + foods.get(i).getIndexSrc());
				foods.get(i).setSku(foods.get(i).getFoodSku());
				foods.get(i).setSales(foods.get(i).getFoodSales());
			}
		}

		/** 查询服装鞋帽. */
		ClothParams clothParams = new ClothParams();
		clothParams.setCompanyId(foodParams.getCompanyId());
		// 查询该商品的库存集合
		List<ClothParams> cloth = commodityDao.queryClothByCompanyId(clothParams);

		for (int i = 0; i < cloth.size(); i++) {
			cloth.get(i).setClothImg(Enumerate.CLOTH_IMG_SRC + cloth.get(i).getClothImg());

			// 查询该商品对象的库存集合

			SsDto ssDto = new SsDto();
			ssDto.setCommodityId(cloth.get(i).getClothId());
			List<SsDto> Ss = sizeDao.querySsByCommodityId(ssDto);
			int sku = 0;
			if (Ss != null) {
				for (int j = 0; j < Ss.size(); j++) {
					sku += Ss.get(j).getSku();
				}
			}
			cloth.get(i).setSku(sku);
			if (sku == 0) {
				cloths.add(cloth.get(i));
			}
		}


		/** step6: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("商品集合");
		Map<String, Object> dataMap = new HashMap<String, Object>();

		dataMap.put("food", foods);
		dataMap.put("foodsize", foods.size());

		dataMap.put("cloth", cloths);
		dataMap.put("clohtsize", cloths.size());
		dataMap.put("count", foods.size() + cloths.size());
		dataMap.put("message", "[food:绿色食品],[cloth:服装类],[foodsize:食品类商品总件数],[clothsize:服装类商品总件数],[count:所有商品总件数]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 7.商家按销量查询商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	@Override
	public ResponseDto queryCommodityByCompanyIdAndSale(FoodParams foodParams) throws Exception {
		/** step1: judge null */
		if (foodParams == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(foodParams.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		foodParams.setId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		foodParams.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: 判断type 1，升序 2，降序 */
		if (foodParams.getType() == null || foodParams.getType().equals("")) {
			throw new BusinessException("升降序标识不能为空");
		}

		/** step5: permission */
		UserDto userDto = new UserDto();
		userDto.setId(foodParams.getId());

		if (!CacheContainer.user_cmd(userDto).contains(foodParams.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step6: exe query */
		List<FoodParams> foods = new ArrayList<FoodParams>();
		List<ClothParams> cloth = new ArrayList<ClothParams>();

		ClothParams clothParams = new ClothParams();
		clothParams.setCompanyId(foodParams.getCompanyId());


		if (foodParams.getType() == 1) {
			foods = commodityDao.queryFoodByCompanyIdAndSaleUp(foodParams);
			cloth = commodityDao.queryClothByCompanyIdAndSaleUp(clothParams);
		}

		if (foodParams.getType() == 2) {
			foods = commodityDao.queryFoodByCompanyIdAndSaleDown(foodParams);
			cloth = commodityDao.queryClothByCompanyIdAndSaleDown(clothParams);
		}

		/** 查询绿色食品. */
		if (foods != null && foods.size() > 0) {
			for (int i = 0; i < foods.size(); i++) {
				foods.get(i).setIndexSrc(Enumerate.FOOD_IMG_SRC + foods.get(i).getIndexSrc());
				foods.get(i).setSku(foods.get(i).getFoodSku());
				foods.get(i).setSales(foods.get(i).getFoodSales());
			}
		}


		/** 查询服装鞋帽. */
		if (cloth != null && cloth.size() > 0) {
			for (int i = 0; i < cloth.size(); i++) {
				cloth.get(i).setClothImg(Enumerate.CLOTH_IMG_SRC + cloth.get(i).getClothImg());
				cloth.get(i).setSales(cloth.get(i).getClothSales());
				// 查询该商品的库存集合
				SsDto ssDto = new SsDto();
				ssDto.setCommodityId(cloth.get(i).getClothId());
				List<SsDto> Ss = sizeDao.querySsByCommodityId(ssDto);
				int sku = 0;
				if (Ss != null) {
					for (int j = 0; j < Ss.size(); j++) {
						sku += Ss.get(j).getSku();
					}
				}
				cloth.get(i).setSku(sku);
			}
		}

		/** step7: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("商品集合");
		Map<String, Object> dataMap = new HashMap<String, Object>();

		dataMap.put("food", foods);
		dataMap.put("foodsize", foods.size());

		dataMap.put("cloth", cloth);
		dataMap.put("clohtsize", cloth.size());
		dataMap.put("count", foods.size() + cloth.size());
		dataMap.put("message", "[food:绿色食品],[cloth:服装类],[foodsize:食品类商品总件数],[clothsize:服装类商品总件数],[count:所有商品总件数]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 8.商家按库存查询商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	@Override
	public ResponseDto queryCommodityByCompanyIdAndSku(FoodParams foodParams) throws Exception {
		/** step1: judge null */
		if (foodParams == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(foodParams.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		foodParams.setId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		foodParams.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: 判断type 1，升序 2，降序 */
		if (foodParams.getType() == null || foodParams.getType().equals("")) {
			throw new BusinessException("升降序标识不能为空");
		}

		/** step5: permission */
		UserDto userDto = new UserDto();
		userDto.setId(foodParams.getId());

		if (!CacheContainer.user_cmd(userDto).contains(foodParams.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step6: exe query */
		List<FoodParams> foods = new ArrayList<FoodParams>();
		List<ClothParams> cloth = new ArrayList<ClothParams>();

		ClothParams clothParams = new ClothParams();
		clothParams.setCompanyId(foodParams.getCompanyId());

		if (foodParams.getType() == 1) {
			foods = commodityDao.queryFoodByCompanyIdAndSkuUp(foodParams);
			cloth = commodityDao.queryClothByCompanyId(clothParams);
		}

		if (foodParams.getType() == 2) {
			foods = commodityDao.queryFoodByCompanyIdAndSkuDown(foodParams);
			cloth = commodityDao.queryClothByCompanyId(clothParams);
		}

		/** 查询绿色食品. */
		if (foods != null && foods.size() > 0) {
			for (int i = 0; i < foods.size(); i++) {
				foods.get(i).setIndexSrc(Enumerate.FOOD_IMG_SRC + foods.get(i).getIndexSrc());
				foods.get(i).setSku(foods.get(i).getFoodSku());
				foods.get(i).setSales(foods.get(i).getFoodSales());
			}
		}

		/** 查询服装鞋帽. */
		if (cloth != null && cloth.size() > 0) {
			for (int i = 0; i < cloth.size(); i++) {
				cloth.get(i).setClothImg(Enumerate.CLOTH_IMG_SRC + cloth.get(i).getClothImg());
				cloth.get(i).setSales(cloth.get(i).getClothSales());
				// 查询该商品的库存集合
				SsDto ssDto = new SsDto();
				ssDto.setCommodityId(cloth.get(i).getClothId());
				List<SsDto> Ss = sizeDao.querySsByCommodityId(ssDto);
				int sku = 0;
				if(Ss != null){
					for (int j = 0; j < Ss.size(); j++) {
						sku += Ss.get(j).getSku();
					}
				}
				cloth.get(i).setSku(sku);
			}
		}

		/** step7: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("商品集合");
		Map<String, Object> dataMap = new HashMap<String, Object>();

		dataMap.put("food", foods);
		dataMap.put("foodsize", foods.size());

		dataMap.put("cloth", cloth);
		dataMap.put("clohtsize", cloth.size());
		dataMap.put("count", foods.size() + cloth.size());
		dataMap.put("message", "[food:绿色食品],[cloth:服装类],[foodsize:食品类商品总件数],[clothsize:服装类商品总件数],[count:所有商品总件数]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 9.商家按上架时间查询商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	@Override
	public ResponseDto queryCommodityByCompanyIdAndOnvggleTime(FoodParams foodParams) throws Exception {
		/** step1: judge null */
		if (foodParams == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断用户ID. */
		TokenDto token = CacheContainer.getToken(foodParams.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		foodParams.setId(token.getUserDto().getId());

		/** step3: 判断公司ID. */
		foodParams.setCompanyId(token.getUserDto().getCompanyId());

		/** step4: 判断type 1，升序 2，降序 */
		if (foodParams.getType() == null || foodParams.getType().equals("")) {
			throw new BusinessException("升降序标识不能为空");
		}

		/** step5: permission */
		UserDto userDto = new UserDto();
		userDto.setId(foodParams.getId());

		if (!CacheContainer.user_cmd(userDto).contains(foodParams.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step6: exe query */
		List<FoodParams> foods = new ArrayList<FoodParams>();
		List<ClothParams> cloth = new ArrayList<ClothParams>();

		ClothParams clothParams = new ClothParams();
		clothParams.setCompanyId(foodParams.getCompanyId());

		if (foodParams.getType() == 1) {
			foods = commodityDao.queryFoodByCompanyIdAndOnvggleTimeUp(foodParams);
			cloth = commodityDao.queryClothByCompanyIdAndOnvggleTimeUp(clothParams);
		}

		if (foodParams.getType() == 2) {
			foods = commodityDao.queryFoodByCompanyIdAndOnvggleTimeDown(foodParams);
			cloth = commodityDao.queryClothByCompanyIdAndOnvggleTimeDown(clothParams);
		}

		/** 查询绿色食品. */
		if (foods != null && foods.size() > 0) {
			for (int i = 0; i < foods.size(); i++) {
				foods.get(i).setIndexSrc(Enumerate.FOOD_IMG_SRC + foods.get(i).getIndexSrc());
				foods.get(i).setSku(foods.get(i).getFoodSku());
				foods.get(i).setSales(foods.get(i).getFoodSales());
			}
		}

		/** 查询服装鞋帽. */
		if (cloth != null && cloth.size() > 0) {
			for (int i = 0; i < cloth.size(); i++) {
				cloth.get(i).setClothImg(Enumerate.CLOTH_IMG_SRC + cloth.get(i).getClothImg());
				// 查询该商品的库存集合
				SsDto ssDto = new SsDto();
				ssDto.setCommodityId(cloth.get(i).getClothId());
				List<SsDto> Ss = sizeDao.querySsByCommodityId(ssDto);
				int sku = 0;
				if (Ss != null) {
					for (int j = 0; j < Ss.size(); j++) {
						sku += Ss.get(j).getSku();
					}
				}
				cloth.get(i).setClothSku(sku);
			}
		}

		/** step7: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("商品集合");
		Map<String, Object> dataMap = new HashMap<String, Object>();

		dataMap.put("food", foods);
		dataMap.put("foodsize", foods.size());

		dataMap.put("cloth", cloth);
		dataMap.put("clohtsize", cloth.size());
		dataMap.put("count", foods.size() + cloth.size());
		dataMap.put("message", "[food:绿色食品],[cloth:服装类],[foodsize:食品类商品总件数],[clothsize:服装类商品总件数],[count:所有商品总件数]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 10.添加商品尺码库存信息
	 * 
	 * @param ssDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-11
	 */
	@Override
	public ResponseDto insertSizeAndSku(SsDto ssDto) throws Exception {
		/** step1: 空值判断. */
		if (ssDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断图片颜色图片ID 商品ID */
		if (!Util.isNotEmpty(ssDto.getCommodityId()) || !Util.isNotEmpty(ssDto.getImageId())) {
			throw new BusinessException("颜色图片ID和商品ID不能为空");
		}

		/** step3: 判断库存和尺码字符串. */
		if (!Util.isNotEmpty(ssDto.getSizeArry()) || !Util.isNotEmpty(ssDto.getSkuArry())) {
			throw new BusinessException("库存和尺码数组不能为空");
		}

		/** step4: 添加库存尺码. */
		JSONArray jsonSize = JSONArray.parseArray(ssDto.getSizeArry());
		JSONArray jsonSku = JSONArray.parseArray(ssDto.getSkuArry());
		for (int i = 0; i < jsonSize.size(); i++) {
			SsDto ss = new SsDto();
			ss.setSsId(Util.getUniqueSn());
			ss.setImageId(ssDto.getImageId());
			ss.setCommodityId(ssDto.getCommodityId());
			ss.setSize(jsonSize.get(i).toString());
			ss.setSku(Integer.parseInt(jsonSku.get(i).toString()));
			System.out.println(ss.getSsId() + "--" + ss.getImageId() + "==" + ss.getCommodityId() + "--" + ss.getSize() + "--" + ss.getSku());
			
			sizeDao.addSizeSku(ss);
		}

		/** step5: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("成功添加尺码库存文本信息");
		return responseDto;
	}
}
