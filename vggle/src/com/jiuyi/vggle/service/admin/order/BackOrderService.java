package com.jiuyi.vggle.service.admin.order;

import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.order.OrderDto;


public interface BackOrderService {
	/**
	 * 1.订单 状态 查询订单
	 *
	 * @return
	 * @throws Exception
	 * @date 2015-6-29
	 * @author gc
	 */
	public ResponseDto queryOrderByCompanyIdOrderStatus(OrderDto orderDto) throws Exception;

	/**
	 * 2.订单 编号 查询订单
	 *
	 * @return
	 * @throws Exception
	 * @date 2015-6-29
	 * @author gc
	 */
	public ResponseDto queryOrderByCompanyIdOrderId(OrderDto orderDto) throws Exception;

	/**
	 * 3.评论状态 查询订单
	 *
	 * @return
	 * @throws Exception
	 * @date 2015-6-29
	 * @author gc
	 */
	public ResponseDto queryOrderByCompanyIdDiscuss(OrderDto orderDto) throws Exception;

	/**
	 * 4.删除状态——查询订单
	 *
	 * @return
	 * @throws Exception
	 * @date 2015-6-29
	 * @author gc
	 */
	public ResponseDto queryOrderByCompanyIdDelete(OrderDto orderDto) throws Exception;

	/**
	 * 5.根据付款日期查询订单
	 *
	 * @param orderDto
	 * @return
	 * @throws Exception
	 */
	public ResponseDto queryOrderByCompanyIdPayDate(OrderDto orderDto) throws Exception;

	/**
	 * 6.修改订单（确认发货）
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-7
	 */
	public ResponseDto alterOrderStatusByCompanyId(OrderDto orderDto) throws Exception;

	/**
	 * 7.删除订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @DATE 2015-7-17
	 * @author gc
	 */
	public ResponseDto deleteOrderByCompanyId(OrderDto orderDto) throws Exception;

}
