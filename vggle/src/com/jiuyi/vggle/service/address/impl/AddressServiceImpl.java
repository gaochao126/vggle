package com.jiuyi.vggle.service.address.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.vggle.common.dict.CacheContainer;
import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.address.AddressDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.TokenDto;
import com.jiuyi.vggle.dto.address.AddressDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.address.AddressService;
import com.jiuyi.vggle.service.coin.impl.CoinServiceImpl;

@Service
public class AddressServiceImpl implements AddressService {
	private final static Logger logger = Logger.getLogger(CoinServiceImpl.class);

	@Autowired
	private AddressDao addressDao;
	/**
	 * 1.添加地址
	 * 
	 * @author gc
	 * @date 2015-5-27
	 * @param addressDto
	 * @throws Exception
	 */
	@Override
	public ResponseDto addAddress(AddressDto addressDto) throws Exception {
		/** step1. 空值判断 */
		if (addressDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断用户ID */
		TokenDto token = CacheContainer.getToken(addressDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}

		// if (addressDto.getUserId() != token.getUserDto().getId()) {
		// throw new BusinessException("用户ID不合法");
		// }
		addressDto.setUserId(token.getUserDto().getId());

		/** step3. 判断收货地址省市县不能为空 */
		if (!Util.isNotEmpty(addressDto.getProvince()) || !Util.isNotEmpty(addressDto.getCity())) {
			throw new BusinessException("省市信息不能为空");
		}

		/** step4. 判断收货地址省市县不能为空 */
		if (!Util.isNotEmpty(addressDto.getArea()) || !Util.isNotEmpty(addressDto.getStreet())) {
			throw new BusinessException("县街道信息不能为空");
		}

		/** step5. 判断收货人姓名不能为空 */
		if (!Util.isNotEmpty(addressDto.getPersonName()) || !Util.isNotEmpty(addressDto.getPersonPhone())) {
			throw new BusinessException("收货人姓名或联系方式不能为空");
		}

		/** step6. 添加地址 */
		addressDto.setAddrId(Util.getUniqueSn());
		addressDto.setAddrStatus(0);
		addressDao.addAddress(addressDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("添加成功");
		responseDto.setDetail(addressDto);
		return responseDto;
	}

	/**
	 * 2.修改地址
	 * 
	 * @author gc
	 * @date 2015-5-27
	 * @param addressDto
	 * @throws Exception
	 */
	@Override
	public ResponseDto updateAddress(AddressDto addressDto) throws Exception {
		/** step1. 空值判断 */
		if (addressDto == null) {
			logger.info("AddressServiceImpl.addAddress  addressDto is null");
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断地址ID */
		TokenDto token = CacheContainer.getToken(addressDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}

		addressDto.setUserId(token.getUserDto().getId());

		if (addressDto.getAddrId() == null || addressDto.getAddrId().trim().equals("")) {
			throw new BusinessException("地址ID不能为空");
		}

		/** step3. 判断收货地址省市县不能为空 */
		if (addressDto.getProvince() == null || addressDto.getProvince().trim().equals("") || addressDto.getCity() == null || addressDto.getCity().trim().equals("")) {
			throw new BusinessException("省市信息不能为空");
		}

		/** step4. 判断收货地址省市县不能为空 */
		if (addressDto.getArea() == null || addressDto.getArea().trim().equals("") || addressDto.getStreet() == null || addressDto.getStreet().trim().equals("")) {
			throw new BusinessException("县街道信息不能为空");
		}

		/** step5. 判断收货人姓名不能为空 */
		if (addressDto.getPersonName() == null || addressDto.getPersonName().trim().equals("") || addressDto.getPersonPhone() == null || addressDto.getPersonPhone().equals("")) {
			throw new BusinessException("收货人姓名或联系方式不能为空");
		}

		/** step6. 修改地址 */
		AddressDto addre = addressDao.queryAddrByAddrId(addressDto);
		if (addre == null) {
			throw new BusinessException("被修改地址不存在");
		}
		if (addre.getUserId() != token.getUserDto().getId()) {
			throw new BusinessException("该地址不属于您的地址，您无法操作");
		}

		addressDao.updateAddress(addressDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("修改成功");
		responseDto.setDetail(addressDto);
		return responseDto;
	}

	/**
	 * 3.删除地址
	 * 
	 * @author gc
	 * @date 2015-5-27
	 * @param addressDto
	 * @throws Exception
	 */
	@Override
	public ResponseDto deleteAddress(AddressDto addressDto) throws Exception {
		/** step1. 空值判断 */
		if (addressDto == null) {
			logger.info("AddressServiceImpl.addAddress  addressDto is null");
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断地址ID */
		if (addressDto.getAddrId() == null || addressDto.getAddrId().trim().equals("")) {
			throw new BusinessException("地址ID不能为空");
		}

		/** step3. 根据token得到用户对象 */
		TokenDto token = CacheContainer.getToken(addressDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}

		/** step4. 根据地址ID查询地址. */
		AddressDto addr = addressDao.queryAddrByAddrId(addressDto);
		if (addr == null) {
			throw new BusinessException("该地址不存在");
		}
		if (addr.getUserId() != token.getUserDto().getId()) {
			throw new BusinessException("该地址不属于您，无法删除");
		}

		/** step5. 删除地址 */
		addressDao.deleteAddress(addressDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("删除成功");
		return responseDto;
	}

	/**
	 * 4.设为默认地址
	 * 
	 * @author gc
	 * @date 2015-5-27
	 * @param addressDto
	 * @throws Exception
	 */
	@Override
	public ResponseDto updateAddrStatus(AddressDto addressDto) throws Exception {
		/** step1. 空值判断 */
		if (addressDto == null) {
			logger.info("AddressServiceImpl.addAddress  addressDto is null");
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断地址ID */
		if (addressDto.getAddrId() == null || addressDto.getAddrId().trim().equals("")) {
			throw new BusinessException("地址ID不能为空");
		}

		/** step3. 根据token得到用户对象 */
		TokenDto token = CacheContainer.getToken(addressDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}

		/** step4. 根据地址ID查询地址. */
		AddressDto addr = addressDao.queryAddrByAddrId(addressDto);
		if (addr == null) {
			throw new BusinessException("该地址不存在");
		}
		if (addr.getUserId() != token.getUserDto().getId()) {
			throw new BusinessException("该地址不属于您，无法删除");
		}

		/** step5. 修改其他地址为非默认状态. */
		addressDao.updateAddrStatusByStatus(addressDto);

		/** step6. 判断 */
		addressDto.setAddrStatus(1);
		addressDao.updateAddrStatus(addressDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("设置成功");
		return responseDto;
	}

	/**
	 * 5.查询用户地址
	 * 
	 * @author gc
	 * @date 2015-6-1
	 * @param addressDto
	 * @throws Exception
	 */
	@Override
	public ResponseDto queryAddrByUserId(AddressDto addressDto) throws Exception {
		/** step1. 空值判断 */
		if (addressDto == null) {
			logger.info("AddressServiceImpl.addAddress  addressDto is null");
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 根据token得到用户对象 */
		TokenDto token = CacheContainer.getToken(addressDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		addressDto.setUserId(token.getUserDto().getId());
		
		/** step3. 查询地址. */
		List<AddressDto> addrs = addressDao.queryAddrByUserId(addressDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("查询成功");
		responseDto.setDetail(addrs);
		return responseDto;
	}
}
