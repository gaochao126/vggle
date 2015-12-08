package com.jiuyi.vggle.service.admin.permission;

import java.util.List;

import com.jiuyi.vggle.dto.admin.permission.PermissionDetailDto;
import com.jiuyi.vggle.dto.user.UserDto;

public interface QueryPermissionService {

	/**
	 * 1.查询所有权限
	 * 
	 * @param permissionDetailDto
	 * @return
	 * @throws Exception
	 */
	public List<PermissionDetailDto> queryAllPermission(PermissionDetailDto permissionDetailDto) throws Exception;

	/**
	 * query admin permission action
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public List<String> Action(UserDto userDto) throws Exception;

	/**
	 * 修改管理员权限（内存中）
	 * 
	 * @param userDto
	 * @throws Exception
	 */
	public void alterAdminPermission(UserDto userDto) throws Exception;
}
