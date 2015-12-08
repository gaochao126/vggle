package com.jiuyi.vggle.service.admin.user.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.jiuyi.vggle.common.dict.CacheContainer;
import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.util.Enumerate;
import com.jiuyi.vggle.common.util.MD5;
import com.jiuyi.vggle.common.util.SmsVerifyKit;
import com.jiuyi.vggle.common.util.SysCfg;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.admin.AdminDao;
import com.jiuyi.vggle.dao.user.UserDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.TokenDto;
import com.jiuyi.vggle.dto.admin.permission.PermissionDetailDto;
import com.jiuyi.vggle.dto.user.UserDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.admin.user.BackUserService;

@Service
public class BackUserServiceImpl implements BackUserService {
	@Autowired
	private UserDao userDao;

	@Autowired
	private AdminDao adminDao;



	/**
	 * 1.查看普通用户
	 *
	 * @param userDto
	 * @return
	 * @throws Exception
	 * @date 2015-6-29
	 * @author gc
	 */
	@Override
	public ResponseDto allUser(UserDto userDto) throws Exception {
		/** init. */
		if (!CacheContainer.user_cmd(userDto).contains(userDto.getCmd())) {
			throw new BusinessException("您没有此操作的权限");
		}

		/** step1: query all user */
		List<UserDto> allUser = userDao.queryAllUser(userDto);

		/** step2: for allUser change user head img src */
		for (int i = 0; i < allUser.size(); i++) {
			if (Util.isNotEmpty(allUser.get(i).getHeadPortrait())) {
				allUser.get(i).setHeadPortrait(Enumerate.USER_HEAD + allUser.get(i).getHeadPortrait());
			}
		}

		/** step3: */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("所有用户");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("alluser", allUser);
		dataMap.put("count", allUser.size());
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 2.管理员登录
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-30
	 */
	@Override
	public ResponseDto adminLogin(UserDto userDto) throws Exception {

		/** step1:空异常处理. */
		if (userDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2:校验手机号不为空. */
		if (!Util.isNotEmpty(userDto.getPhone())) {
			throw new BusinessException("手机号未填写");
		}

		/** step3:校验密码不为空. 和管理员级别不能为空 */
		if (!Util.isNotEmpty(userDto.getPassword())) {
			throw new BusinessException("密码不能为空");
		}

		/** step4: 判断手机号是否存在 */
		UserDto admin = adminDao.queryAdminByPhone(userDto);
		if (admin == null) {
			throw new BusinessException("用户不存在");
		}

		/** step5:校验账号或密码是否正确. */
		userDto.setPassword(MD5.getMD5Code(userDto.getPassword()));
		UserDto dto = adminDao.queryByPhoneAndPassword(userDto);
		if (dto == null) {
			throw new BusinessException("密码错误");
		}

		/** step6:保存token. */
		TokenDto token = new TokenDto();
		dto.setToken(MD5.getMD5Code(Util.getUniqueSn()));
		token.setToken(dto.getToken());
		token.setUserDto(dto);
		token.setUpdateTime(System.currentTimeMillis());
		CacheContainer.putToken(token.getToken(), token);

		/** step7:返回结果. */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("登录成功");
		dto.setPassword(null);
		responseDto.setDetail(dto);
		return responseDto;
	}

	/**
	 * 3.管理员注册
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-30
	 */
	@Override
	public ResponseDto adminRegister(UserDto userDto) throws Exception {
		/** step1:空异常处理. */
		if (userDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2:校验手机号不为空. */
		if (!Util.isNotEmpty(userDto.getPhone())) {
			throw new BusinessException("手机号未填写");
		}

		/** step3:校验密码不为空. */
		if (!Util.isNotEmpty(userDto.getPassword())) {
			throw new BusinessException("密码未填写");
		}

		/** step4:校验密码加密方式. */
		if (userDto.getPassword().length() != 32) {
			throw new BusinessException("密码加密方式不对");
		}

		/** step5:校验账号是否存在. */
		UserDto dto = userDao.queryUserByPhone(userDto);
		if (dto != null) {
			throw new BusinessException("该手机号已注册");
		}

		/** step6:校验短信验证码. */
		String verifyResult = new SmsVerifyKit(SysCfg.getString("messageAuthentication.appkey"), userDto.getPhone(), SysCfg.getString("messageAuthentication.zone"), userDto.getVerificationCode()).checkcode();
		JsonObject jsonObject = verifyResult != null ? Constants.jsonParser.parse(verifyResult).getAsJsonObject() : null;
		if (!(jsonObject != null && jsonObject.has("status") && jsonObject.get("status").getAsInt() == 200)) {
			throw new BusinessException("手机验证码不对");
		}

		/** step7:注册. */
		userDto.setPassword(MD5.getMD5Code(userDto.getPassword()));
		adminDao.adminRegister(userDto);

		/** step8:返回结果. */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("注册成功");
		return responseDto;
	}

	/**
	 * 4.创建管理员
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-30
	 */
	@Override
	public ResponseDto createAdmin(UserDto userDto) throws Exception {
		/** step1: judge null */
		if (userDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** init. */
		if (!CacheContainer.user_cmd(userDto).contains(userDto.getCmd())) {
			throw new BusinessException("您没有此操作的权限");
		}

		/** step2: judge id password confirmPassword */
		if (userDto.getId() == null || userDto.getId().equals("")) {
			throw new BusinessException("用户ID不能为空");
		}

		/** step3: judge password confirmPassword */
		if (!Util.isNotEmpty(userDto.getPassword()) || !Util.isNotEmpty(userDto.getConfirmPass())) {
			throw new BusinessException("密码或确认密码不能为空");
		}

		/** step4: judge phone and companyId and compName */
		if (userDto.getPhone() == null || userDto.getPhone().equals("")) {
			throw new BusinessException("手机号不能为空");
		}

		if (userDto.getPassword().length() != 32 || userDto.getConfirmPass().length() != 32) {
			throw new BusinessException("密码加密方式不正确");
		}

		/** step5： judge password and confirmPass */
		if (!userDto.getPassword().equals(userDto.getConfirmPass())) {
			throw new BusinessException("两次密码不一致");
		}

		/** step6: judge permissionGrade */
		if (userDto.getLevel() == null || userDto.getLevel().equals("")) {
			throw new BusinessException("当前管理员权限等级不能为空");
		}

		/** step6: set up level */
		userDto.setPassword(MD5.getMD5Code(userDto.getPassword()));
		/** 1 为超级管理员. 2.高级管理员. 3.企业一级管理员. 4.企业二级管理员. */
		if (userDto.getLevel() == 1) {
			userDto.setPermissionGrade(2);
			userDto.setGradeInfo("高级管理员");
		}
		if (userDto.getLevel() == 2) {
			userDto.setPermissionGrade(3);
			userDto.setGradeInfo("企业一级管理员");
		}
		if (userDto.getLevel() == 3) {
			userDto.setPermissionGrade(4);
			userDto.setGradeInfo("企业二级管理员");
		}

		/** 设置创建者ID. */
		userDto.setCreateId(userDto.getId());

		/** 执行添加. */
		adminDao.adminRegister(userDto);

		/** step7: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("创建结果");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("message", "创建成功");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 5.管理员修改信息
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-6
	 */
	@Override
	public ResponseDto updateAdmin(UserDto userDto) throws Exception {
		/** step1: judge null */
		if (userDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: judge phone */
		if (userDto.getPhone() == null || userDto.getPhone().equals("") || userDto.getId() == null || userDto.getId().equals("")) {
			throw new BusinessException("手机号或用户ID不能为空");
		}

		/** step3: exe update */
		adminDao.updateAdmin(userDto);

		/** step4: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("修改结果");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("message", "修改成功");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 6.修改管理员权限
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-6
	 */
	@Override
	public ResponseDto alterAdminHavePermission(UserDto userDto) throws Exception {
		/** step1: judge null */
		if (userDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}


		/** init. */
		if (!CacheContainer.user_cmd(userDto).contains(userDto.getCmd())) {
			throw new BusinessException("您没有此操作的权限");
		}

		/** step2: judge companyId and havePermission */
		if (!Util.isNotEmpty(userDto.getCompanyId()) || !Util.isNotEmpty(userDto.getHavePermission())) {
			throw new BusinessException("企业ID和权限集合不能为空");
		}

		/** step3: judge id */
		if (userDto.getId() == null || userDto.getId().equals("") || userDto.getAnotherId() == null || userDto.getAnotherId().equals("")) {
			throw new BusinessException("当前管理员ID或被修改管理员ID不能为空");
		}

		/** step4: query admin by anotherID */
		UserDto user = adminDao.queryAdminByAnotherId(userDto);
		if (!userDto.getCompanyId().equals(user.getCompanyId())) {
			throw new BusinessException("您没有此操作权限,你们不属于同一家公司");
		}

		/** step5: judge permissionGrade */
		if ((user.getPermissionGrade() - 1) != userDto.getPermissionGrade()) {

			throw new BusinessException("您没有此操作权限,权限等级不合法");
		}

		/** step6: exe alter */
		adminDao.alterPermission(userDto);

		/** step7: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("修改结果");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("message", "修改成功");
		responseDto.setDetail(dataMap);
		return responseDto;

	}

	/**
	 * 7.删除管理员(权限操作)
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-6
	 */
	@Override
	public ResponseDto deleteAdmin(UserDto userDto) throws Exception {
		/** step1: judge null */
		if (userDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** init. */
		if (!CacheContainer.user_cmd(userDto).contains(userDto.getCmd())) {
			throw new BusinessException("您没有此操作的权限");
		}

		/** step2: judge permissionGrade and id 判断权限等级 */
		if (userDto.getId() == null || userDto.getId().equals("") || userDto.getAnotherId() == null || userDto.getAnotherId().equals("")) {
			throw new BusinessException("当前管理员ID或被删除管理员ID不能为空");
		}

		/** step3: */
		if (userDto.getPermissionGrade() == null || userDto.getPermissionGrade().equals("")) {
			throw new BusinessException("当前管理员权限等级不能为空");
		}

		/** step4: query admin by anotherID */
		UserDto user = adminDao.queryAdminByAnotherId(userDto);
		if (user == null) {
			throw new BusinessException("被删除管理员不存在");
		}

		/** step5: 判断该管理员是否是创建者. */
		if (user.getCreateId() != userDto.getId()) {
			throw new BusinessException("该管理员不是您创建，无法删除");
		}

		/** step6: 查询被删除管理员是否创建过管理员 ， 如果该管理员创建过管理员那么他将删除他创建的管理员组. */

		/** 查询该管理员创建的组 */
		UserDto uDto = new UserDto();
		uDto.setCreateId(userDto.getAnotherId());
		List<UserDto> adminOne = adminDao.queryAdminByCreateId(uDto);

		/** 如果被删除管理员创建的组，那么开始操作他创建的组. */
		if (adminOne != null && adminOne.size() > 0) {
			for (int i = 0; i < adminOne.size(); i++) {

				/** 查询该管理员创建的组. */
				UserDto nextAdmin = new UserDto();
				nextAdmin.setCreateId(adminOne.get(i).getId());
				List<UserDto> groupAdmin = adminDao.queryAdminByCreateId(nextAdmin);

				/** 如果该管理员创建了组，将继续操作组. */
				if (groupAdmin != null && groupAdmin.size() > 0) {
					for (int j = 0; j < groupAdmin.size(); j++) {
						UserDto endAdmin = new UserDto();
						endAdmin.setId(groupAdmin.get(i).getId());
						adminDao.deleteAdmin(endAdmin);
					}
				}

				/** 创建一个用户对象，用于删除操作. */
				UserDto nextAloneAdmin = new UserDto();
				nextAloneAdmin.setId(adminOne.get(i).getId());

				/** 删除管理员. */
				adminDao.deleteAdmin(nextAloneAdmin);
			}
		}

		/** 创建一个用户对象. 用于删除操作 */
		UserDto aloneAdmin = new UserDto();
		aloneAdmin.setId(userDto.getAnotherId());

		/** 删除被删除的管理员. */
		adminDao.deleteAdmin(aloneAdmin);

		/** step7: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("删除结果");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("message", "删除成功");
		responseDto.setDetail(dataMap);
		return responseDto;
	}

	/**
	 * 8.查询可操作权限
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-17
	 */
	@Override
	public ResponseDto queryPermission(UserDto userDto) throws Exception {
		/** step1: judge null */
		if (userDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2: judge userId */
		if (userDto.getId() == null || userDto.getId().equals("")) {
			throw new BusinessException("当前管理员ID不能为空");
		}

		/** step3: query permission */
		List<PermissionDetailDto> permission = new ArrayList<PermissionDetailDto>();
		permission = CacheContainer.allPermission();

		/** step4: query admin bi id */
		UserDto user = adminDao.queryAdminById(userDto);
		if (user.getPermissionGrade() != 1) {
			for (int i = 0; i < permission.size(); i++) {
				if (permission.get(i).getPermissionId().equals("1")) {
					permission.remove(i);
					break;
				}
			}
		}
		/** step5: result */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("所有可操作权限");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("permission", permission);
		dataMap.put("message", "[permissionDesc：权限描述；permissionType：权限类别：1用户相关，2订单相关，3商品相关]");
		responseDto.setDetail(dataMap);
		return responseDto;
	}
}
