package com.jiuyi.vggle.dao.admin;


import java.util.List;

import com.jiuyi.vggle.dto.admin.permission.PermissionDetailDto;
import com.jiuyi.vggle.dto.user.UserDto;

public interface AdminDao {
	/**
	 * 1.注册
	 * 
	 * @param adminDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-30
	 */
	public void adminRegister(UserDto userDto) throws Exception;

	/**
	 * 2.登录
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-30
	 */
	public UserDto queryByPhoneAndPassword(UserDto userDto) throws Exception;

	/**
	 * 3.用过ID查询管理员对象信息
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public UserDto queryAdminById(UserDto userDto) throws Exception;

	/**
	 * 4.查询权限详情
	 * 
	 * @param permissionDetailDto
	 * @return
	 * @throws Exception
	 */
	public List<PermissionDetailDto> queryPermission(PermissionDetailDto permissionDetailDto) throws Exception;

	/**
	 * 5.通过权限ID查询权限对象
	 * 
	 * @param permissionDetailDto
	 * @return
	 * @throws Exception
	 */
	public PermissionDetailDto queryPermissionById(PermissionDetailDto permissionDetailDto) throws Exception;

	/**
	 * 6.修改管理员信息
	 * 
	 * @param permissionDetailDto
	 * @return
	 * @throws Exception
	 */
	public void updateAdmin(UserDto userDto) throws Exception;

	/**
	 * 7.修改管理员权限
	 * 
	 * @param permissionDetailDto
	 * @return
	 * @throws Exception
	 */
	public void alterPermission(UserDto userDto) throws Exception;

	/**
	 * 8.删除管理员
	 * 
	 * @param permissionDetailDto
	 * @return
	 * @throws Exception
	 */
	public void deleteAdmin(UserDto userDto) throws Exception;

	/**
	 * 9.通过anotherID查询管理员对象信息
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public UserDto queryAdminByAnotherId(UserDto userDto) throws Exception;

	/**
	 * 10.通过创建者ID查询管理员集合
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public List<UserDto> queryAdminByCreateId(UserDto userDto) throws Exception;

	/**
	 * 11.通过手机号查询管理员对象
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public UserDto queryAdminByPhone(UserDto userDto) throws Exception;
}
