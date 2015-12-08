package com.jiuyi.vggle.dao.code;

import java.util.List;

import com.jiuyi.vggle.dto.code.CodeDto;

public interface CodeDao {

	/**
	 * 1.查询所有验证码
	 * 
	 * @param codeDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-9
	 */
	public List<CodeDto> queryAllCode(CodeDto codeDto) throws Exception;

	/**
	 * 1.查询所有验证码
	 * 
	 * @param codeDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-9
	 */
	public CodeDto queryCodeById(CodeDto codeDto) throws Exception;
}
