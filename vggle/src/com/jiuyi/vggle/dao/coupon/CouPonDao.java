package com.jiuyi.vggle.dao.coupon;

import java.util.List;

import com.jiuyi.vggle.dto.coupon.CouPonDto;

public interface CouPonDao {

	/**
	 * 1.添加优惠券
	 * 
	 * @param couPonDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-4
	 */
	public void addCouPon(CouPonDto couPonDto) throws Exception;

	/**
	 * 2.修改优惠券状态
	 * 
	 * @param couPonDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-4
	 */
	public void updateCouPonStatus(CouPonDto couPonDto) throws Exception;

	/**
	 * 3.删除优惠券
	 * 
	 * @param couPonDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-4
	 */
	public void deleteCouPon(CouPonDto couPonDto) throws Exception;

	/**
	 * 4.通过礼品券ID查询礼品券
	 * 
	 * @param couPonDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-4
	 */
	public CouPonDto queryCouPonByCouPonId(CouPonDto couPonDto) throws Exception;

	/**
	 * 5.通过用户ID查询优惠券
	 * 
	 * @param couPonDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-4
	 */
	public List<CouPonDto> queryCouPonByUserId(CouPonDto couPonDto) throws Exception;

	/**
	 * 6.查询用户最大优惠券金额
	 * 
	 * @param couPonDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-4
	 */
	public CouPonDto queryCouPonMaxAmount(CouPonDto couPonDto) throws Exception;


}
