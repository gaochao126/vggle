package com.jiuyi.vggle.dto.admin.permission;

import java.io.Serializable;

import com.jiuyi.vggle.dto.BaseDto;

public class PermissionDetailDto extends BaseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8958103162623875927L;

	/** 权限ID. */
	private String permissionId;

	/** 权限描述. */
	private String permissionDesc;

	/** 权限动作. */
	private String action;

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionDesc() {
		return permissionDesc;
	}

	public void setPermissionDesc(String permissionDesc) {
		this.permissionDesc = permissionDesc;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
