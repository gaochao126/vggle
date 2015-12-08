package com.jiuyi.vggle.dao.discuss;

import java.util.List;

import com.jiuyi.vggle.dto.Discuss.DiscussDto;

public interface DiscussDao {
	/**
	 * 1.添加评论
	 * 
	 * @author gc
	 * @date 2015-5-28
	 * @param discussDto
	 * @throws Exception
	 */
	public void addDiscuss(DiscussDto discussDto) throws Exception;

	/**
	 * 2.查询商品评论
	 * 
	 * @author gc
	 * @date 2015-5-28
	 * @param discussDto
	 * @return
	 * @throws Exception
	 */
	public List<DiscussDto> queryDiscussByCommodityId(DiscussDto discussDto) throws Exception;

	/**
	 * 3.查询用户评论
	 * 
	 * @author gc
	 * @date 2015-5-28
	 * @param discussDto
	 * @return
	 * @throws Exception
	 */
	public List<DiscussDto> queryDiscussByUserId(DiscussDto discussDto) throws Exception;

	/**
	 * 4.设置评论状态
	 * 
	 * @author gc
	 * @date 2015-5-28
	 * @param discussDto
	 * @return
	 * @throws Exception
	 */
	public void updateDiscussStatus(DiscussDto discussDto) throws Exception;

}
