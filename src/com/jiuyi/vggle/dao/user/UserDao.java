package com.jiuyi.vggle.dao.user;

import java.util.List;

import com.jiuyi.vggle.dto.user.UserDto;

/**
 * @description 用户dao层接口
 * @author zhb
 * @createTime 2015年5月21日
 */
public interface UserDao {
    /**
     * @description 根据手机号获取用户
     * @param userDto
     * @return
     * @throws Exception
     */
    public UserDto queryUserByPhone(UserDto userDto) throws Exception;

    /**
     * @description 根据id获取用户
     * @param userDto
     * @return
     * @throws Exception
     */
    public UserDto queryUserById(UserDto userDto) throws Exception;

    /**
     * @description 根据的机号获取用户
     * @param userDto
     * @return
     * @throws Exception
     */
    public void register(UserDto userDto) throws Exception;

    /**
     * @description 编辑用户信息
     * @param userDto
     * @return
     * @throws Exception
     */
    public void editUserInfo(UserDto userDto) throws Exception;

	/**
	 * @description 修改钱包密码
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	public void updateUserWalletPass(UserDto userDto) throws Exception;

	/**
	 * 找回密码
	 * 
	 * @author gc
	 * @date 2015-6-23
	 * @param userDto
	 * @throws Exception
	 */
	public void updataPass(UserDto userDto) throws Exception;

	/**
	 * 重置密码
	 * 
	 * @author gc
	 * @date 2015-6-23
	 * @param userDto
	 * @throws Exception
	 */
	public void updataPassById(UserDto userDto) throws Exception;

	/**
	 * 查询所有用户
	 * 
	 * @param userDto
	 * @return
	 * @throws Exception
	 * @date 2015-6-29
	 * @author gc
	 */
	public List<UserDto> queryAllUser(UserDto userDto) throws Exception;

	/**
	 * 更新头像
	 * 
	 * @param userDto
	 * @throws Exception
	 */
	public void editHead(UserDto userDto) throws Exception;
}