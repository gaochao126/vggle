package com.jiuyi.vggle.service.discuss;

import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.Discuss.DiscussDto;

public interface DiscussService {
	/**
	 * 1.添加评论
	 * 
	 * @author gc
	 * @date 2015-5-28
	 * @param discussDto
	 * @throws Exception
	 */
	public ResponseDto addDiscuss(DiscussDto discussDto) throws Exception;

	/**
	 * 2.查询商品评论
	 * 
	 * @author gc
	 * @date 2015-5-28
	 * @param discussDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto queryDiscussByCommodityId(DiscussDto discussDto) throws Exception;

	/**
	 * 3.查询用户评论
	 * 
	 * @author gc
	 * @date 2015-5-28
	 * @param discussDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto queryDiscussByUserId(DiscussDto discussDto) throws Exception;

	/**
	 * 4.设置评论状态
	 * 
	 * @author gc
	 * @date 2015-5-28
	 * @param discussDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto updateDiscussStatus(DiscussDto discussDto) throws Exception;
}
