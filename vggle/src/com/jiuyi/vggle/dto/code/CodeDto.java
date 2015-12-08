package com.jiuyi.vggle.dto.code;

import java.io.Serializable;

import com.jiuyi.vggle.dto.BaseDto;

public class CodeDto extends BaseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5946125149407400896L;

	private Integer codeId;

	private String codeValue;

	private String codeName;

	public Integer getCodeId() {
		return codeId;
	}

	public void setCodeId(Integer codeId) {
		this.codeId = codeId;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

}
