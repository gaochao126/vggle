package com.jiuyi.vggle.service.pay.quick;


import com.jiuyi.vggle.common.pay.ShortcutReqBean;
import com.jiuyi.vggle.dto.ResponseDto;
import com.jiuyi.vggle.dto.pay.BankDto;

/**
 * @description 快捷支付业务层接口
 * @author gc
 * @createTime 2015年5月27日
 */
public interface ShortcutPayService {
    /**
	 * 1.查询快捷支付列表
	 * 
	 * @description 查询银行列表
	 * @param bankDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public ResponseDto queryQuickBanks(BankDto bankDto) throws Exception;

	/**
	 * 2.查询网银支付列表
	 * 
	 * @description 查询银行列表
	 * @param bankDto
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public ResponseDto queryOnlineBanks(BankDto bankDto) throws Exception;

    /**
	 * 3.@description 消费交易QP0001 (商户->快捷支付平台)
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    public ResponseDto QP0001(ShortcutReqBean shortcutReqBean) throws Exception;
    
    /**
	 * 4.@description 快捷支付手机动态鉴权QP0002(商户->快捷支付平台)
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
	public ResponseDto QP0002(ShortcutReqBean shortcutReqBean) throws Exception;
    
    /**
	 * 5.@description 关闭快捷支付QP0003（商户->快捷支付平台）
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    public ResponseDto QP0003(ShortcutReqBean shortcutReqBean) throws Exception;
    
    /**
	 * 6.@description 快捷支付客户卡信息查询QP0004(商户->快捷支付平台)
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    public ResponseDto QP0004(ShortcutReqBean shortcutReqBean) throws Exception;
    
    /**
	 * 7.@description 退货交易QP0005(商户->快捷支付平台，暂无)
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    public ResponseDto QP0005(ShortcutReqBean shortcutReqBean) throws Exception;
    
    /**
	 * 8.@description 快捷支付交易流水查询QP0006(商户->快捷支付平台)
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    public ResponseDto QP0006(ShortcutReqBean shortcutReqBean) throws Exception;
    
    /**
	 * 9.@description 快捷支付卡信息查询QP0007(商户->快捷支付平台)
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    public ResponseDto QP0007(ShortcutReqBean shortcutReqBean) throws Exception;
    
    /**
	 * 10.@description 一键支付交易QP0008 (商户->快捷支付平台)
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    public ResponseDto QP0008(ShortcutReqBean shortcutReqBean) throws Exception;
    
    /**
	 * 11.@description 一键支付限额查询QP0009 (商户->快捷支付平台)
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-5-27
	 */
    public ResponseDto QP0009(ShortcutReqBean shortcutReqBean) throws Exception;

	/**
	 * 12.使用五谷钱包支付
	 * 
	 * @param shortcutReqBean
	 * @return
	 * @throws Exception
	 * @author gc
	 * @date 2015-6-9
	 */
	public ResponseDto vggleWalletPay(ShortcutReqBean shortcutReqBean) throws Exception;
}