package com.jiuyi.vggle.service.shopcar;

import java.util.List;

import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.shopcar.ShopCarDto;

/**
 * 
 * @author gc
 * @date 2015-5-26
 * 
 */
public interface ShopcarService {
	/**
	 * 1. 用户添加购物车
	 * 
	 * @param shopCarDto
	 * @author gc
	 * @date 2015-5-26
	 */
	public ResponseDto addShop(ShopCarDto shopCarDto) throws Exception;

	/**
	 * 2.通过用户ID查询用户购物车对象
	 * 
	 * @param userId
	 * @return
	 * 
	 * @author gc
	 * @date 2015-5-26
	 */
	public ResponseDto queryShopCarByUserId(ShopCarDto shopCarDto) throws Exception;

	/**
	 * 3.购物车添加数量
	 * 
	 * @param userId
	 * @return
	 * 
	 * @author gc
	 * @date 2015-5-26
	 */
	public ResponseDto upShopCar(ShopCarDto shopCarDto) throws Exception;

	/**
	 * 4.购物车减少数量
	 * 
	 * @param userId
	 * @return
	 * @author gc
	 * @date 2015-5-26
	 */
	public ResponseDto downShopCar(ShopCarDto shopCarDto) throws Exception;

	/**
	 * 5.查询购物车数量
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-26
	 */
	public ResponseDto queryBuyCount(ShopCarDto shopCarDto) throws Exception;

	/**
	 * 6.删除购物车
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @author gc
	 * @date 2015-5-26
	 */
	public ResponseDto deleteCar(ShopCarDto shopCarDto) throws Exception;


	/**
	 * 7.通过购物车ID得到购物车对象
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 * 
	 */
	public ShopCarDto queryShopCarById(ShopCarDto shopCarDto) throws Exception;

	/**
	 * 8.取消购物车中选中的商品
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 * 
	 */
	public ResponseDto updateCarStatus(ShopCarDto shopCarDto) throws Exception;


	/**
	 * 9.获得购物车选中购买的商品
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 * 
	 */
	public ResponseDto queryCarBuyOk(ShopCarDto shopCarDto) throws Exception;

	/**
	 * 10.服务器端使用 。获得购物车选中购买的商品
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 * 
	 */
	public List<ShopCarDto> queryCarBuyOkServ(ShopCarDto shopCarDto) throws Exception;

	/**
	 * 11.全选按钮
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 * 
	 */
	public ResponseDto checkAll(ShopCarDto shopCarDto) throws Exception;

	/**
	 * 12.删除购物车服务器使用
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @author gc
	 * @date 2015-5-26
	 */
	public void deleteCarServ(ShopCarDto shopCarDto) throws Exception;

	/**
	 * 13.立即购买按钮操作
	 * 
	 * @param shopCarDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-2
	 */
	public ResponseDto atOncePay(ShopCarDto shopCarDto) throws Exception;
}
