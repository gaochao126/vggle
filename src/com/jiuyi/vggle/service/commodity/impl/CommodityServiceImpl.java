package com.jiuyi.vggle.service.commodity.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.util.Enumerate;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.commodity.CommodityDao;
import com.jiuyi.vggle.dao.commodity.image.ImageDao;
import com.jiuyi.vggle.dao.commodity.size.SizeDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.commodity.image.ClothImg;
import com.jiuyi.vggle.dto.commodity.image.FoodImg;
import com.jiuyi.vggle.dto.commodity.params.ClothParams;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;
import com.jiuyi.vggle.dto.commodity.size.SizeDto;
import com.jiuyi.vggle.dto.commodity.sizesku.SsDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.commodity.CommodityService;

@Service
public class CommodityServiceImpl implements CommodityService {
	@Autowired
	private CommodityDao commodityDao;

	@Autowired
	private ImageDao imageDao;

	@Autowired
	private SizeDao sizeDao;

	private final static Logger logger = Logger.getLogger(CommodityServiceImpl.class);

	/**
	 * 1.通过主分类查询商品
	 * 
	 * @author gc
	 * @date 2015-5-25
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto queryFoodParamsAll(FoodParams foodParams) throws Exception {
		/** step1. 空值判断 */
		if (foodParams == null) {
			logger.info("CommodityServiceImpl.queryFoodParamsAll  coinDto is null");
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 校验必输字段 */
		if (foodParams.getType() == null || foodParams.getType().equals("")) {
			throw new BusinessException("商品type不能为空");
		}

		/** step2. 根据type判断查询那张表 */
		List<FoodParams> foods = new ArrayList<FoodParams>();
		List<ClothParams> cloths = new ArrayList<ClothParams>();

		if (foodParams.getType() == 1) {
			foods = commodityDao.queryFoodParamsAll(foodParams);
		}

		if (foodParams.getType() == 2) {
			ClothParams clothParams = new ClothParams();
			clothParams.setType(foodParams.getType());
			cloths = commodityDao.queryAllClothParams(clothParams);
		}

		/** step3. 更新绿色食品图片地址链接. */
		if (foods != null) {
			for (int i = 0; i < foods.size(); i++) {
				foods.get(i).setIndexSrc(Enumerate.FOOD_IMG_SRC + foods.get(i).getIndexSrc());
			}
		}

		/** step4. 更改服装鞋帽图片地址链接. */
		if (cloths != null) {
			for (int i = 0; i < cloths.size(); i++) {
				cloths.get(i).setClothImg(Enumerate.CLOTH_IMG_SRC + cloths.get(i).getClothImg());
			}
		}

		/** step5. 返回结果. */
		ResponseDto responseDto = new ResponseDto();
		
		if (foodParams.getType() == 1) {
			responseDto.setDetail(foods);
		}
		if (foodParams.getType() == 2) {
			responseDto.setDetail(cloths);
		}
		responseDto.setResultDesc("[secondList:主级分类商品集合]");
		responseDto.setResultDesc("查询成功");
		return responseDto;
	}

	/**
	 * 2.通过主分类和二级分类查询商品 eg: 10,五谷杂粮; 11,油盐酱醋; 12,干货; 13,水果蔬菜.
	 * 
	 * @author gc
	 * @date 2015-5-25
	 * @return
	 * @throws Exception
	 * @params type secondId
	 */
	@Override
	public ResponseDto queryCommodBySecondId(FoodParams foodParams) throws Exception {
		/** step1. 空值判断 */
		if (foodParams == null) {
			logger.info("CommodityServiceImpl.queryFoodParamsAll  coinDto is null");
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断二级ID. */
		if (foodParams.getSecondId() == null || foodParams.getSecondId().equals("")) {
			throw new BusinessException("二级分类ID不能为空");
		}

		/** step3. 查询二级分类产品 */
		List<FoodParams> foods = new ArrayList<FoodParams>();
		List<ClothParams> cloths = new ArrayList<ClothParams>();

		if (foodParams.getType() == 1) {
			foods = commodityDao.queryCommodByFoodKind(foodParams);
		}

		if (foodParams.getType() == 2) {
			ClothParams clothParams = new ClothParams();
			clothParams.setSecondId(foodParams.getSecondId());
			cloths = commodityDao.queryClothBySecondId(clothParams);
		}

		/** step4. 更改图片链接 */
		if (foods != null) {
			for (int i = 0; i < foods.size(); i++) {
				foods.get(i).setIndexSrc(Enumerate.FOOD_IMG_SRC + foods.get(i).getIndexSrc());
			}
		}

		if (cloths != null) {
			for (int i = 0; i < cloths.size(); i++) {
				cloths.get(i).setClothImg(Enumerate.CLOTH_IMG_SRC + cloths.get(i).getClothImg());
			}
		}

		/** step5. 返回结果. */
		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		if (foodParams.getType() == 1) {
			dataMap.put("secondList", foods);
		}
		if (foodParams.getType() == 2) {
			dataMap.put("secondList", cloths);
		}
		responseDto.setDetail(dataMap);
		responseDto.setResultDesc("二级分类查询成功");
		return responseDto;
	}

	/**
	 * 3.通过商品名字、
	 * 
	 * @author gc
	 * @date 2015-5-25
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto queryCommodByCommodName(FoodParams foodParams) throws Exception {
		/** step1. 空值判断 */
		if (foodParams == null) {
			logger.info("CommodityServiceImpl.queryCommodByCommodName  coinDto is null");
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断关键字是否为空. */
		if (foodParams.getFoodName().trim() == null || foodParams.getFoodName().trim().equals("")) {
			throw new BusinessException("没有输入关键字哦");
		}

		/** step3. 通过名字或者拼音查询绿色食品表. */
		List<FoodParams> foods = commodityDao.queryCommodByCommodName(foodParams);
		if (foods != null) {
			for (int i = 0; i < foods.size(); i++) {
				foods.get(i).setIndexSrc(Enumerate.FOOD_IMG_SRC + foods.get(i).getIndexSrc());
			}
		}
		
		/** step4. 查询服装鞋帽表. */
		ClothParams clothParams = new ClothParams();
		clothParams.setClothName(foodParams.getFoodName());
		List<ClothParams> cloths = commodityDao.queryClothByKeyWord(clothParams);

		if(cloths !=null){
			for (int i = 0; i < cloths.size(); i++) {
				cloths.get(i).setClothImg(Enumerate.CLOTH_IMG_SRC + cloths.get(i).getClothImg());
			}
		}

		Map<String, Object> dataMap = new HashMap<String, Object>();
		if (foods != null) {
			dataMap.put("foods", foods);
		}
		if (cloths != null) {
			dataMap.put("cloths", cloths);
		}
		ResponseDto responseDto = new ResponseDto();
		responseDto.setDetail(dataMap);
		responseDto.setResultDesc("查询成功");
		return responseDto;
	}

	/**
	 * 4.获得首页商品
	 * 
	 * @author gc
	 * @date 2015-5-25
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto queryCommodIndex(FoodParams foodParams) throws Exception {
		/** step1. 获得粮油干货产品 */
		List<FoodParams> foods = commodityDao.queryFoodParamsAll(foodParams);
		if (foods != null) {
			for (int i = 0; i < foods.size(); i++) {
				foods.get(i).setIndexSrc(Enumerate.FOOD_IMG_SRC + foods.get(i).getIndexSrc());
			}
		}

		/** step2. 获得所有服装鞋帽 */
		ClothParams clothParams = new ClothParams();
		List<ClothParams> cloths = commodityDao.queryAllClothParams(clothParams);
		if (cloths != null) {
			for (int i = 0; i < cloths.size(); i++) {
				cloths.get(i).setClothImg(Enumerate.CLOTH_IMG_SRC + cloths.get(i).getClothImg());
			}
		}

		/** step3. 获得精品推荐 */
		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("food", foods);
		dataMap.put("cloth", cloths);
		responseDto.setDetail(dataMap);
		responseDto.setResultDesc("首页商品");
		return responseDto;
	}

	/**
	 * 5.获得商品详情
	 * 
	 * @author gc
	 * @date 2015-5-25
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto queryCommodDetail(FoodParams foodParams) throws Exception {
		/** step1. 空值判断 */
		if (foodParams == null || foodParams.getType() == null || foodParams.getType().equals("")) {
			logger.info("CommodityServiceImpl.queryCommodDetail  coinDto is null");
			throw new BusinessException(Constants.DATA_ERROR);
		}

		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		/** step2. 查询粮油干货表. */
	

		if (foodParams.getType() == 1) {
			FoodParams food = new FoodParams();
			food = commodityDao.queryFoodById(foodParams);
			food.setIndexSrc(Enumerate.FOOD_IMG_SRC + food.getIndexSrc());

			/** step2-1. 获得商品图片信息. */
			List<FoodImg> imgs = imageDao.queryAllImageById(foodParams);
			if (imgs != null) {
				for (int i = 0; i < imgs.size(); i++) {
					imgs.get(i).setImageUrl(Enumerate.FOOD_IMG_SRC + imgs.get(i).getImageUrl());
				}
			}
			food.setFoodImg(imgs);

			dataMap.put("info", food);
		}

		/** step3. 获得服装鞋帽 */
		ClothParams cloth = new ClothParams();
		if (foodParams.getType() == 2) {
			ClothParams clothParams = new ClothParams();
			clothParams.setClothId(foodParams.getFoodId());
			cloth = commodityDao.queryClothByClothId(clothParams);
			cloth.setClothImg(Enumerate.CLOTH_IMG_SRC + cloth.getClothImg());

			/** 获得商品图片信息. */
			List<ClothImg> clothImgs = imageDao.queryAllImageByClothId(clothParams);
			if (clothImgs != null) {
				for (int i = 0; i < clothImgs.size(); i++) {
					clothImgs.get(i).setImageName(Enumerate.CLOTH_IMG_SRC + clothImgs.get(i).getImageName());
				}
			}

			/** 获得商品大小集合. */
			SizeDto sizeDto = new SizeDto();
			sizeDto.setClothId(foodParams.getFoodId());
			List<SizeDto> size = sizeDao.querySizeByClohtId(sizeDto);

			// 新增
			SsDto ssDto = new SsDto();
			ssDto.setCommodityId(clothParams.getClothId());
			List<SsDto> ss = sizeDao.querySsByCommodityId(ssDto);
			List<String> list = new ArrayList<String>();
			// 初始化服装类商品库存
			cloth.setSku(0);
			if (ss != null) {
				for (int j = 0; j < ss.size(); j++) {
					list.add(ss.get(j).getSize());
					System.out.println(ss.get(j).getSku() + "" + ss.get(j).getImageId());
					cloth.setSku(cloth.getSku() + ss.get(j).getSku());// 累加该商品不同图片的库存，得到总库存
				}

				// 判断该商品是不是衣服或裤子
				if (cloth.getSecondId() == 201 || cloth.getSecondId() == 202) {
					Util.ListToSet(list, 0);
				} else {
					Util.ListToSet(list, 1);
				}
			}
			cloth.setClothImgs(clothImgs);
			cloth.setSizeArry(list);
			cloth.setSize(size);// 次数据不要，临时测试

			dataMap.put("info", cloth);
		}
		responseDto.setDetail(dataMap);
		responseDto.setResultDesc("商品详情");
		return responseDto;
	}

	/**
	 * 6.获得精品推荐
	 * 
	 * @author gc
	 * @date 2015-6-17
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto commodityShow(FoodParams foodParams) throws Exception {
		/** step1. 获得粮油干货产品 */
		List<FoodParams> foods = commodityDao.queryShowFood(foodParams);
		if (foods != null) {
			for (int i = 0; i < foods.size(); i++) {
				foods.get(i).setIndexSrc(Enumerate.FOOD_IMG_SRC + foods.get(i).getIndexSrc());
			}
		}

		/** step2. 获得所有服装鞋帽 */
		ClothParams clothParams = new ClothParams();
		List<ClothParams> cloths = commodityDao.queryShowCloth(clothParams);
		if (cloths != null) {
			for (int i = 0; i < cloths.size(); i++) {
				cloths.get(i).setClothImg(Enumerate.CLOTH_IMG_SRC + cloths.get(i).getClothImg());
			}
		}
		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("food", foods);
		dataMap.put("cloth", cloths);
		responseDto.setDetail(dataMap);
		responseDto.setResultDesc("首页精品推荐商品");
		return responseDto;
	}

	/**
	 * 7.通过颜色图片ID和尺码得到库存
	 * 
	 * @param ssDto
	 * @return
	 * @throws Exception
	 * @date 2015-8-13
	 * @author gc
	 */
	@Override
	public ResponseDto querySkuByImageIdAndSize(SsDto ssDto) throws Exception {
		/** step1: judge null */
		if (ssDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: judge imageId and size */
		if (!Util.isNotEmpty(ssDto.getImageId()) || !Util.isNotEmpty(ssDto.getSize())) {
			throw new BusinessException("颜色图片ID和尺码不能为空");
		}

		/** step3: exe query */
		SsDto ss = sizeDao.queryByImageIdAndSize(ssDto);

		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("sku", ss);
		responseDto.setDetail(dataMap);
		responseDto.setResultDesc("该颜色尺码对应库存对象");
		return responseDto;
	}
}
