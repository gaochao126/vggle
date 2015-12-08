package com.jiuyi.vggle.service.admin.commodity;

import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.commodity.params.ClothParams;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;
import com.jiuyi.vggle.dto.commodity.sizesku.SsDto;

public interface BackCommodityService {

	/**
	 * 1.添加服装鞋帽类商品
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-1
	 */
	public ResponseDto insertCloth(ClothParams clothParams) throws Exception;

	/**
	 * 2.添加绿色食品类
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @date 2015-7-1
	 * @author gc
	 */
	public ResponseDto insertFood(FoodParams foodParams) throws Exception;

	/**
	 * 3.修改绿色食品
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @date 2015-7-1
	 * @author gc
	 */
	public ResponseDto updateCloth(ClothParams clothParams) throws Exception;

	/**
	 * 4.修改服装鞋帽类
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @date 2015-7-1
	 * @author gc
	 */
	public ResponseDto updateFood(FoodParams foodParams) throws Exception;

	/**
	 * 5.商家查询属于自己的商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-7
	 * 
	 */
	public ResponseDto queryCommodityByCompanyId(FoodParams foodParams) throws Exception;

	/**
	 * 6.商家查询属于自己的所有商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 * 
	 */
	public ResponseDto queryAllCommodityByCompanyId(FoodParams foodParams) throws Exception;

	/**
	 * 6.商家查询库存为空的商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 * 
	 */
	public ResponseDto queryCommodityByCompanyIdAndSkuNull(FoodParams foodParams) throws Exception;

	/**
	 * 7.商家按销量查询商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public ResponseDto queryCommodityByCompanyIdAndSale(FoodParams foodParams) throws Exception;

	/**
	 * 8.商家按库存查询商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public ResponseDto queryCommodityByCompanyIdAndSku(FoodParams foodParams) throws Exception;

	/**
	 * 9.商家按上架时间查询商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public ResponseDto queryCommodityByCompanyIdAndOnvggleTime(FoodParams foodParams) throws Exception;

	/**
	 * 10.添加商品尺码库存信息
	 * 
	 * @param ssDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-11
	 */
	public ResponseDto insertSizeAndSku(SsDto ssDto) throws Exception;
}
