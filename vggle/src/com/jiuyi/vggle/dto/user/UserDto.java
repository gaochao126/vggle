package com.jiuyi.vggle.dto.user;

import com.jiuyi.vggle.dto.BaseDto;

/**
 * @description 用户实体类
 * @author zhb
 * @createTime 2015年5月21日
 */
public class UserDto extends BaseDto {
    /** serialVersionUID. */
    private static final long serialVersionUID = 3691437631098098376L;

    /** 用户id. */
	// private Integer id;

	/** anotherId */
	private Integer anotherId;

    /** 电话. */
    private String phone;

    /** 昵称. */
    private String nickname;


    /** 真实姓名. */
    private String realName;

	/** 用户新密码. */
	private String newPassword;

    /** 密码. */
    private String password;

	/** 确认密码. */
	private String confirmPass;

	/** 钱包密码. */
	private String walletPass;

    /** 邮箱. */
    private String email;

    /** 头像. */
    private String headPortrait;

	/** 权限. */
	private Integer permissionGrade;

	/** 权限等级. */
	private Integer level;

	/** 用户拥有权限集合. */
	private String havePermission;

	/** 用户拥有权限数量. */
	private Integer permissionCount;

	/** 性别. */
	private String sex;

	/** 身份证号. */
	private String card;

	/** 公司标识. */
	private String companyId;

	/** 公司姓名. */
	private String companyName;

	/** 创建者ID. */
	private Integer createId;

	/** 管理员描述. */
	private String gradeInfo;

    /** 性别(1:男, 2:女). */
    private Integer gender;

    /** 短信验证码. */
    private String verificationCode;

	/** 验证码. */
	private String code;

	// @Override
	// public Integer getId() {
	// return id;
	// }
	//
	// @Override
	// public void setId(Integer id) {
	// this.id = id;
	// }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

	public String getWalletPass() {
		return walletPass;
	}

	public void setWalletPass(String walletPass) {
		this.walletPass = walletPass;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getConfirmPass() {
		return confirmPass;
	}

	public void setConfirmPass(String confirmPass) {
		this.confirmPass = confirmPass;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public Integer getPermissionGrade() {
		return permissionGrade;
	}

	public void setPermissionGrade(Integer permissionGrade) {
		this.permissionGrade = permissionGrade;
	}

	public String getHavePermission() {
		return havePermission;
	}

	public void setHavePermission(String havePermission) {
		this.havePermission = havePermission;
	}

	public Integer getPermissionCount() {
		return permissionCount;
	}

	public void setPermissionCount(Integer permissionCount) {
		this.permissionCount = permissionCount;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getGradeInfo() {
		return gradeInfo;
	}

	public void setGradeInfo(String gradeInfo) {
		this.gradeInfo = gradeInfo;
	}

	public Integer getAnotherId() {
		return anotherId;
	}

	public void setAnotherId(Integer anotherId) {
		this.anotherId = anotherId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}