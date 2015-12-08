package com.jiuyi.vggle.dto.address;

import java.io.Serializable;

import com.jiuyi.vggle.dto.BaseDto;

/**
 * 用户地址表
 * 
 * @author gc
 * @date 2015-5-27
 */
public class AddressDto extends BaseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 133412945106977267L;

	/** 地址ID. */
	private String addrId;

	/** 用户ID. */
	private Integer userId;

	/** 省份. */
	private String province;

	/** 城市. */
	private String city;

	/** 县城. */
	private String area;

	/** 街道. */
	private String street;

	/** 邮编. */
	private Integer code;

	/** 收货人姓名. */
	private String personName;

	/** 收货人手机号. */
	private String personPhone;

	/** 地址状态. */
	private Integer addrStatus;

	public String getAddrId() {
		return addrId;
	}

	public void setAddrId(String addrId) {
		this.addrId = addrId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonPhone() {
		return personPhone;
	}

	public void setPersonPhone(String personPhone) {
		this.personPhone = personPhone;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getAddrStatus() {
		return addrStatus;
	}

	public void setAddrStatus(Integer addrStatus) {
		this.addrStatus = addrStatus;
	}

}
