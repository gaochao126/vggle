package com.jiuyi.vggle.dao.pay;

import java.util.List;

import com.jiuyi.vggle.dto.pay.BankDto;

/**
 * @description 实名认证dao层接口
 * @author gc
 * @createTime 2015年5月27日
 */
public interface BankDao {
    /**
	 * 1.查询快捷支付银行列表
	 * 
	 * @description 查询银行列表
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public List<BankDto> queryQuickBanks() throws Exception;

	/**
	 * 2.查询网银支付银行列表
	 * 
	 * @description 查询银行列表
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public List<BankDto> queryOnlineBanks() throws Exception;

	/**
	 * 3.通过银行号获得银行图标
	 * 
	 * @description 查询银行列表
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public BankDto queryOnlineBanksByBankNo(BankDto bankDto) throws Exception;
}