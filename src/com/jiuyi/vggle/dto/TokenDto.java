package com.jiuyi.vggle.dto;

import java.io.Serializable;

import com.jiuyi.vggle.dto.user.UserDto;

/**
 * @description toke实体类
 * @author zhb
 * @createTime 2015年5月21日
 */
public class TokenDto implements Serializable {
    /** serialVersionUID. */
    private static final long serialVersionUID = 2297851842800437936L;

    private String token;

    private UserDto userDto;

    private long updateTime;

    public String getToken() {
        return token;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setToken(String token) {
        this.token = token;
    }
}