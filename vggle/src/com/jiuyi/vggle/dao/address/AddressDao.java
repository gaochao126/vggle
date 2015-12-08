package com.jiuyi.vggle.dao.address;

import java.util.List;

import com.jiuyi.vggle.dto.address.AddressDto;

public interface AddressDao {

	/**
	 * 1.添加地址
	 * 
	 * @author gc
	 * @date 2015-5-27
	 * @param addressDto
	 * @throws Exception
	 */
	public void addAddress(AddressDto addressDto) throws Exception;

	/**
	 * 2.修改地址
	 * 
	 * @author gc
	 * @date 2015-5-27
	 * @param addressDto
	 * @throws Exception
	 */
	public void updateAddress(AddressDto addressDto) throws Exception;

	/**
	 * 3.删除地址
	 * 
	 * @author gc
	 * @date 2015-5-27
	 * @param addressDto
	 * @throws Exception
	 */
	public void deleteAddress(AddressDto addressDto) throws Exception;

	/**
	 * 4.修改地址状态
	 * 
	 * @author gc
	 * @date 2015-5-27
	 * @param addressDto
	 * @throws Exception
	 */
	public void updateAddrStatus(AddressDto addressDto) throws Exception;

	/**
	 * 5.修改原来默认地址为非默认
	 * 
	 * @author gc
	 * @date 2015-5-27
	 * @param addressDto
	 * @throws Exception
	 */
	public void updateAddrStatusByStatus(AddressDto addressDto) throws Exception;

	/**
	 * 6.查询用户地址
	 * 
	 * @author gc
	 * @date 2015-5-27
	 * @param addressDto
	 * @throws Exception
	 */
	public List<AddressDto> queryAddrByUserId(AddressDto addressDto) throws Exception;

	/**
	 * 7.根据地址ID查询地址对象
	 * 
	 * @author gc
	 * @date 2015-6-5
	 * @param addressDto
	 * @throws Exception
	 */
	public AddressDto queryAddrByAddrId(AddressDto addressDto) throws Exception;

}
