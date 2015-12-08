package com.jiuyi.vggle.service.commodity;

import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;
import com.jiuyi.vggle.dto.commodity.sizesku.SsDto;

public interface CommodityService {
	/**
	 * 1.通过主分类查询商品
	 * 
	 * @author gc
	 * @date 2015-5-25
	 * @return
	 * @throws Exception
	 */
	public ResponseDto queryFoodParamsAll(FoodParams foodParams) throws Exception;

	/**
	 * 2.通过主分类和二级分类查询商品 eg: 10,五谷杂粮; 11,油盐酱醋; 12,干货; 13,水果蔬菜.
	 * 
	 * @author gc
	 * @date 2015-5-25
	 * @return
	 * @throws Exception
	 */
	public ResponseDto queryCommodBySecondId(FoodParams foodParams) throws Exception;

	/**
	 * 3.通过商品名字、拼音、
	 * 
	 * @author gc
	 * @date 2015-5-25
	 * @return
	 * @throws Exception
	 */
	public ResponseDto queryCommodByCommodName(FoodParams foodParams) throws Exception;

	/**
	 * 4.获得首页商品
	 * 
	 * @author gc
	 * @date 2015-5-25
	 * @return
	 * @throws Exception
	 */
	public ResponseDto queryCommodIndex(FoodParams foodParams) throws Exception;

	/**
	 * 5.获得商品详情
	 * 
	 * @author gc
	 * @date 2015-5-25
	 * @return
	 * @throws Exception
	 */
	public ResponseDto queryCommodDetail(FoodParams foodParams) throws Exception;

	/**
	 * 6.获得精品推荐
	 * 
	 * @author gc
	 * @date 2015-6-17
	 * @return
	 * @throws Exception
	 */
	public ResponseDto commodityShow(FoodParams foodParams) throws Exception;

	/**
	 * 7.通过颜色图片ID和尺码得到库存
	 * 
	 * @param ssDto
	 * @return
	 * @throws Exception
	 * @date 2015-8-13
	 * @author gc
	 */
	public ResponseDto querySkuByImageIdAndSize(SsDto ssDto) throws Exception;
}
