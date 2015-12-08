package com.jiuyi.vggle.service.coupon.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.vggle.common.dict.CacheContainer;
import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.coupon.CouPonDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.TokenDto;
import com.jiuyi.vggle.dto.coupon.CouPonDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.coupon.CouPonService;

@Service
public class CouPonServiceImpl implements CouPonService {

	@Autowired
	private CouPonDao couPonDao;

	/**
	 * 1.添加优惠券
	 * 
	 * @param couPonDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-4
	 */
	@Override
	public ResponseDto addCouPon(CouPonDto couPonDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 2.修改优惠券状态
	 * 
	 * @param couPonDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-4
	 */
	@Override
	public ResponseDto updateCouPonStatus(CouPonDto couPonDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 3.删除优惠券
	 * 
	 * @param couPonDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-4
	 */
	@Override
	public ResponseDto deleteCouPon(CouPonDto couPonDto) throws Exception {
		/** step1: judge null */
		if (couPonDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: judge couponId */
		if (!Util.isNotEmpty(couPonDto.getCouponId())) {
			throw new BusinessException("优惠券ID不能为空");
		}
		
		/** step3: delete coupon */
		couPonDao.deleteCouPon(couPonDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("删除成功");
		return responseDto;
	}

	/**
	 * 4.通过优惠券ID得到优惠券
	 * 
	 * @param couPonDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-4
	 */
	@Override
	public ResponseDto queryCouPonByCouPonId(CouPonDto couPonDto) throws Exception {

		/** step1: judge null */
		if (couPonDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: judge couponId */
		if (!Util.isNotEmpty(couPonDto.getCouponId())) {
			throw new BusinessException("优惠券ID不能为空");
		}

		/** step3: query coupon by userId and couponId */
		CouPonDto coupon = couPonDao.queryCouPonByCouPonId(couPonDto);

		/** step4: result */
		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("coupon", coupon);
		responseDto.setDetail(dataMap);
		responseDto.setResultDesc("成功查询优惠券");
		return responseDto;

	}


	/**
	 * 5.通过用户ID查询优惠券
	 * 
	 * @param couPonDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-4
	 */
	@Override
	public ResponseDto queryCouPonByUserId(CouPonDto couPonDto) throws Exception {
		/** step1: judge null */
		if (couPonDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: judge couponId */
		TokenDto token = CacheContainer.getToken(couPonDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		couPonDto.setUserId(token.getUserDto().getId());

		/** step3: query userCoupon */
		Double amount = 0.0;
		List<CouPonDto> coupons = couPonDao.queryCouPonByUserId(couPonDto);
		if(coupons==null || coupons.size()==0){
			amount = 0.0;
		}
		for (int i = 0; i < coupons.size(); i++) {
			amount += coupons.get(i).getCouponAmount();
		}
		
		/** step4: result */
		ResponseDto responseDto = new ResponseDto();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("coupon", coupons);
		dataMap.put("count", coupons.size());
		dataMap.put("couponAmount", Util.Rounding(amount));
		responseDto.setDetail(dataMap);
		responseDto.setResultDesc("用户优惠券");
		return responseDto;
	}

	/**
	 * 6.查询用户最大优惠券金额
	 * 
	 * @param couPonDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-4
	 */
	@Override
	public ResponseDto queryCouPonMaxAmount(CouPonDto couPonDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
