package com.jiuyi.vggle.service.address;

import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.address.AddressDto;

public interface AddressService {

	/**
	 * 1.添加地址
	 * 
	 * @author gc
	 * @date 2015-5-27
	 * @param addressDto
	 * @throws Exception
	 */
	public ResponseDto addAddress(AddressDto addressDto) throws Exception;

	/**
	 * 2.修改地址
	 * 
	 * @author gc
	 * @date 2015-5-27
	 * @param addressDto
	 * @throws Exception
	 */
	public ResponseDto updateAddress(AddressDto addressDto) throws Exception;

	/**
	 * 3.删除地址
	 * 
	 * @author gc
	 * @date 2015-5-27
	 * @param addressDto
	 * @throws Exception
	 */
	public ResponseDto deleteAddress(AddressDto addressDto) throws Exception;

	/**
	 * 4.修改地址状态
	 * 
	 * @author gc
	 * @date 2015-5-27
	 * @param addressDto
	 * @throws Exception
	 */
	public ResponseDto updateAddrStatus(AddressDto addressDto) throws Exception;

	/**
	 * 5.查询用户地址
	 * 
	 * @author gc
	 * @date 2015-6-1
	 * @param addressDto
	 * @throws Exception
	 */
	public ResponseDto queryAddrByUserId(AddressDto addressDto) throws Exception;
}
