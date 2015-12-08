package com.jiuyi.vggle.service.discuss.impl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.util.Enumerate;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.commodity.CommodityDao;
import com.jiuyi.vggle.dao.discuss.DiscussDao;
import com.jiuyi.vggle.dao.user.UserDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.Discuss.DiscussDto;
import com.jiuyi.vggle.dto.commodity.params.ClothParams;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;
import com.jiuyi.vggle.dto.user.UserDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.discuss.DiscussService;

@Service
public class DiscussServiceImpl implements DiscussService {

	@Autowired
	private DiscussDao discussDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private CommodityDao commodityDao;

	/**
	 * 1.添加评论
	 * 
	 * @author gc
	 * @date 2015-5-28
	 * @param discussDto
	 * @throws Exception
	 */
	@Override
	public ResponseDto addDiscuss(DiscussDto discussDto) throws Exception {
		/** step1. 空值判断. */
		if (discussDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断必输字段不能为空,用户ID和商品ID. */
		if (discussDto.getUserId() == null || discussDto.getUserId().equals("") || discussDto.getCommodityId() == null || discussDto.getCommodityId().equals("")) {
			throw new BusinessException("用户ID或者商品ID不能为空");
		}

		/** step3. 判断评论内容和商品评分不为空. */
		if (discussDto.getDisMess() == null || discussDto.getDisMess().equals("") || discussDto.getCommodScore() == null || discussDto.getCommodScore().equals("")) {
			throw new BusinessException("评论内容和商品评分不能为空");
		}

		/** step4. 服务态度评分和物流评分不能为空. */
		if (discussDto.getServiceScore() == null || discussDto.getServiceScore().equals("") || discussDto.getTransScore() == null || discussDto.getTransScore().equals("")) {
			throw new BusinessException("服务态度评分和商品评分不能为空");
		}

		/** step5. 商品type不能为空. */
		if (discussDto.getType() == null || discussDto.getType().equals("")) {
			throw new BusinessException("商品type类型不能为空");
		}

		/** step6. 通过用户ID查询用户手机号. */
		UserDto userDto = new UserDto();
		userDto.setId(discussDto.getUserId());
		UserDto user = userDao.queryUserById(userDto);

		/** step7. 设置评论的电话号码或姓名. */
		String disName = "";
		System.out.println(user.getNickname() + "---");
		if (user.getNickname() != null && !"".equals(user.getNickname())) {
			disName = user.getNickname().substring(0, 1) + "**";
		} else {
			disName = user.getPhone().substring(0, 3) + "****" + user.getPhone().substring(3, 7);
		}

		/** step8. 添加评论. */
		discussDto.setDisId(Util.getUniqueSn());
		discussDto.setDisTime(Util.getDateTime());
		discussDto.setUserName(disName);
		discussDto.setDisStatus(0);
		discussDao.addDiscuss(discussDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("添加成功");
		return responseDto;
	}

	/**
	 * 2.查询商品评论
	 * 
	 * @author gc
	 * @date 2015-5-28
	 * @param discussDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto queryDiscussByCommodityId(DiscussDto discussDto) throws Exception {
		/** step1. 空值判断. */
		if (discussDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断必输字段不能为空,商品ID. */
		if (discussDto.getCommodityId() == null || discussDto.getCommodityId().equals("")) {
			throw new BusinessException("商品ID不能为空");
		}

		/** step3. 查询商品评论. */
		List<DiscussDto> discuss = discussDao.queryDiscussByCommodityId(discussDto);

		/** step4. 计算评分. */
		Double commodscore = 0.0;
		Double servicescore = 0.0;
		Double transscore = 0.0;

		for (int i = 0; i < discuss.size(); i++) {
			commodscore += discuss.get(i).getCommodScore();
			servicescore += discuss.get(i).getServiceScore();
			transscore += discuss.get(i).getTransScore();
		}

		DecimalFormat df = new DecimalFormat(("#.00"));

		if (discuss != null || discuss.size() > 0) {
			commodscore = commodscore / discuss.size();
			servicescore = servicescore / discuss.size();
			transscore = transscore / discuss.size();
		}
		/** step5.返回结果. */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("查询成功");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("discuss", discuss);
		dataMap.put("commodscore", df.format(commodscore));
		dataMap.put("servicescore", df.format(servicescore));
		dataMap.put("transscore", df.format(transscore));
		dataMap.put("count", discuss.size());
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 3.查询用户评论
	 * 
	 * @author gc
	 * @date 2015-5-28
	 * @param discussDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto queryDiscussByUserId(DiscussDto discussDto) throws Exception {
		/** step1. 空值判断. */
		if (discussDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断必输字段不能为空,商品ID. */
		if (discussDto.getUserId() == null || discussDto.getUserId().equals("")) {
			throw new BusinessException("用户ID不能为空");
		}

		/** step3. 查询商品评论. */
		List<DiscussDto> discuss = discussDao.queryDiscussByUserId(discussDto);
		for (int i = 0; i < discuss.size(); i++) {
			if (discuss.get(i).getType() == 1) {
				FoodParams foodParams = new FoodParams();
				foodParams.setFoodId(discuss.get(i).getCommodityId());
				FoodParams food = commodityDao.queryFoodById(foodParams);
				discuss.get(i).setImgSrc(Enumerate.FOOD_IMG_SRC + food.getIndexSrc());
				discuss.get(i).setCommodName(food.getFoodName());
			}

			if (discuss.get(i).getType() == 2) {
				ClothParams clothParams = new ClothParams();
				clothParams.setClothId(discuss.get(i).getCommodityId());
				ClothParams cloth = commodityDao.queryClothByClothId(clothParams);
				discuss.get(i).setImgSrc(Enumerate.CLOTH_IMG_SRC + cloth.getClothImg());
				discuss.get(i).setCommodName(cloth.getClothName());
			}
		}

		/** step4.返回结果. */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("查询成功");
		responseDto.setDetail(discuss);
		return responseDto;
	}

	/**
	 * 4.设置评论状态
	 * 
	 * @author gc
	 * @date 2015-5-28
	 * @param discussDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto updateDiscussStatus(DiscussDto discussDto) throws Exception {
		/** step1. 空值判断. */
		if (discussDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断必输字段不能为空,评论ID. */
		if (discussDto.getDisId() == null || discussDto.getDisId().equals("")) {
			throw new BusinessException("评论ID不能为空");
		}

		/** step3. 设置评论状态. */
		discussDto.setDisStatus(1);
		discussDao.updateDiscussStatus(discussDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("修改成功");
		return responseDto;
	}

}
