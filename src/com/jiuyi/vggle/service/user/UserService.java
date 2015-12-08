package com.jiuyi.vggle.service.user;

import java.util.List;

import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.code.CodeDto;
import com.jiuyi.vggle.dto.user.UserDto;

/**
 * @description 用户业务层接口
 * @author zhb
 * @createTime 2015年5月21日
 */
public interface UserService {
    /**
     * @description 获取短信验证码
     * @param userDto
     * @return
     * @throws Exception
     */
    public ResponseDto getVerifyCode(UserDto userDto) throws Exception;

    /**
     * @description 注册
     * @param userDto
     * @return
     * @throws Exception
     */
    public ResponseDto register(UserDto userDto) throws Exception;

    /**
     * @description 登录
     * @param userDto
     * @return
     * @throws Exception
     */
    public ResponseDto signIn(UserDto userDto) throws Exception;

    /**
     * @description 登出
     * @param userDto
     * @return
     * @throws Exception
     */
    public ResponseDto signOut(UserDto userDto) throws Exception;

    /**
     * @description 查询用户信息
     * @param userDto
     * @return
     * @throws Exception
     */
    public ResponseDto queryUserInfo(UserDto userDto) throws Exception;

    /**
     * @description 编辑用户信息
     * @param userDto
     * @return
     * @throws Exception
     */
    public ResponseDto editUserInfo(UserDto userDto) throws Exception;

	/**
	 * @description 验证手机号是否存在
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto checkPhone(UserDto userDto) throws Exception;

	/**
	 * @description 修改钱包密码
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto editWalletPass(UserDto userDto) throws Exception;

	/**
	 * @description 获取验证码
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto queryCode(CodeDto codeDto) throws Exception;

	/**
	 * @description 校验验证码
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto CheckCode(CodeDto codeDto) throws Exception;

	/**
	 * @description 查询验证码到内存中
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public List<CodeDto> codeList() throws Exception;

	/**
	 * @description 查询用户是否设置钱包密码
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto queryUserByWalletPass(UserDto userDto) throws Exception;

	/**
	 * @description 判断钱包密码是否正确
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto checkWalletPass(UserDto userDto) throws Exception;

	/**
	 * 找回密码
	 * 
	 * @author gc
	 * @date 2015-6-23
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto updataPassword(UserDto userDto) throws Exception;

	/**
	 * 重置密码
	 * 
	 * @author gc
	 * @date 2015-6-23
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto resetPassword(UserDto userDto) throws Exception;

	/**
	 * 验证短信验证码是否正确
	 * 
	 * @author gc
	 * @date 2015-6-23
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto checkCodeYON(UserDto userDto) throws Exception;

}