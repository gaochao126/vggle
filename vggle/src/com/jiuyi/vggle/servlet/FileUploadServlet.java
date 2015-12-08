package com.jiuyi.vggle.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyi.vggle.common.dict.CacheContainer;
import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.util.Enumerate;
import com.jiuyi.vggle.common.util.SysCfg;
import com.jiuyi.vggle.common.util.Util;
import com.jiuyi.vggle.dao.commodity.CommodityDao;
import com.jiuyi.vggle.dao.commodity.image.ImageDao;
import com.jiuyi.vggle.dao.commodity.size.SizeDao;
import com.jiuyi.vggle.dao.user.UserDao;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.TokenDto;
import com.jiuyi.vggle.dto.commodity.image.ClothImg;
import com.jiuyi.vggle.dto.commodity.image.FoodImg;
import com.jiuyi.vggle.dto.commodity.params.ClothParams;
import com.jiuyi.vggle.dto.commodity.params.FoodParams;
import com.jiuyi.vggle.dto.user.UserDto;
import com.jspsmart.upload.File;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.SmartUpload;

/**
 * @description 文件上传
 * @author zhb
 * @createTime 2015年5月26日
 */
public class FileUploadServlet extends HttpServlet {
    private static final long serialVersionUID = -7020454770786546065L;
    private final static Logger logger = Logger.getLogger(FileUploadServlet.class);

	@Autowired
	private ImageDao imageDao = Constants.applicationContext.getBean(ImageDao.class);

	@Autowired
	private CommodityDao commodityDao = Constants.applicationContext.getBean(CommodityDao.class);

	@Autowired
	private SizeDao sizeDao = Constants.applicationContext.getBean(SizeDao.class);

	@Autowired
	private UserDao userDao = Constants.applicationContext.getBean(UserDao.class);

	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = null;
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json;charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");

			String goodId = req.getParameter("goodId");// 商品ID
			String code = req.getParameter("code");// 图片种类
			String imgtype = req.getParameter("imgtype");// 图片类型
			String colorName = req.getParameter("colorName");// 颜色名称

			// 登录验证
			ResponseDto responseDto = new ResponseDto();
			String token = req.getParameter("token");

			logger.info("FileUploadServlet.--token--" + token);
			logger.info("FileUploadServlet.--goodId--" + goodId);
			logger.info("FileUploadServlet.--code--" + code);
			logger.info("FileUploadServlet.--imgtype--" + imgtype);
			logger.info("FileUploadServlet.--colorName--" + colorName);

			logger.info("FileUploadServlet.==开始判断token");

			TokenDto tokenDto = Util.isNotEmpty(token) ? CacheContainer.getToken(token) : null;
			if (tokenDto != null && tokenDto.getUserDto() != null) {
				tokenDto.setUpdateTime(System.currentTimeMillis());
			} else {
				responseDto.setResultCode(2);
				responseDto.setResultDesc("未登录");
				out = resp.getWriter();
				out.print(Constants.gson.toJson(responseDto));
				logger.info("FileUploadServlet.service#response data:" + Constants.gson.toJson(responseDto));
				out.flush();
				out.close();
				return;
			}
			
			logger.info("FileUploadServlet.==判断完成  -");
			
			// 新建一个SmartUpload对象
			SmartUpload su = new SmartUpload();

			PageContext pageContext = JspFactory.getDefaultFactory().getPageContext(this, req, resp, "", true, 8192, true);
			logger.info("FileUploadServlet.--开始设定上传文件相关参数");

			// 上传初始化
			su.initialize(pageContext);

			// 限制每个上传文件上传限制
			su.setMaxFileSize(SysCfg.getInt("fileUpload.maxFileSize"));

			// 限制总上传文件的长度
			su.setTotalMaxFileSize(SysCfg.getInt("fileUpload.totalMaxFileSize"));

			// 设定允许上传的文件
			su.setAllowedFilesList(SysCfg.getString("fileUpload.allowedFilesList"));

			// 设定禁止上传的文件
			su.setDeniedFilesList(SysCfg.getString("fileUpload.deniedFilesList"));

			logger.info("FileUploadServlet.--设定好上传文件相关参数");
			// 上传文件
			su.upload();

			logger.info("FileUploadServlet.--执行完su.upload()方法");
			Map<String, Object> dataMap = new HashMap<String, Object>();

			// 存储路径
			String filePath = null;
			// 声明一个变量存储图片ID
			String imageId = null;
			// 读取路径
			String readPath = null;

			/** 根据code类型选择上传类型、1、上传头像；2、上传绿色食品图片；3、上传服装鞋帽图片 ----> 确定图片路径 */
			if ("1".equals(code)) {
				filePath = Enumerate.UPLOAD_HEAD;
			} else if ("2".equals(code)) {
				filePath = Enumerate.UPLOAD_FOOD;
			} else if ("3".equals(code)) {
				filePath = Enumerate.UPLOAD_CLOTH;
			}

			logger.info("FileUploadServlet.-- 判断code,值为： --" + code);

			/** 获得文件集合. */
			Files files = su.getFiles();
			if (files == null || files.getCount() == 0) {
				responseDto.setResultDesc("上件文件不能为空");
				responseDto.setResultCode(1);
				out = resp.getWriter();
				out.print(Constants.gson.toJson(responseDto));
				logger.info("FileUploadServlet.service#response data:" + Constants.gson.toJson(responseDto));
				out.flush();
				out.close();
				return;
			}

			logger.info("FileUploadServlet.-- 判断完文件是否为空 --");

			for (int i = 0; i < files.getCount(); i++) {
				File file = files.getFile(i);
				String fileName = null;
				UserDto user = tokenDto.getUserDto();

				/** 头像类 */
				if ("1".equals(code)) {
					fileName = "head_" + user.getId() + "." + file.getFileExt();
					readPath = Enumerate.USER_HEAD + fileName;

					UserDto u = tokenDto.getUserDto();
					logger.info("FileUploadServlet.-- 根据token得到用户对象，用户ID是" + u.getId());
					u.setHeadPortrait(fileName);

					// 操作数据库，修改前端用户头像
					userDao.editHead(u);
					logger.info("FileUploadServlet.-- code为1时 fileName--" + fileName);

				}

				/** 绿色食品类 */
				else if ("2".equals(code)) {
					fileName = "food_" + user.getId() + "_" + Util.getUniqueSn() + "." + file.getFileExt();
					readPath = Enumerate.FOOD_IMG_SRC + fileName;
					logger.info("FileUploadServlet.-- code为2时 fileName--" + fileName);

					FoodImg foodImg = new FoodImg();
					FoodParams food = new FoodParams();

					/** 判断图片类型. */
					if (imgtype.equals("1")) {
						logger.info("FileUploadServlet.-- code为2时,imgtype=1 fileName--" + fileName);
						food.setIndexSrc(fileName);
						// 如果没有商品ID,
						if (!Util.isNotEmpty(goodId)) {
							goodId = Util.getUniqueSn();
							food.setFoodId(goodId);
							food.setType(1);
							commodityDao.addFood(food);
						} else {
							food.setFoodId(goodId);
							// 查询是否有对应商品ID的商品
							FoodParams qryFood = commodityDao.queryFoodById(food);
							if (qryFood == null || qryFood.equals("")) {
								food.setType(1);
								commodityDao.addFood(food);
							} else {
								commodityDao.updateFoodIndexSrc(food);
							}
						}
					}

					if (imgtype.equals("2")) {
						if (!Util.isNotEmpty(goodId)) {
							goodId = Util.getUniqueSn();
							food.setFoodId(goodId);
							food.setType(1);
							commodityDao.addFood(food);
						}
						logger.info("FileUploadServlet.-- code为2时,imgtype=2 fileName--" + fileName);
						foodImg.setImageType(2);
					}

					if (imgtype.equals("3")) {
						if (!Util.isNotEmpty(goodId)) {
							goodId = Util.getUniqueSn();
							food.setFoodId(goodId);
							food.setType(1);
							commodityDao.addFood(food);
						}
						logger.info("FileUploadServlet.-- code为2时,imgtype=3 fileName--" + fileName);
						foodImg.setImageType(3);
					}

					/** 执行添加方法. */
					imageId = Util.getUniqueSn();
					foodImg.setImageId(imageId);
					foodImg.setFoodId(goodId);
					foodImg.setImageUrl(fileName);
					imageDao.addFoodImg(foodImg);
				}

				/** 服装鞋帽类 */
				else if ("3".equals(code)) {
					fileName = "cloth_" + user.getId() + "_" + Util.getUniqueSn() + "." + file.getFileExt();
					readPath = Enumerate.CLOTH_IMG_SRC + fileName;
					logger.info("FileUploadServlet.-- code为3时 fileName--" + fileName);

					// 生成一个图片ID
					String imgId = Util.getUniqueSn();
					ClothParams cloth = new ClothParams();
					ClothImg clothImg = new ClothImg();
					
					/** 首页展示图片. */
					if (imgtype.equals("1")) {
						logger.info("FileUploadServlet.-- code为3时,imgtype=1 fileName--" + fileName);
						cloth.setClothImg(fileName);

						// 如果ID为空
						if (!Util.isNotEmpty(goodId)) {
							goodId = Util.getUniqueSn();
							cloth.setClothId(goodId);
							cloth.setType(2);
							commodityDao.addCloth(cloth);
						} else {
							cloth.setClothId(goodId);
							ClothParams qryCloth = commodityDao.queryClothByClothId(cloth);
							if (qryCloth == null || qryCloth.equals("")) {
								cloth.setType(2);
								commodityDao.addCloth(cloth);
							} else {
								commodityDao.updateClothImg(cloth);
							}
						}
					}

					/** 商品详情图片 */
					if (imgtype.equals("2")) {
						logger.info("FileUploadServlet.-- code为3时,imgtype=2 fileName--" + fileName);
						// 添加-->图片表
						clothImg.setImageType(2);
						clothImg.setColorName(colorName);
						clothImg.setImageId(imgId);

						if (!Util.isNotEmpty(goodId)) {
							goodId = Util.getUniqueSn();
							cloth.setClothId(goodId);
							cloth.setType(2);
							commodityDao.addCloth(cloth);
						}
						clothImg.setImageName(fileName);
						clothImg.setClothId(goodId);
						imageDao.addClothImg(clothImg);
					}

					if (imgtype.equals("3")) {
						logger.info("FileUploadServlet.-- code为3时,imgtype=3 fileName--" + fileName);
						clothImg.setImageType(3);
						if (!Util.isNotEmpty(goodId)) {
							goodId = Util.getUniqueSn();
							cloth.setClothId(goodId);
							cloth.setType(2);
							commodityDao.addCloth(cloth);
						}
						clothImg.setClothId(goodId);
						clothImg.setImageId(imgId);
						clothImg.setImageName(fileName);
						imageDao.addClothImg(clothImg);
					}
					imageId = imgId;
				}

				try {

					logger.info("FileUploadServlet--开始执行---file.saveAs()方法  filePath:" + filePath + ";fileName:" + fileName + "保存路径：" + filePath + fileName);

					file.saveAs(filePath + fileName, SmartUpload.SAVE_AUTO);

					logger.info("FileUploadServlet--执行完成，并开始进行list.add() 方法---file.saveAs()方法  filePath:" + filePath + ";fileName:" + fileName);
					dataMap.put("path", readPath);
					logger.info("FileUploadServlet--list.add执行完成");

				} catch (Exception e) {
					e.printStackTrace();
					ResponseDto repDto = new ResponseDto();
					repDto.setResultCode(1);
					repDto.setResultDesc("上传失败,执行file.saveAs()方法有错误");
					out = resp.getWriter();
					out.print(Constants.gson.toJson(repDto));
					logger.info("FileUploadServlet.service#response data:" + Constants.gson.toJson(repDto));
					out.flush();
					out.close();
				}

			}
			responseDto.setResultDesc("上传成功");
			dataMap.put("commodityId", goodId);
			if (code.equals("3") && imgtype.equals("2")) {
				dataMap.put("imageId", imageId);
			}
			responseDto.setDetail(dataMap);

			out = resp.getWriter();
			out.print(Constants.gson.toJson(responseDto));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDto responseDto = new ResponseDto();
			responseDto.setResultCode(1);
			responseDto.setResultDesc("上传失败");
			out = resp.getWriter();
			out.print(Constants.gson.toJson(responseDto));
			logger.info("FileUploadServlet.service#response data:" + Constants.gson.toJson(responseDto));
			out.flush();
			out.close();
		}
    }
}