package com.jiuyi.vggle.service.order;

import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.order.OrderDto;

/**
 * 
 * @author gc
 * @date 2015-4-23
 */
public interface OrderService {
	/**
	 * 1.添加订单
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public ResponseDto addOrder(OrderDto orderDto) throws Exception;

	/**
	 * 2.根据用户ID获得用户订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public ResponseDto queryOrderDtoByUserId(OrderDto orderDto) throws Exception;


	/**
	 * 3.确认收货
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public ResponseDto updateOrderStatus(OrderDto orderDto) throws Exception;

	/**
	 * 4.用户删除（隐藏）订单（修改订单删除状态）
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public ResponseDto updateOrderDeleteStatus(OrderDto orderDto) throws Exception;

	/**
	 * 5.用户退款申请
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public ResponseDto updateOrderRefundStatus(OrderDto orderDto) throws Exception;

	/**
	 * 6.根据订单号以及用户ID得到相关的商品付款信息
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-28
	 */
	public ResponseDto queryOrderNews(OrderDto orderDto) throws Exception;

	/**
	 * 7.根据评论状态得到订单
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-28
	 */
	public ResponseDto queryOrderByDiscuss(OrderDto orderDto) throws Exception;

	/**
	 * 8.立即购买提交订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @date 2015-7-13
	 * @author gc
	 * 
	 */
	public ResponseDto atoncePayOrder(OrderDto orderDto) throws Exception;
}
