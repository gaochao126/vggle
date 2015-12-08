package com.jiuyi.vggle.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jiuyi.vggle.common.dict.CacheContainer;
import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.util.ExportExcel;
import com.jiuyi.vggle.dao.coin.CoinDao;
import com.jiuyi.vggle.dto.coin.CoinDto;
import com.jiuyi.vggle.dto.user.UserDto;
import com.jiuyi.vggle.service.BusinessException;

public class OutputExcel extends HttpServlet {

	private CoinDao coinDao = Constants.applicationContext.getBean(CoinDao.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 7377730813108618142L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
			  
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 声明一个list用来装在礼品卡对象
		List<CoinDto> list = new ArrayList<CoinDto>();
		CoinDto NoCoin = new CoinDto();

		// 接收编号
		String[] no = request.getParameterValues("no");

		// 接收 token
		String token = request.getParameter("token");

		// 判断权是否登录
		UserDto userDto = CacheContainer.getToken(token).getUserDto();

		try {

			if (userDto == null) {
				throw new BusinessException("未登录");
			}

			for (int i = 0; i < no.length; i++) {
				// 查询coin对象
				CoinDto coinDto = new CoinDto();
				coinDto.setNo(Integer.parseInt(no[i]));

				NoCoin = coinDao.queryCoinByNo(coinDto);

				// 导出后将卡类型变为实体卡
				coinDto.setSource(1);
				coinDao.updateCoinSource(coinDto);
				list.add(NoCoin);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		String fileName = new SimpleDateFormat(" yyyy年MM月dd日  HH时 MM分  ss秒 ").format(new Date()) + "_礼品卡信息.xls";
		fileName = new String(fileName.getBytes("utf8"), "iso8859-1");
		response.reset();
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);// 指定下载的文件名
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		OutputStream output = response.getOutputStream();
		BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
			  
		// 定义单元格报头
		String worksheetTitle = new SimpleDateFormat(" yyyy年MM月dd日 ").format(new Date()) + "_导出礼品卡信息";
			  
		HSSFWorkbook wb = new HSSFWorkbook();

		// 创建单元格样式
		HSSFCellStyle cellStyleTitle = wb.createCellStyle();

		// 指定单元格居中对齐
		cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 指定单元格垂直居中对齐
		cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		// 指定当单元格内容显示不下时自动换行
		cellStyleTitle.setWrapText(true);

		// =========================================================

		HSSFCellStyle cellStyle = wb.createCellStyle();

		// 指定单元格居中对齐
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 指定单元格垂直居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		// 指定当单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);


		// ============================================

		// 设置单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeight((short) 350);
		cellStyleTitle.setFont(font);
		
		// 主题内容设置单元格字体
		HSSFFont fontNext = wb.createFont();
		fontNext.setFontHeight((short) 250);
		cellStyle.setFont(fontNext);
			  
		// 工作表名
		String No = "编号";
		String Id = "卡号";
		String Pass = "密码";
		String Amount = "金额";
		String Type = "类型";
		String Remark = "备注";
			  
		HSSFSheet sheet = wb.createSheet();
		sheet.setColumnWidth(0, 2500);
		sheet.setColumnWidth(1, 6500);
		sheet.setColumnWidth(2, 6500);
		sheet.setColumnWidth(3, 3500);
		sheet.setColumnWidth(4, 3500);
		sheet.setColumnWidth(5, 3500);

		ExportExcel exportExcel = new ExportExcel(wb, sheet);

		// 创建报表头部
		exportExcel.createNormalHead(worksheetTitle, 6);
		// 定义第一行
		HSSFRow row1 = sheet.createRow(1);
		HSSFCell cell1 = row1.createCell(0);

		// 第一行第一列
		cell1.setCellStyle(cellStyleTitle);
		cell1.setCellValue(new HSSFRichTextString(No));

		// 第一行第er列
		cell1 = row1.createCell(1);
		cell1.setCellStyle(cellStyleTitle);
		cell1.setCellValue(new HSSFRichTextString(Id));

		// 第一行第san列
		cell1 = row1.createCell(2);
		cell1.setCellStyle(cellStyleTitle);
		cell1.setCellValue(new HSSFRichTextString(Pass));

		// 第一行第si列
		cell1 = row1.createCell(3);
		cell1.setCellStyle(cellStyleTitle);
		cell1.setCellValue(new HSSFRichTextString(Amount));
			 
		// 第一行第wu列
		cell1 = row1.createCell(4);
		cell1.setCellStyle(cellStyleTitle);
		cell1.setCellValue(new HSSFRichTextString(Type));

		// 第一行第liu列
		cell1 = row1.createCell(5);
		cell1.setCellStyle(cellStyleTitle);
		cell1.setCellValue(new HSSFRichTextString(Remark));
			  
			        
		// 定义第二行
		HSSFRow row = sheet.createRow(2);
		HSSFCell cell = row.createCell(1);

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {

				row = sheet.createRow(i + 2);

				cell = row.createCell(0);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(list.get(i).getNo() + ""));

				cell = row.createCell(1);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(list.get(i).getCoinId()));

				cell = row.createCell(2);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(list.get(i).getCoinPass()));

				cell = row.createCell(3);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(list.get(i).getAmount() + ""));

				cell = row.createCell(4);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(list.get(i).getType() + ""));

				cell = row.createCell(5);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(""));
			}
		}

		try {
			bufferedOutPut.flush();
			wb.write(bufferedOutPut);
			bufferedOutPut.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Output   is   closed ");
		} finally {
			list.clear();
		}
	}
}

