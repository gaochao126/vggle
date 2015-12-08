package com.jiuyi.vggle.dao.commodity.image;

import java.util.List;

import com.jiuyi.vggle.dto.commodity.image.ClothImg;
import com.jiuyi.vggle.dto.commodity.image.FoodImg;
import com.jiuyi.vggle.dto.commodity.params.ClothParams;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;

public interface ImageDao {

	/**
	 * 1.查询绿色食品粮油干货
	 * 
	 * @param foodImg
	 * @return
	 * @throws Exception
	 */
	public List<FoodImg> queryAllImageById(FoodParams foodParams) throws Exception;

	/**
	 * 2.查询服装鞋帽配饰
	 * 
	 * @param foodImg
	 * @return
	 * @throws Exception
	 */
	public List<ClothImg> queryAllImageByClothId(ClothParams clothParams) throws Exception;

	/**
	 * 3.根据服装ID查询服装图片对象
	 * 
	 * @param foodImg
	 * @return
	 * @throws Exception
	 */
	public ClothImg queryClothImgByImgId(ClothImg clothImg) throws Exception;

	/**
	 * 4.添加绿色食品类图片对象
	 * 
	 * @param foodImg
	 * @throws Exception
	 * @date 2015-7-7
	 * @author gc
	 */
	public void addFoodImg(FoodImg foodImg) throws Exception;

	/**
	 * 5.添加服装鞋帽类图片对象
	 * 
	 * @param ClothImg
	 * @throws Exception
	 * @date 2015-7-7
	 * @author gc
	 */
	public void addClothImg(ClothImg ClothImg) throws Exception;
}
