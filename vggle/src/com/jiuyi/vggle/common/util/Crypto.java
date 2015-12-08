/**
 * MD5加密算法
 */
package com.jiuyi.vggle.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;



/**
 * <p>Title: MD5加密算法</p>
 * <p>Description: 商户不需要进行修改</p>
 * <p>西安银博科技发展公司 2009. All rights reserved.</p>
 */
public class Crypto {

	private static final Logger log = Logger.getLogger(Crypto.class);
	/**
	 * 计算消息摘要
	 * 计算结果长度：SHA-1 40byte, MD5 32byte, SHA-256 64byte
	 * @param strSrc 计算摘要的源字符串
	 * @param encName 摘要算法: "SHA-1"  "MD5"  "SHA-256", 默认为"SHA-1"
	 * @return 正确返回摘要值,错误返回null
	 */
	public static String GetMessageDigest(String strSrc, String encName) {
		MessageDigest md = null;
		String strDes = null;
		final String ALGO_DEFAULT = "SHA-1";
		//final String ALGO_MD5 = "MD5";
		//final String ALGO_SHA256 = "SHA-256";

		byte[] bt = strSrc.getBytes();
		try {
			if (StringUtils.isEmpty(encName)) {
				encName = ALGO_DEFAULT;
			}
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			strDes = bytes2Hex(md.digest()); //to HexString
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strDes;
	}

	/**
	 * 将字节数组转为HEX字符串(16进制串)
	 * @param bts 要转换的字节数组
	 * @return 转换后的HEX串
	 */
	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
	
	/**
	 * 对数据进行签名
	 * @param strSrc
	 * @return
	 */
	public String signData(String strSrc) {
		return null;
	}

	/**
	 * 验证签名
	 * @param strSign 签名
	 * @param strSrc  原始数据
	 * @return Boolean
	 */
	public Boolean verfiySign(String strSign, String strSrc) {
		return null;
	}

	/**
	 * 设置加解密配置文件
	 * @param filePath
	 */
	public void setConfig(String filePath) {
	}
	
	/**
	 * 测试函数
	 * @param argc
	 */
	public static void main(String[] argc) {
		System.out
				.print(Crypto
						.GetMessageDigest(
								"merchantid=1162&merorderid=4139683877777&amountsum=1&subject=1162000&currencytype=01&autojump=1&waittime=5&merurl=http://www.51791.com&informmer=1&informurl=http://113.204.51.36:51107/vggle/RecivePay&confirm=1&merbank=ICBC&tradetype=0&bankInput=1&interface=5.00&bankcardtype=01&pdtdetailurl=http://115.29.33.165:80&merkey=nNH470Apn1muN5",
								"MD5"));
		log.info("");
	}

	
//	/**
//	 * 功能：MD5加密
//	 * @param strSrc 加密的源字符串
//	 * @return 加密串 长度32位
//	 */
//	public static String GetMessageDigest(String strSrc) {
//		MessageDigest md = null;
//		String strDes = null;
//		final String ALGO_MD5 = "MD5";
//
//		byte[] bt = strSrc.getBytes();
//		try {
//			md = MessageDigest.getInstance(ALGO_MD5);
//			md.update(bt);
//			strDes = bytes2Hex(md.digest());
//		} catch (NoSuchAlgorithmException e) {
//			throw new IllegalStateException(
//				"系统不支持的MD5算法！");
//		}
//		return strDes;
//	}
//
//	/**
//	 * 将字节数组转为HEX字符串(16进制串)
//	 * @param bts 要转换的字节数组
//	 * @return 转换后的HEX串
//	 */
//	public static String bytes2Hex(byte[] bts) {
//		String des = "";
//		String tmp = null;
//		for (int i = 0; i < bts.length; i++) {
//			tmp = (Integer.toHexString(bts[i] & 0xFF));
//			if (tmp.length() == 1) {
//				des += "0";
//			}
//			des += tmp;
//		}
//		return des;
//	}

}
