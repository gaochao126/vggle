package com.jiuyi.vggle.service.admin.coin;

import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.coin.CoinDto;

public interface BackCoinServer {

	/**
	 * 1.添加礼品卡
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-19
	 */
	public ResponseDto insertCoin(CoinDto coinDto) throws Exception;

	/**
	 * 2.查询未激活可导出的礼品卡
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto queryCoinByType(CoinDto coinDto) throws Exception;

	/**
	 * 3.通过卡编号激活单张卡
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto ActivaOne(CoinDto coinDto) throws Exception;

	/**
	 * 4.批量激活礼品卡
	 * 
	 * @param coinDto
	 * @return
	 * @throws Exception
	 * @date 2015-8-19
	 * @author gc
	 */
	public ResponseDto ActivaSome(CoinDto coinDto) throws Exception;
}
