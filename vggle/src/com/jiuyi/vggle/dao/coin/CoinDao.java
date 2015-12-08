package com.jiuyi.vggle.dao.coin;

import java.util.List;

import com.jiuyi.vggle.dto.coin.CoinDto;


public interface CoinDao {
	/**
	 * 1.添加礼券卡
	 * 
	 * @param coinDto
	 * @throws Exception
	 */
	public void addCoinDto(CoinDto coinDto) throws Exception;

	/**
	 * 2.修改礼券卡金额（增加）
	 * 
	 * @param coinDto
	 * @throws Exception
	 */
	public void updateCoinAmountup(CoinDto coinDto) throws Exception;

	/**
	 * 3.修改礼券卡金额（全部减少）通过手机号
	 * 
	 * @param coinDto
	 * @throws Exception
	 */
	public void updateCoinAmountdown(CoinDto coinDto) throws Exception;

	/**
	 * 4.修改礼券卡金额（全部减少）通过ID，（礼品卡金额置为0）并且更改礼品卡状态为已使用
	 * 
	 * @param coinDto
	 * @throws Exception
	 */
	public void updateCoinAmountdownByCoinId(CoinDto coinDto) throws Exception;

	/**
	 * 5.修改礼券卡金额（部分减少）通过ID
	 * 
	 * @param coinDto
	 * @throws Exception
	 */
	public void updateCoinAmountdownSome(CoinDto coinDto) throws Exception;

	/**
	 * 6.修改礼券卡手机号(用户兑换礼品卡)
	 * 
	 * @param coinDto
	 * @throws Exception
	 */
	public void updateCoinPhoneAndStatus(CoinDto coinDto) throws Exception;

	/**
	 * 7.通过ID修改礼券卡手机号(转让礼品卡)
	 * 
	 * @param coinDto
	 * @throws Exception
	 */
	public void updateCoinPhoneById(CoinDto coinDto) throws Exception;

	/**
	 * 8.删除礼券卡
	 * 
	 * @param coinDto
	 * @throws Exception
	 */
	public void deleteCoin(CoinDto coinDto) throws Exception;

	/**
	 * 9.修改礼券卡手机号和金额
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-14
	 */
	public void updateCoinPhoneAndAmount(CoinDto coinDto) throws Exception;

	/**
	 * 10.通过密码查询礼品卡对象
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-14
	 */
	public CoinDto queryCoinByCoinPass(CoinDto coinDto) throws Exception;

	/**
	 * 11.通过ID查询礼品卡对象
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-22
	 */
	public CoinDto queryCoinByCoinId(CoinDto coinDto) throws Exception;

	/**
	 * 12.通过ID修改礼品卡状态
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-23
	 */
	public CoinDto updateCoinStatusById(CoinDto coinDto) throws Exception;

	/**
	 * 13.通过手机号查询用户礼品卡
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-23
	 */
	public List<CoinDto> queryCoinByPhone(CoinDto coinDto) throws Exception;

	/**
	 * 14.通过手机号查询用户礼品卡钱包页面
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-23
	 */
	public List<CoinDto> queryCoinByPhoneWalletPage(CoinDto coinDto) throws Exception;

	/**
	 * 15.查询用户面值最大的礼品卡
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-23
	 */
	public CoinDto queryCoinMaxAmount(CoinDto coinDto) throws Exception;

	/**
	 * 16.根据type——内用外用方式 _礼品卡状态——查询礼品卡
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 */
	public List<CoinDto> queryCoinByTypeAndSourceAndCoinStatus(CoinDto coinDto) throws Exception;

	/**
	 * 17.激活单张礼品卡，并添加建卡日期，有效期
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 * @date 2015-8-19
	 * @author gc
	 */
	public int updataCoinStatusAndCreateAndValidity(CoinDto coinDto) throws Exception;

	/**
	 * 18.通过礼品卡编号查询礼品卡
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 */
	public CoinDto queryCoinByNo(CoinDto coinDto) throws Exception;

	/**
	 * 19.修改礼品卡，是1.实体卡，0.虚拟卡状态
	 * 
	 * @param coinDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-20
	 */
	public void updateCoinSource(CoinDto coinDto) throws Exception;
}
