package com.jiuyi.vggle.service.user.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
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
import com.jiuyi.vggle.dao.code.CodeDao;
import com.jiuyi.vggle.dao.user.UserDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.TokenDto;
import com.jiuyi.vggle.dto.code.CodeDto;
import com.jiuyi.vggle.dto.user.UserDto;
import com.jiuyi.vggle.service.BusinessException;
import com.jiuyi.vggle.service.user.UserService;

/**
 * @description 用户业务层实现类
 * @author zhb
 * @createTime 2015年5月21日
 */
@Service
public class UserServiceImpl implements UserService {
	private final static Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

	@Autowired
	private CodeDao codeDao;

	private static List<CodeDto> codes = new ArrayList<CodeDto>();

    /**
     * @description 获取短信验证码
     * @param userDto
     * @return
     * @throws Exception
     */
    @Override
	public ResponseDto getVerifyCode(UserDto userDto) throws Exception {
        /** step1:空异常处理. */
        if (userDto == null) {
            throw new BusinessException(Constants.DATA_ERROR);
        }

        /** step2:手机号不为空校验. */
        if (!Util.isNotEmpty(userDto.getPhone())) {
            throw new BusinessException("手机号必须填写");
        }
        /** step3:发送验证码. */
        String result = new SmsVerifyKit(SysCfg.getString("messageAuthentication.appkey"), userDto.getPhone(), SysCfg.getString("messageAuthentication.zone"),
                null).sendMsg();
        JsonObject jsonObject = Constants.jsonParser.parse(result).getAsJsonObject();
		logger.info("UserServiceImpl.getVerifyCode status:" + jsonObject.get("status").getAsInt());
        if (!(jsonObject != null && jsonObject.has("status") && jsonObject.get("status").getAsInt() == 200)) {
            throw new BusinessException("验证码获取失败");
        }

        /** step4:返回结果. */
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultDesc("验证码获取成功");
        return responseDto;
    }

    /**
     * @description 注册
     * @param userDto
     * @return
     * @throws Exception
     */
    @Override
	public ResponseDto register(UserDto userDto) throws Exception {
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
        String verifyResult = new SmsVerifyKit(SysCfg.getString("messageAuthentication.appkey"), userDto.getPhone(),
                SysCfg.getString("messageAuthentication.zone"), userDto.getVerificationCode()).checkcode();
        JsonObject jsonObject = verifyResult != null ? Constants.jsonParser.parse(verifyResult).getAsJsonObject() : null;
        if (!(jsonObject != null && jsonObject.has("status") && jsonObject.get("status").getAsInt() == 200)) {
            throw new BusinessException("手机验证码不对");
        }

		/** step7:注册. */
		userDto.setPassword(MD5.getMD5Code(userDto.getPassword()));
        userDao.register(userDto);

		/** step8:返回结果. */
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultDesc("注册成功");
        return responseDto;
    }

    /**
     * @description 登录
     * @param userDto
     * @return
     * @throws Exception
     */
    @Override
	public ResponseDto signIn(UserDto userDto) throws Exception {
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

        /** step4:校验账号或密码是否正确. */
        UserDto dto = userDao.queryUserByPhone(userDto);
		if (dto == null || !dto.getPassword().equals(MD5.getMD5Code(userDto.getPassword()))) {
            throw new BusinessException("账号或密码错误");
        }

        /** step5:保存token. */
        TokenDto token = new TokenDto();
        dto.setToken(MD5.getMD5Code(Util.getUniqueSn()));
        token.setToken(dto.getToken());
        token.setUserDto(dto);
        token.setUpdateTime(System.currentTimeMillis());
        CacheContainer.putToken(token.getToken(), token);

        /** step6:返回结果. */
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultDesc("登录成功");
        dto.setPassword(null);
        responseDto.setDetail(dto);
        return responseDto;
    }

    /**
     * @description 登出
     * @param userDto
     * @return
     * @throws Exception
     */
    @Override
	public ResponseDto signOut(UserDto userDto) throws Exception {
        /** step1:空异常处理. */
        if (userDto == null) {
            throw new BusinessException(Constants.DATA_ERROR);
        }

        /** step2:删除token. */
        TokenDto token = CacheContainer.getToken(userDto.getToken());
        if (token != null) {
            CacheContainer.removeToken(token.getToken());
        }

        /** step3:返回结果. */
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultDesc("登出成功");
        return responseDto;
    }

    /**
     * @description 查询用户信息
     * @param userDto
     * @return
     * @throws Exception
     */
    @Override
	public ResponseDto queryUserInfo(UserDto userDto) throws Exception {
        /** step1:空异常处理. */
        if (userDto == null) {
            throw new BusinessException(Constants.DATA_ERROR);
        }

        /** step2:获取用户信息. */
        TokenDto token = CacheContainer.getToken(userDto.getToken());
        UserDto dto = token != null ? token.getUserDto() : null;
		if (dto != null) {
			if (!Util.isNotEmpty(dto.getNickname())) {
				dto.setNickname(dto.getPhone());
			}
		}
        /** step3:返回结果. */
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultDesc("查询成功");
        responseDto.setDetail(dto);
        return responseDto;
    }

    /**
     * @description 编辑用户信息
     * @param userDto
     * @return
     * @throws Exception
     */
    @Override
	public ResponseDto editUserInfo(UserDto userDto) throws Exception {
        /** step1:空异常处理. */
        if (userDto == null) {
            throw new BusinessException(Constants.DATA_ERROR);
        }

        /** step2:保存更改信息. */
        userDao.editUserInfo(userDto);

        /** step3:更新token. */
        TokenDto token = CacheContainer.getToken(userDto.getToken());
        UserDto dto = userDao.queryUserById(userDto);
        if (token != null) {
            token.setUserDto(dto);
        }

        /** step4:返回结果. */
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultDesc("保存成功");
        return responseDto;
    }

	/**
	 * 验证手机号是否存在
	 * 
	 * @author gc
	 * @date 2015-5-26
	 * @param phone
	 */
	@Override
	public ResponseDto checkPhone(UserDto userDto) throws Exception {
		/** step1:空异常处理. */
		if (userDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}
		/** step2:校验手机号不为空. */
		if (!Util.isNotEmpty(userDto.getPhone())) {
			throw new BusinessException("手机号未填写");
		}

		/** step3:校验账号是否存在. */
		UserDto dto = userDao.queryUserByPhone(userDto);
		if (dto == null) {
			throw new BusinessException("手机账户不存在");
		}
		/** step4:返回结果. */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("手机账户存在");
		return responseDto;
	}

	/**
	 * @description 修改钱包密码
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto editWalletPass(UserDto userDto) throws Exception {
		/** step1:空异常处理. */
		if (userDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2.用户ID和钱包密码不能为空 */
		if (userDto.getWalletPass() == null || userDto.getWalletPass().equals("")) {
			throw new BusinessException("钱包密码不能为空");
		}

		/** step3. 通过token获取ID */
		TokenDto token = CacheContainer.getToken(userDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		userDto.setId(token.getUserDto().getId());

		/** step4. 判断钱包密码和用户密码是否相同 */
		UserDto user = userDao.queryUserById(userDto);
		if (user.getPassword().equals(MD5.getMD5Code(userDto.getWalletPass()))) {
			throw new BusinessException("钱包密码不可与登录密码相同");
		}
		userDto.setWalletPass(MD5.getMD5Code(userDto.getWalletPass()));

		/** step5. 对密码进行md5加密 */
		userDao.updateUserWalletPass(userDto);

		/** step6:返回结果. */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("设置成功");
		return responseDto;
	}

	/**
	 * @description 获取验证码
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto queryCode(CodeDto codeDto) throws Exception {
		codeList();
		Random random = new Random();
		int index = random.nextInt(30);
		codes.get(index).setCodeName(codes.get(index).getCodeName());

		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("获取验证码成功");
		responseDto.setDetail(codes.get(index));
		return responseDto;
	}

	/**
	 * @description 校验验证码
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto CheckCode(CodeDto codeDto) throws Exception {
		/** step1:空异常处理. */
		if (codeDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2.判断codeID 和验证码值不能为空 */
		if (codeDto.getCodeId() == null || codeDto.getCodeId().equals("") || codeDto.getCodeValue() == null || codeDto.getCodeValue().equals("")) {
			throw new BusinessException("验证码ID和验证码值不能为空");
		}

		/** step3. 判断验证码是否正确 */
		CodeDto code = codeDao.queryCodeById(codeDto);
		if (!code.getCodeValue().equals(codeDto.getCodeValue().toLowerCase())) {
			throw new BusinessException("验证码错误");
		}
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("验证码正确");
		return responseDto;
	}

	/**
	 * @description 查询验证码到内存中
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<CodeDto> codeList() throws Exception {
		if (codes.isEmpty()) {
			CodeDto codeDto = new CodeDto();
			codes = codeDao.queryAllCode(codeDto);
			for (int i = 0; i < codes.size(); i++) {
				codes.get(i).setCodeName(Enumerate.CODE_SRC + codes.get(i).getCodeName());
			}
			return codes;
		}
		return codes;
	}

	/**
	 * @description 查询用户是否设置钱包密码
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto queryUserByWalletPass(UserDto userDto) throws Exception {
		/** step1:空异常处理. */
		if (userDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 获取用户ID */
		TokenDto token = CacheContainer.getToken(userDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		userDto.setId(token.getUserDto().getId());

		/** step3. 查询用户对象 */
		ResponseDto responseDto = new ResponseDto();
		UserDto user = userDao.queryUserById(userDto);


		Map<String, Object> dataMap = new HashMap<String, Object>();
		if (user.getWalletPass() == null || user.getWalletPass().equals("")) {
			responseDto.setResultDesc("用户没有设置钱包密码");
			dataMap.put("result", "1");
			responseDto.setDetail(dataMap);
		} else {
			/** step4:返回结果. */
			responseDto.setResultDesc("用户已经设置钱包密码");
			dataMap.put("result", "0");
			responseDto.setDetail(dataMap);
		}
		return responseDto;
	}

	/**
	 * @description 判断钱包密码是否正确
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto checkWalletPass(UserDto userDto) throws Exception {
		/** step1:空异常处理. */
		if (userDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断用户ID和钱包密码不能为空 */
		if (userDto.getWalletPass() == null || userDto.getWalletPass().equals("")) {
			throw new BusinessException("用户ID或钱包密码不能为空");
		}

		/** step3. 获取ID */
		TokenDto token = CacheContainer.getToken(userDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		userDto.setId(token.getUserDto().getId());

		/** step4. 判断钱包密码输入是否正确 */
		UserDto user = userDao.queryUserById(userDto);
		if (!user.getWalletPass().equals(MD5.getMD5Code(userDto.getWalletPass()))) {
			throw new BusinessException("钱包密码错误");
		}

		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("钱包密码正确");
		return responseDto;

	}

	/**
	 * 找回密码
	 * 
	 * @author gc
	 * @date 2015-6-23
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto updataPassword(UserDto userDto) throws Exception {
		/** step1:空异常处理. */
		if (userDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断用户手机号和新密码一级确认密码不能为空 */
		if (userDto.getPhone() == null || userDto.getPhone().equals("") || !Util.isNotEmpty(userDto.getPassword()) || !Util.isNotEmpty(userDto.getConfirmPass())) {
			throw new BusinessException("用户手机号、密码、确认密码不能为空");
		}

		/** step3. 判断密码是否加密 */
		if (userDto.getPassword().length() != 32 || userDto.getConfirmPass().length() != 32) {
			throw new BusinessException("密码加密方式错误");
		}

		/** step4. 判断密码和确认密码是否一致 */
		if (!userDto.getPassword().equals(userDto.getConfirmPass())) {
			throw new BusinessException("密码和确认密码不一致");
		}

		/** step5. 执行修改 */
		userDto.setPassword(MD5.getMD5Code(userDto.getPassword()));
		userDao.updataPass(userDto);

		/** step6. 返回结果 */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("密码更新成功");
		return responseDto;

	}

	/**
	 * 重置密码
	 * 
	 * @author gc
	 * @date 2015-6-23
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto resetPassword(UserDto userDto) throws Exception {
		/** step1:空异常处理. */
		if (userDto == null) {
			throw new BusinessException(Constants.DATA_ERROR);
		}

		/** step2. 判断用户ID和新密码一级确认密码不能为空 */
		if (!Util.isNotEmpty(userDto.getPassword()) || !Util.isNotEmpty(userDto.getConfirmPass()) || !Util.isNotEmpty(userDto.getNewPassword())) {
			throw new BusinessException("用户手机号、旧密码、新密码、确认密码不能为空");
		}

		/** step3. 获得用户ID */
		TokenDto token = CacheContainer.getToken(userDto.getToken());
		if (token == null) {
			throw new BusinessException("未登录");
		}
		userDto.setId(token.getUserDto().getId());

		/** step4. 判断密码是否加密 */
		if (userDto.getPassword().length() != 32 || userDto.getConfirmPass().length() != 32 || userDto.getNewPassword().length() != 32) {
			throw new BusinessException("密码加密方式错误");
		}

		/** step5. 根据用户ID得到用户对象 */
		UserDto user = userDao.queryUserById(userDto);

		if (user == null) {
			throw new BusinessException("用户不存在");
		}
		System.out.println(userDto.getPassword());
		System.out.println(MD5.getMD5Code(userDto.getPassword()));
		System.out.println(user.getPassword());
		if (!MD5.getMD5Code(userDto.getPassword()).equals(user.getPassword())) {
			throw new BusinessException("旧密码输入错误");
		}

		/** step6. 判断密码和确认密码是否一致 */
		if (!userDto.getNewPassword().equals(userDto.getConfirmPass())) {
			throw new BusinessException("密码和确认密码不一致");
		}

		/** step7. 执行修改 */
		userDto.setPassword(MD5.getMD5Code(userDto.getNewPassword()));
		userDao.updataPassById(userDto);

		/** step8. 返回结果 */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("密码重置成功");
		return responseDto;
	}

	/**
	 * 验证短信验证码是否正确
	 * 
	 * @author gc
	 * @date 2015-6-23
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseDto checkCodeYON(UserDto userDto) throws Exception {
		/** step1:校验短信验证码. */
		String verifyResult = new SmsVerifyKit(SysCfg.getString("messageAuthentication.appkey"), userDto.getPhone(), SysCfg.getString("messageAuthentication.zone"), userDto.getVerificationCode()).checkcode();
		JsonObject jsonObject = verifyResult != null ? Constants.jsonParser.parse(verifyResult).getAsJsonObject() : null;
		if (!(jsonObject != null && jsonObject.has("status") && jsonObject.get("status").getAsInt() == 200)) {
			throw new BusinessException("手机验证码不对");
		}

		/** step2. 返回结果 */
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResultDesc("验证码正确");
		return responseDto;
	}

}