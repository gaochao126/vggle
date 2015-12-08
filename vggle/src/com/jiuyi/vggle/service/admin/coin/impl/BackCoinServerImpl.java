package com.jiuyi.vggle.service.admin.coin.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.vggle.common.dict.CacheContainer;
import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.coin.CoinDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.TokenDto;
import com.jiuyi.vggle.dto.coin.CoinDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.admin.coin.BackCoinServer;

@Service
public class BackCoinServerImpl implements BackCoinServer {
	// private final static Logger logger =
	// Logger.getLogger(BackCoinServerImpl.class);

	@Autowired
	private CoinDao coinDao;
	/**
	 * 1.添加礼品卡
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-19
	 */
	@Override
	public ResponseDto insertCoin(CoinDto coinDto) throws Exception {
		/** step1. */
		if (coinDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断生成数量，生成类型，生成金额 */
		if (coinDto.getNum() == null || coinDto.getNum().equals("") || coinDto.getType() == null || coinDto.getType().equals("")) {
			throw new BusinessException("礼品卡数量和类型不能为空");
		}

		/** step3. 判断生成金额 */
		if (coinDto.getAmount() == null || coinDto.getAmount().equals("")) {
			throw new BusinessException("生成金额不能为空");
		}

		/** step4: 根据token获取用户对象 */
		TokenDto token = CacheContainer.getToken(coinDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		/** step5: 判断权限 */

		if (!CacheContainer.user_cmd(token.getUserDto()).contains(coinDto.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}


		/** setp6. 循环添加礼品卡. (管理员操作) */
		for (int i = 0; i < coinDto.getNum(); i++) {

			/** 设置礼品卡卡号 */
			coinDto.setCoinId(Util.getUniqueSn());
			/** 初始化礼品卡状态 10,未激活 */
			coinDto.setCoinStatus(10);
			/** 设置礼品卡，为虚拟卡. */
			coinDto.setSource(0);
			/** 设置礼品卡密码. */
			coinDto.setCoinPass(Util.getCoinPass());
			/** 执行添加. */
			coinDao.addCoinDto(coinDto);
		}
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("添加成功");
		return responseDto;
	}

	/**
	 * 2.按礼品卡内外用状态 查询——————未激活可导出的礼品卡
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto queryCoinByType(CoinDto coinDto) throws Exception {
		/** step1: 空值判断 */
		if (coinDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断礼品卡type0.内部卡 ——1.外部卡 */
		if (coinDto.getType() == null || coinDto.getType().equals("")) {
			throw new BusinessException("礼品卡type不能为空");
		}

		/** step3: 根据token获取用户对象 */
		TokenDto Token = CacheContainer.getToken(coinDto.getToken());
		if (Token == null) {
			throw new BusinessException("未登录");
		}

		/** step4: 判断权限 */

		if (!CacheContainer.user_cmd(Token.getUserDto()).contains(coinDto.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step5: 执行查询 */
		//设置礼品卡状态为未激活状态
		coinDto.setCoinStatus(10);
		//设置礼品卡状态为虚拟卡
		coinDto.setSource(0);
		
		List<CoinDto> coins = coinDao.queryCoinByTypeAndSourceAndCoinStatus(coinDto);

		/** step6: 返回结果 */
		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("coins", coins);
		responseDto.setDetail(dataMap);
		responseDto.setResultDesc("可导出礼品卡集合");

		return responseDto;
	}


	/**
	 * 3.通过卡编号激活单张卡
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto ActivaOne(CoinDto coinDto) throws Exception {
		/** step1: 空值判断 */
		if (coinDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断礼品卡no */
		if (coinDto.getNo() == null || coinDto.getNo().equals("")) {
			throw new BusinessException("礼品卡no不能为空");
		}

		/** step3: 根据token获取用户对象 */
		TokenDto token = CacheContainer.getToken(coinDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}

		/** step4: 判断权限 */
		if (!CacheContainer.user_cmd(token.getUserDto()).contains(coinDto.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		
		/** step5: 查询该礼品卡 */
		CoinDto no = coinDao.queryCoinByNo(coinDto);
		if(no ==null){
			throw new BusinessException("礼品卡不存在");
		}
		
		if(no.getCoinStatus()!=10){
			throw new BusinessException("该礼品卡已经被激活");
		}
		
		/** step6: 执行修改（激活） */
		
		// 设置礼品卡起始日期
		coinDto.setCreateTime(Util.getDateTime());
		
		// 设置礼品卡有效期
		coinDto.setValidity(Util.validityTime());
		
		// 设置礼品卡状态
		coinDto.setCoinStatus(0);

		coinDao.updataCoinStatusAndCreateAndValidity(coinDto);
		
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("激活成功");
		return responseDto;
	}

	/**
	 * 4.批量激活礼品卡
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 * @date 2015-8-19
	 * @author gc
	 */
	@Override
	public ResponseDto ActivaSome(CoinDto coinDto) throws Exception {
		/** step1: 空值判断 */
		if (coinDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: 判断起始终止数值 */
		if (coinDto.getBegin() == null || coinDto.getBegin().equals("") || coinDto.getEnd() == null || coinDto.getEnd().equals("")) {
			throw new BusinessException("起始终止编号不能为空");
		}

		
		/** step3: 根据token获取用户对象 */
		TokenDto token = CacheContainer.getToken(coinDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}

		/** step4: 判断权限 */

		if (!CacheContainer.user_cmd(token.getUserDto()).contains(coinDto.getCmd())) {
			throw new BusinessException("您没有该操作权限");
		}

		/** step5:循环激活， */
		// 设置创建日期
		coinDto.setCreateTime(Util.getDateTime());
		// 设置有效期
		coinDto.setValidity(Util.validityTime());

		for (int i = coinDto.getBegin(); i <= coinDto.getEnd(); i++) {
			CoinDto no = new CoinDto();
			no.setNo(i);
			CoinDto coin = coinDao.queryCoinByNo(no);
			if (coin != null && coin.getCoinStatus() == 10) {
				coinDto.setNo(i);
				coinDto.setCoinStatus(0);
				coinDao.updataCoinStatusAndCreateAndValidity(coinDto);
			}
		}

		/** step6:返回结果 */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("激活成功");
		return responseDto;
	}
}
