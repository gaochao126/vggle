package com.jiuyi.vggle.service.admin.user;

import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.user.UserDto;


public interface BackUserService {
	/**
	 * 1.查询网站普通用户信息
	 *
	 * @param userDto
	 * @return
	 * @throws Exception
	 * @date 2015-6-29
	 * @author gc
	 */
	public ResponseDto allUser(UserDto userDto) throws Exception;

	/**
	 * 2.管理员登录
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-30
	 */
	public ResponseDto adminLogin(UserDto userDto) throws Exception;

	/**
	 * 3.管理员注册
	 * 
	 * @param adminDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-30
	 */
	public ResponseDto adminRegister(UserDto userDto) throws Exception;

	/**
	 * 4.创建管理员
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-30
	 */
	public ResponseDto createAdmin(UserDto userDto) throws Exception;

	/**
	 * 5.管理员修改信息
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-6
	 */
	public ResponseDto updateAdmin(UserDto userDto) throws Exception;

	/**
	 * 6.修改管理员权限
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-6
	 */
	public ResponseDto alterAdminHavePermission(UserDto userDto) throws Exception;

	/**
	 * 7.删除管理员
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-6
	 */
	public ResponseDto deleteAdmin(UserDto userDto) throws Exception;

	/**
	 * 8.查询可操作权限
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-17
	 */
	public ResponseDto queryPermission(UserDto userDto) throws Exception;

}
