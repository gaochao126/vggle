package com.jiuyi.vggle.dao.commodity;

import java.util.List;

import com.jiuyi.vggle.dto.commodity.params.ClothParams;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;

public interface CommodityDao {

	/** ============================================================== */
	/** Table: t_commodity_foods 绿色食品粮油干货系列 */
	/** ============================================================== */

	/**
	 * 1.通过主分类查询商品
	 * 
	 * @author gc
	 * @date 2015-5-23
	 * @return
	 * @throws Exception
	 */
	public List<FoodParams> queryFoodParamsAll(FoodParams foodParams) throws Exception;

	/**
	 * 2.通过主分类和二级分类查询商品 eg: 10,五谷杂粮; 11,油盐酱醋; 12,干货; 13,水果蔬菜.
	 * 
	 * @author gc
	 * @date 2015-5-23
	 * @return
	 * @throws Exception
	 */
	public List<FoodParams> queryCommodByFoodKind(FoodParams foodParams) throws Exception;

	/**
	 * 3.通过商品名字、拼音、
	 * 
	 * @author gc
	 * @date 2015-5-23
	 * @return
	 * @throws Exception
	 */
	public List<FoodParams> queryCommodByCommodName(FoodParams foodParams) throws Exception;

	/**
	 * 4.通过ID和商品类型得到商品详情
	 * 
	 * @author gc
	 * @date 2015-5-23
	 * @return
	 * @throws Exception
	 */
	public FoodParams queryFoodById(FoodParams foodParams) throws Exception;

	/**
	 * 5.修改绿色食品库存
	 * 
	 * @author gc
	 * @date 2015-6-8
	 * @return
	 * @throws Exception
	 */
	public void updataFoodSku(FoodParams foodParams) throws Exception;

	/**
	 * 6.获得绿色食品类推荐
	 * 
	 * @author gc
	 * @date 2015-6-8
	 * @return
	 * @throws Exception
	 */
	public List<FoodParams> queryShowFood(FoodParams foodParams) throws Exception;

	/**
	 * 7.添加绿色食品
	 * 
	 * @param foodParams
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-1
	 */
	public void addFood(FoodParams foodParams) throws Exception;

	/**
	 * 8.修改绿色食品
	 * 
	 * @param foodParams
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-1
	 */
	public void updateFood(FoodParams foodParams) throws Exception;

	/**
	 * 9.修改绿色食品销量
	 * 
	 * @param foodParams
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-1
	 */
	public void updateFoodSales(FoodParams foodParams) throws Exception;

	/**
	 * 10.更改商品首页展示图片
	 * 
	 * @param foodParams
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-7
	 */
	public void updateFoodIndexSrc(FoodParams foodParams) throws Exception;

	/**
	 * 11.商家查询属于自己的商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-7
	 */
	public List<FoodParams> queryFoodByCompanyId(FoodParams foodParams) throws Exception;

	/**
	 * 12.商家查询库存已经为0的商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public List<FoodParams> queryFoodByCompanyIdAndSku(FoodParams foodParams) throws Exception;

	/**
	 * 13.商家查询按销量升序的商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public List<FoodParams> queryFoodByCompanyIdAndSaleUp(FoodParams foodParams) throws Exception;

	/**
	 * 14.商家查询按销量降序的商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public List<FoodParams> queryFoodByCompanyIdAndSaleDown(FoodParams foodParams) throws Exception;

	/**
	 * 15.商家查询按库存升序的商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public List<FoodParams> queryFoodByCompanyIdAndSkuUp(FoodParams foodParams) throws Exception;

	/**
	 * 16.商家查询按库存降序的商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public List<FoodParams> queryFoodByCompanyIdAndSkuDown(FoodParams foodParams) throws Exception;

	/**
	 * 17.商家查询按库存升序的商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public List<FoodParams> queryFoodByCompanyIdAndOnvggleTimeUp(FoodParams foodParams) throws Exception;

	/**
	 * 18.商家查询按库存降序的商品
	 * 
	 * @param foodParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public List<FoodParams> queryFoodByCompanyIdAndOnvggleTimeDown(FoodParams foodParams) throws Exception;

	/** ============================================================== */
	/** Table: t_commodity_cloth_size 服装鞋帽配饰系列 */
	/** ============================================================== */

	/**
	 * 1.添加服装鞋帽配饰系列
	 * 
	 * @param clothParams
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-6
	 */
	public void addCloth(ClothParams clothParams) throws Exception;

	/**
	 * 2.查询所有服装鞋帽配饰商品
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-6
	 */
	public List<ClothParams> queryAllClothParams(ClothParams clothParams) throws Exception;

	/**
	 * 3.通过二级分类查询服装鞋帽系类产品
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-6
	 */
	public List<ClothParams> queryClothBySecondId(ClothParams clothParams) throws Exception;

	/**
	 * 4.通过关键字查询服装鞋帽系类产品
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-6
	 */
	public List<ClothParams> queryClothByKeyWord(ClothParams clothParams) throws Exception;

	/**
	 * 5.通过商品ID查询服装鞋帽配饰对象
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 */
	public ClothParams queryClothByClothId(ClothParams clothParams) throws Exception;

	/**
	 * 6.修改服装鞋帽配饰库存
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 */
	public void updataClothSku(ClothParams clothParams) throws Exception;

	/**
	 * 7.获得服装鞋帽类精品推荐
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 */
	public List<ClothParams> queryShowCloth(ClothParams clothParams) throws Exception;

	/**
	 * 8.修改服装鞋帽系列商品
	 * 
	 * @param clothParams
	 * @throws Exception
	 */
	public void updateCloth(ClothParams clothParams) throws Exception;

	/**
	 * 9.修改服装鞋帽销量
	 * 
	 * @param clothParams
	 * @throws Exception
	 */
	public void updateClothSales(ClothParams clothParams) throws Exception;

	/**
	 * 10.更改服装类商品图片名字
	 * 
	 * @param clothParams
	 * @throws Exception
	 */
	public void updateClothImg(ClothParams clothParams) throws Exception;

	/**
	 * 11.商家查询属于自己的商品
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-7
	 */
	public List<ClothParams> queryClothByCompanyId(ClothParams clothParams) throws Exception;

	/**
	 * 12.商家查询库存为空的商品
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public List<ClothParams> queryClothByCompanyIdAndSku(ClothParams clothParams) throws Exception;
	
	/**
	 * 13.商家按销量升序查询
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public List<ClothParams> queryClothByCompanyIdAndSaleUp(ClothParams clothParams) throws Exception;
	
	/**
	 * 14.商家按销量降序查询
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public List<ClothParams> queryClothByCompanyIdAndSaleDown(ClothParams clothParams) throws Exception;
	
	
	/**
	 * 15.商家上架时间升序查询
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public List<ClothParams> queryClothByCompanyIdAndOnvggleTimeUp(ClothParams clothParams) throws Exception;

	/**
	 * 16.商家按上架时间降序查询
	 * 
	 * @param clothParams
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-8-4
	 */
	public List<ClothParams> queryClothByCompanyIdAndOnvggleTimeDown(ClothParams clothParams) throws Exception;

}
