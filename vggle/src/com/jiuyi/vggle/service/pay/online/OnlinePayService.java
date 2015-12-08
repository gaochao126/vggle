package com.jiuyi.vggle.service.pay.online;

import com.jiuyi.vggle.common.pay.OnlineReqBean;
import com.jiuyi.vggle.dto.ResponseDto;

public interface OnlinePayService {

	/**
	 * 1.浏览器网银支付
	 * 
	 * @param onlineReqBean
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-11
	 */
	public ResponseDto FromBrowToPay(OnlineReqBean onlineReqBean) throws Exception;

	/**
	 * 2.将用户支付信息添加到内存中
	 * 
	 * @param onlineReqBean
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-12
	 */
	public void addPayMess(OnlineReqBean onlineReqBean) throws Exception;

	/**
	 * 3.查询内存中的用户信息
	 * 
	 * @param onlineReqBean
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-12
	 */
	public OnlineReqBean queryPayMess(OnlineReqBean onlineReqBean) throws Exception;
}
