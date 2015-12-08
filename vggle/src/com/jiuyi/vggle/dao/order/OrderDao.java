package com.jiuyi.vggle.dao.order;

import java.util.List;

import com.jiuyi.vggle.dto.order.OrderDto;

/**
 * 
 * @author gaochao
 * @date 2015-5-27
 */
public interface OrderDao {
	/**
	 * 1.添加订单
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public void addOrderDto(OrderDto orderDto) throws Exception;
	
	/**
	 * 2.根据用户ID获得用户订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public List<OrderDto> queryOrderDtoByUserId(OrderDto orderDto) throws Exception;

	/**
	 * 3.根据用户ID和订单状态查询用户订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public List<OrderDto> queryOrderDtoByUserIdAndOrderStatus(OrderDto orderDto) throws Exception;

	/**
	 * 4.查询所有订单
	 * 
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public List<OrderDto> queryAllOrderDto() throws Exception;

	/**
	 * 5.修改收货订单状态
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public void updateOrderReciveStatus(OrderDto orderDto) throws Exception;

	/**
	 * 6.修改发货时间和订单状态
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public void updateOrderShipmentsTimeAndStatus(OrderDto orderDto) throws Exception;

	/**
	 * 7.修改评论状态
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public void updateOrderDiscussStatus(OrderDto orderDto) throws Exception;
	
	/**
	 * 8.用户删除（隐藏）订单（修改订单删除状态）
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public void updateOrderDeleteStatus(OrderDto orderDto) throws Exception;

	/**
	 * 9.修改退款状态
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public void updateOrderRefundStatus(OrderDto orderDto) throws Exception;

	/**
	 * 10.添加订单消费记录
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public void addHis(OrderDto orderDto) throws Exception;

	/**
	 * 11.根据订单编号查询订单
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-28
	 */
	public List<OrderDto> queryOrderByOrderNo(OrderDto orderDto) throws Exception;

	/**
	 * 12.付款后 根据订单编号修改订单状态
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-28
	 */
	public void updateOrderStatusByOrderNo(OrderDto orderDto) throws Exception;

	/**
	 * 13.根据订单Id查询订单
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-28
	 */
	public OrderDto queryOrderByOrderId(OrderDto orderDto) throws Exception;

	/**
	 * 14.根据评价状态得到评价状态
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-28
	 */
	public List<OrderDto> queryOrderByNoDiscuss(OrderDto orderDto) throws Exception;

	/**
	 * 15.管理员根据订单状态查询订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-29
	 */
	public List<OrderDto> queryOrderByStatus(OrderDto orderDto) throws Exception;

	/**
	 * 16.查询所有订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-29
	 */
	public List<OrderDto> queryAllOrder(OrderDto orderDto) throws Exception;

	/**
	 * 17.通过评论 查询所有订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-29
	 */
	public List<OrderDto> queryAllOrderByDisuss(OrderDto orderDto) throws Exception;

	/**
	 * 18.通过删除状态 查询所有订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-29
	 */
	public List<OrderDto> queryAllOrderByDelete(OrderDto orderDto) throws Exception;

	/**
	 * 19.获得当前日期之前某个时间段订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-29
	 */
	public List<OrderDto> queryAllOrderByDate(OrderDto orderDto) throws Exception;

	/**
	 * 20.修改订单编号
	 * 
	 * @param orderDto
	 * @throws Exception
	 */
	public void updataOrderNoById(OrderDto orderDto) throws Exception;
	/** ==================================== */
	/** 后台订单使用接口 */
	/** ==================================== */

	/**
	 * 20.查询企业所有订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @date 2015-7-16
	 * @author gc
	 */
	public List<OrderDto> queryAllOrderByCompanyId(OrderDto orderDto) throws Exception;

	/**
	 * 21.根据订单状态查询企业订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @date 2015-7-16
	 * @author gc
	 */
	public List<OrderDto> queryOrderByCompanyIdAndOrderStatus(OrderDto orderDto) throws Exception;

	/**
	 * 22.根据订单ID查询企业订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-
	 */
	public List<OrderDto> queryOrderByCompanyIdAndOrderId(OrderDto orderDto) throws Exception;

	/**
	 * 23.根据评论状态查询企业订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 */
	public List<OrderDto> queryOrderByCompanyIdAndDiscussStatus(OrderDto orderDto) throws Exception;

	/**
	 * 24.根据删除状态查询企业订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 */
	public List<OrderDto> queryOrderByCompanyIdAndDeleteStatus(OrderDto orderDto) throws Exception;

	/**
	 * 25.根据订单过往时间段查询企业订单
	 * 
	 * @param orderDto
	 * @return
	 * @throws Exception
	 */
	public List<OrderDto> queryOrderByCompanyIdAndPayTime(OrderDto orderDto) throws Exception;

	/**
	 * 26.商家删除订单
	 * 
	 * @param orderDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-7-17
	 */
	public void deleteOrderByCompanyId(OrderDto orderDto) throws Exception;
}
