package com.jiuyi.vggle.service.coin.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.coin.CoinDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.coin.CoinDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.coin.CoinService;

@Service
public class CoinServiceImpl implements CoinService {
	@Autowired
	private CoinDao coinDao;

	private final static Logger logger = Logger.getLogger(CoinServiceImpl.class);
	/**
	 * 1.添加礼券卡 (管理员操作)
	 *
	 * @author gc
	 * @date 2015-5-14
	 */
	@Override
	public ResponseDto addCoin(CoinDto coinDto) throws Exception {
		/** step1. */
		if (coinDto == null) {
			logger.info("CoinServiceImpl.addCoin  coinDto is null");
			throw new BusinessException(Constants.DATA_ERROR);
		}
		logger.info("CoinServiceImpl.addCoin 'num':'" + coinDto.getNum() + "';" + "'amount':'" + coinDto.getAmount() + "'");

		/** setp2. 循环添加礼品卡. (管理员操作) */
		for (int i = 0; i < coinDto.getNum(); i++) {
			coinDto.setCoinId(Util.getUniqueSn());
			/** 初始化礼品卡状态 0 */
			coinDto.setCoinStatus(10);
			coinDto.setCoinPass(Util.getCoinPass());
			coinDao.addCoinDto(coinDto);
		}
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("添加成功");
		return responseDto;
	}

	/**
	 * 2.修改金额增加(暂时未操作)
	 * 
	 * @author gc
	 * @date 2015-5-14
	 */
	@Override
	public ResponseDto updateCoinAmountup(CoinDto coinDto) throws Exception {
		if (coinDto == null || Util.isNotEmpty(coinDto.getCoinId())) {
			throw new BusinessException(Constants.DATA_ERROR);
		}
		/** 通过手机号获得礼品卡对象 */

		coinDao.updateCoinAmountup(coinDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("增加成功");
		return responseDto;
	}

	/**
	 * 3.用户消费礼品卡
	 * 
	 * @author gc
	 * @date 2015-5-14
	 */
	@Override
	public ResponseDto updateCoinAmountdown(CoinDto coinDto) throws Exception {
		if (coinDto == null || coinDto.getCoinId() == null || coinDto.getCoinId().equals("")) {
			throw new BusinessException(Constants.DATA_ERROR);
		}
		/** 通过礼品卡ID得到礼品卡对象. */
		CoinDto coin = coinDao.queryCoinByCoinId(coinDto);
		if (coin == null) {
			throw new BusinessException("没有找到相关礼品卡");
		}
		if (coin.getAmount() < coinDto.getAmount()) {
			throw new BusinessException("您输入的金额大于该礼品卡余额");
		}
		if (coin.getCoinStatus() == 10) {
			throw new BusinessException("该礼品卡未激活");
		}
		if (coin.getCoinStatus() == 2) {
			throw new BusinessException("该礼品卡已经使用过");
		}
		if (new Date().after(coin.getValidity())) {
			throw new BusinessException("您的礼品卡已经过期了");
		}
		/** 建立一个消息对象 */
		ResponseDto responseDto = new ResponseDto();
		/** 如果金额等于余额 则需要修改礼品卡状态为已使用 '2' */
		if (coin.getAmount() == coinDto.getAmount()) {
			coinDto.setCoinStatus(2);
			/** 修改全部金额和状态 */
			coinDao.updateCoinAmountdown(coinDto);
			responseDto.setResultDesc("成功消费全部礼品卡金额");
		} else {
			/** 修改部分金额,不改变状态 */
			coinDao.updateCoinAmountdownSome(coinDto);
			responseDto.setResultDesc("成功消费部分礼品卡金额");
		}

		return responseDto;
	}

	/**
	 * 4.用户转让礼品卡（全部金额）
	 * 
	 * @param coinDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-14
	 */
	@Override
	public ResponseDto gift(CoinDto coinDto) throws Exception {

		if (coinDto == null || coinDto.getCoinId() == null || coinDto.getCoinId().equals("")) {
			throw new BusinessException(Constants.DATA_ERROR);
		}
		/** 验证手机号格式. */
		if (!Util.isMobile(coinDto.getCoinPhone())) {
			throw new BusinessException("手机号格式不正确");
		}
		/** 通过礼品卡ID得到礼品卡对象. */
		CoinDto coin = coinDao.queryCoinByCoinId(coinDto);
		if (coin == null) {
			throw new BusinessException("没有找到相关礼品卡");
		}
		if (coin.getCoinStatus() == 10) {
			throw new BusinessException("该礼品卡未激活");
		}
		if (coin.getCoinStatus() == 2) {
			throw new BusinessException("该礼品卡已经使用过");
		}
		if (Util.getDateTime().after(coin.getValidity())) {
			throw new BusinessException("您的礼品卡已经过期了");
		}
		/** 调用转让接口. */
		coinDao.updateCoinPhoneById(coinDto);

		/** step2. 查询用户账户 */
		List<CoinDto> coins = coinDao.queryCoinByPhone(coinDto);
		double amount = 0;
		if (coins != null) {
			for (int i = 0; i < coins.size(); i++) {
				amount += coins.get(i).getAmount();
			}
		}
		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("coins", coins);
		dataMap.put("money", Util.Rounding(amount));
		dataMap.put("coin", coin);
		responseDto.setDetail(dataMap);
		responseDto.setResultDesc("成功转让礼品卡");
		return responseDto;
	}

	/**
	 * 5.修改手机号(用户--兑换--礼品卡)
	 * 
	 * @author gc
	 * @date 2015-5-14
	 */
	@Override
	public ResponseDto updateCoinPhone(CoinDto coinDto) throws Exception {
		/** step1. 空判断 */
		if (coinDto == null || coinDto.getCoinPass() == null || coinDto.getCoinPass().equals("") || coinDto.getCoinPhone() == null || coinDto.getCoinPhone().equals("")) {
			throw new BusinessException(Constants.DATA_ERROR);
		}
		/** step2. 判断手机号格式 */
		if (!Util.isMobile(coinDto.getCoinPhone())) {
			throw new BusinessException("手机号格式不正确");
		}
		/** 通过礼品卡密码查询礼品卡状态. */
		CoinDto coin = coinDao.queryCoinByCoinPass(coinDto);
		if (coin == null) {
			throw new BusinessException("礼品卡密码错误");
		}

		if(coin.getCoinStatus()==10){
			throw new BusinessException("您的礼品卡未激活,请先激活在使用");
		}

		logger.info("CoinServiceImpl.updateCoinPhone 'coinStatus':'" + coin.getCoinStatus() + "'");
		if (coin.getCoinStatus() != 0) {
			throw new BusinessException("该礼品卡已经兑换");
		}

		/** 设置礼品卡状态为未使用状态 状态码为'1' */
		coinDto.setCoinStatus(1);

		coinDao.updateCoinPhoneAndStatus(coinDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("兑换成功");
		responseDto.setDetail(coin);
		return responseDto;
	}

	/**
	 * 6.删除
	 * 
	 * @author gc
	 * @date 2015-5-14
	 */
	@Override
	public ResponseDto deleteCoin(CoinDto coinDto) throws Exception {
		if (coinDto == null || coinDto.getCoinId() == null || coinDto.getCoinId().equals("")) {
			throw new BusinessException(Constants.DATA_ERROR);
		}
		/** 通过礼品卡ID得到礼品卡对象. */
		CoinDto coin = coinDao.queryCoinByCoinId(coinDto);
		if (coin == null) {
			throw new BusinessException("没有找到相关礼品卡");
		}
		coinDao.deleteCoin(coinDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("删除成功");
		return responseDto;
	}

	/**
	 * 7.修改手机号和金额 (用户购买礼品卡)暂时未操作 *
	 * 
	 * @author gc
	 * 
	 * @date 2015-5-14
	 */
	@Override
	public ResponseDto updateCoinPhoneAndAmount(CoinDto coinDto) throws Exception {
		if (coinDto == null || coinDto.getCoinId() == null || coinDto.getCoinId().equals("")) {
			throw new BusinessException(Constants.DATA_ERROR);
		}
		coinDto.setCoinStatus(1);
		coinDto.setCreateTime(Util.getDateTime());
		coinDto.setValidity(Util.validityTime());
		coinDao.updateCoinPhoneAndAmount(coinDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("成功获得礼品卡");
		return responseDto;
	}

	/**
	 * 8.通过手机号查询用户礼品卡账户
	 * 
	 * @author gc
	 * 
	 * @date 2015-5-14
	 */
	@Override
	public ResponseDto queryCoinByPhone(CoinDto coinDto) throws Exception {
		/** step1. 空值判断 */
		if (coinDto == null || coinDto.getUserPhone() == null || coinDto.getUserPhone().equals("")) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 查询用户账户 */
		coinDto.setCoinStatus(1);
		List<CoinDto> coins = coinDao.queryCoinByPhoneWalletPage(coinDto);
		double amount = 0;
		if (coins != null) {
			for (int i = 0; i < coins.size(); i++) {
				amount += coins.get(i).getAmount();
			}
		}
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("个人礼品卡账户信息");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data", coins);
		dataMap.put("money", Util.Rounding(amount));
		responseDto.setDetail(dataMap);
		return responseDto;
	}

}
