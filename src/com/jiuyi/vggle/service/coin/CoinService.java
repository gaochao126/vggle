package com.jiuyi.vggle.service.coin;

import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.coin.CoinDto;

public interface CoinService {
	/**
	 * 1.添加礼券卡
	 * 
	 * @param coinDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-14
	 */
	public ResponseDto addCoin(CoinDto coinDto) throws Exception;

	/**
	 * 2.修改礼券卡金额（增加）
	 * 
	 * @param coinDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-14
	 */
	public ResponseDto updateCoinAmountup(CoinDto coinDto) throws Exception;

	/**
	 * 3.用户消费礼品卡（减少）
	 * 
	 * @param coinDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-14
	 */
	public ResponseDto updateCoinAmountdown(CoinDto coinDto) throws Exception;

	/**
	 * 4.用户转让礼品卡（全部金额）
	 * 
	 * @param coinDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-14
	 */
	public ResponseDto gift(CoinDto coinDto) throws Exception;

	/**
	 * 5.修改礼券卡手机号
	 * 
	 * @param coinDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-14
	 */
	public ResponseDto updateCoinPhone(CoinDto coinDto) throws Exception;

	/**
	 * 6.删除礼券卡
	 * 
	 * @param coinDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-14
	 */
	public ResponseDto deleteCoin(CoinDto coinDto) throws Exception;

	/**
	 * 7.修改礼券卡手机号和金额
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-14
	 */
	public ResponseDto updateCoinPhoneAndAmount(CoinDto coinDto) throws Exception;

	/**
	 * 8.通过手机号查询用户礼品卡账户
	 * 
	 * @author gc
	 * 
	 * @date 2015-5-14
	 */
	public ResponseDto queryCoinByPhone(CoinDto coinDto) throws Exception;
}
