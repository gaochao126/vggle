package com.jiuyi.vggle.service.admin.permission.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.admin.AdminDao;
import com.jiuyi.vggle.dto.admin.permission.PermissionDetailDto;
import com.jiuyi.vggle.dto.user.UserDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.admin.permission.QueryPermissionService;

@Service
public class QueryPermissionServiceImpl implements QueryPermissionService {

	/** 后端权限入口. */
	public static ConcurrentHashMap<Integer, List<String>> admin_permission = new ConcurrentHashMap<Integer, List<String>>();

	/** 权限集合. */
	public static List<PermissionDetailDto> permission = new ArrayList<PermissionDetailDto>();

	@Autowired
	private AdminDao adminDao;

	/**
	 * 1.查询所有权限
	 * 
	 * @param permissionDetailDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<PermissionDetailDto> queryAllPermission(PermissionDetailDto permissionDetailDto) throws Exception {
		if (permission.isEmpty()) {
			permission = adminDao.queryPermission(permissionDetailDto);
			return permission;
		}
		return permission;
	}

	/**
	 * query admin permission action
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<String> Action(UserDto userDto) throws Exception {
		if (!admin_permission.containsKey(userDto.getId())) {
			PermissionDetailDto permissionDetailDto = new PermissionDetailDto();
			queryAllPermission(permissionDetailDto);

			/** query Admin by id */
			UserDto user = adminDao.queryAdminById(userDto);

			if (user == null) {
				throw new BusinessException("用户不存在");
			}
			System.out.println("用户权限ID集合" + user.getHavePermission());
			/** query admin permission id list */
			String havePermisssion = user.getHavePermission();

			/** String to Serialization */
			JSONArray jsonarray = JSONArray.parseArray(havePermisssion);

			System.out.println("用户权限ID集合大小" + jsonarray.size());

			/** create permission object */
			PermissionDetailDto permissDto = new PermissionDetailDto();

			/** 存储用户动作. */
			List<String> per_action = new ArrayList<String>();
			if (jsonarray == null || jsonarray.size() == 0) {
				throw new BusinessException("该用户尚未分配权限");
			}

			for (int i = 0; i < jsonarray.size(); i++) {
				PermissionDetailDto permiss = new PermissionDetailDto();

				permissDto.setPermissionId(jsonarray.get(i).toString());

				permiss = adminDao.queryPermissionById(permissDto);
				if (permiss == null) {
					throw new BusinessException("没有找到对应权限ID的权限对象");
				}
				JSONArray jsonarr = JSONArray.parseArray(permiss.getAction());

				for (int j = 0; j < jsonarr.size(); j++) {
					per_action.add(jsonarr.get(j).toString());
				}
			}

			admin_permission.put(userDto.getId(), per_action);
			return admin_permission.get(userDto.getId());
		}
		return admin_permission.get(userDto.getId());
	}

	/**
	 * 修改管理员权限（内存中）
	 * 
	 * @param userDto
	 * @throws Exception
	 */
	@Override
	public void alterAdminPermission(UserDto userDto) throws Exception{
		/** step1: 空值判断. */
		if (userDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}
		
		/** step2: 判断权限字符串. */
		if (!Util.isNotEmpty(userDto.getHavePermission())) {
			throw new BusinessException("权限字符串不能为空");
		}

		/** step1: 获得权限字符串集合. */
		String permissionStr = userDto.getHavePermission();

		/** String to Serialization */
		JSONArray jsonarray = JSONArray.parseArray(permissionStr);

		/** create permission object */
		PermissionDetailDto permissDto = new PermissionDetailDto();
		List<String> per_action = new ArrayList<String>();
		for (int i = 0; i < jsonarray.size(); i++) {
			PermissionDetailDto permiss = new PermissionDetailDto();
			permissDto.setPermissionId(jsonarray.get(i).toString());
			permiss = adminDao.queryPermissionById(permissDto);

			JSONArray jsonarr = JSONArray.parseArray(permiss.getAction());


			for (int j = 0; j < jsonarr.size(); j++) {
				per_action.add(jsonarr.get(j).toString());
			}
		}
		admin_permission.putIfAbsent(userDto.getId(), per_action);
	}
}
