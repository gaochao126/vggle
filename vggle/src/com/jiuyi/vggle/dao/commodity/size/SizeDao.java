package com.jiuyi.vggle.dao.commodity.size;

import java.util.List;

import com.jiuyi.vggle.dto.commodity.size.SizeDto;
import com.jiuyi.vggle.dto.commodity.sizesku.SsDto;

public interface SizeDao {
	/**
	 * 1.通过ID查询商品尺码
	 * 
	 * @param sizeDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-6
	 */
	public List<SizeDto> querySizeByClohtId(SizeDto sizeDto) throws Exception;

	/**
	 * 2.添加尺码库存
	 * 
	 * @param ssDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-29
	 */
	public void addSizeSku(SsDto ssDto) throws Exception;

	/**
	 * 3.查询商品对应的库存尺码对象集合
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 */
	public List<SsDto> querySsByCommodityId(SsDto SsDto) throws Exception;

	/**
	 * 4.通过颜色图片ID和尺码得到库存记录
	 * 
	 * @param SsDto
	 * @return
	 * @throws Exception
	 */
	public SsDto queryByImageIdAndSize(SsDto ssDto) throws Exception;

	/**
	 * 5.修改库存
	 * 
	 * @param ssDto
	 * @throws Exception
	 * @date2015-8-14
	 * @author gc
	 * 
	 */
	public void updataSkuByImageIdAmdSizeDown(SsDto ssDto) throws Exception;
}
