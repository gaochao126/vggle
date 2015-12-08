package com.jiuyi.vggle.common.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author zhb
 * @date 2015年3月22日
 */
public class Util {
	private final static Random random = new Random();

	public static boolean isNotEmpty(String str) {
		if (str == null || "".equals(str)) {
			return false;
		} else {
			return true;
		}
	}

	public static String getUniqueSn() {
		String id = System.currentTimeMillis() + "";
		return "9" + id.substring(4, 13) + random.nextInt(9) + random.nextInt(9) + random.nextInt(9) + random.nextInt(9) + random.nextInt(9);
	}

	public static String getCoinPass() {
		return getUniqueSn() + random.nextInt(9) + random.nextInt(9) + random.nextInt(9);
	}


	/**
	 * 创建时间
	 * 
	 * @return
	 */
	public static Date getDateTime() {

		return new Date();
	}

	/**
	 * 获得有效期
	 * 
	 * @return
	 */
	public static Date validityTime() {
		Calendar curr = Calendar.getInstance();
		curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) + 1);
		Date date = curr.getTime();
		return date;
	}

	public static String getVerificationCode() {
		return random.nextInt(9) + random.nextInt(9) + random.nextInt(9) + random.nextInt(9) + random.nextInt(9) + random.nextInt(9) + "";
	}

	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 电话号码验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	/**
	 * Description：小数位处理
	 * 
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static String formatNumber(String number, int decimals) {
		try {
			Float.parseFloat(number);
		} catch (Exception e) {
			return null;
		}

		if (number == null || decimals < 0) {
			return null;
		}

		if (decimals > 0 && number.indexOf(".") != -1) {
			if (number.split("\\.")[1].length() < decimals) {
				for (int i = 0; i < decimals - number.split("\\.")[1].length(); i++) {
					number += "0";
				}
				System.out.println(number + "---");
				return number;
			}

			if (number.split("\\.")[1].length() > decimals) {
				number += number.split("\\.")[0] + "." + number.split("\\.")[1].substring(0, decimals);
				System.out.println(number + "+++");
				return number;
			}
		}

		if (decimals > 0 && number.indexOf(".") == -1) {
			number += ".";
			for (int i = 0; i < decimals; i++) {
				number += "0";
			}
			return number;
		}

		if (decimals == 0 && number.indexOf(".") != -1) {
			return number.split("\\.")[0];
		}

		if (decimals == 0 && number.indexOf(".") == -1) {
			return number;
		}

		return null;
	}

	/**
	 * 保留两位小数,四舍五入
	 */
	public static String Rounding(Double num) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		return nf.format(num);
	}

	/**
	 * 去除List中重复元素，并对衣服尺码排序0-> 尺码为M,L,XL || 1->尺码为数字
	 * 
	 * @param list
	 * @param code
	 * @return
	 */
	public static List<String> ListToSet(List<String> list, int code) {

		List<String> newList = new ArrayList<String>();
		if (code == 0) {
			Set<String> set = new LinkedHashSet<String>();
			set.addAll(list);
			list.clear();
			list.addAll(set);

			if (list.contains("XXS") || list.contains("xxs")) {
				newList.add("XXS");
			}
			if (list.contains("XS") || list.contains("xs")) {
				newList.add("XS");
			}
			if (list.contains("S") || list.contains("s")) {
				newList.add("S");
			}
			if (list.contains("M") || list.contains("m")) {
				newList.add("M");
			}
			if (list.contains("L") || list.contains("l")) {
				newList.add("L");
			}
			if (list.contains("XL") || list.contains("xl")) {
				newList.add("XL");
			}
			if (list.contains("XXL") || list.contains("xxl")) {
				newList.add("XXL");
			}
			if (list.contains("XXXL") || list.contains("xxxl")) {
				newList.add("XXXL");
			}
		}

		if (code == 1) {
			Set<String> set = new LinkedHashSet<String>();
			set.addAll(list);
			list.clear();
			list.addAll(set);
			newList = list;
		}

		return newList;
	}

}