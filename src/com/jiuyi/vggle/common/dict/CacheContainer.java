package com.jiuyi.vggle.common.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;

import com.jiuyi.vggle.dto.TokenDto;
import com.jiuyi.vggle.dto.admin.permission.PermissionDetailDto;
import com.jiuyi.vggle.dto.user.UserDto;
import com.jiuyi.vggle.service.address.AddressService;
import com.jiuyi.vggle.service.admin.coin.BackCoinServer;
import com.jiuyi.vggle.service.admin.commodity.BackCommodityService;
import com.jiuyi.vggle.service.admin.order.BackOrderService;
import com.jiuyi.vggle.service.admin.permission.QueryPermissionService;
import com.jiuyi.vggle.service.admin.user.BackUserService;
import com.jiuyi.vggle.service.coin.CoinService;
import com.jiuyi.vggle.service.commodity.CommodityService;
import com.jiuyi.vggle.service.coupon.CouPonService;
import com.jiuyi.vggle.service.discuss.DiscussService;
import com.jiuyi.vggle.service.order.OrderService;
import com.jiuyi.vggle.service.pay.online.OnlinePayService;
import com.jiuyi.vggle.service.pay.quick.ShortcutPayService;
import com.jiuyi.vggle.service.shopcar.ShopcarService;
import com.jiuyi.vggle.service.user.UserService;


public class CacheContainer {

    /** 业务map. */
    public static Map<String, Class<?>> serviceMap;

    /** not authentication map. */
    public static Map<String, Object> notAuthMap;

    /** token map. */
    private static Map<String, TokenDto> tokenMap;

    public static void init() {
        serviceMap = new HashMap<String, Class<?>>();
        notAuthMap = new HashMap<String, Object>();
        tokenMap = new ConcurrentHashMap<String, TokenDto>();

		/** service interface. user 7 */
        serviceMap.put("getVerifyCode", UserService.class);
        serviceMap.put("register", UserService.class);
        serviceMap.put("signIn", UserService.class);
        serviceMap.put("signOut", UserService.class);
        serviceMap.put("queryUserInfo", UserService.class);
        serviceMap.put("editUserInfo", UserService.class);
		serviceMap.put("checkPhone", UserService.class);
		serviceMap.put("editWalletPass", UserService.class);
		serviceMap.put("queryCode", UserService.class);
		serviceMap.put("queryUserByWalletPass", UserService.class);
		serviceMap.put("checkWalletPass", UserService.class);
		serviceMap.put("CheckCode", UserService.class);
		serviceMap.put("updataPassword", UserService.class);
		serviceMap.put("checkCodeYON", UserService.class);
		serviceMap.put("resetPassword", UserService.class);

		/** service interface. coupon */
		serviceMap.put("queryCouPonByCouPonId", CouPonService.class);
		serviceMap.put("queryCouPonByUserId", CouPonService.class);
		serviceMap.put("deleteCouPon", CouPonService.class);

		/** service interface. Coin 6 */
		serviceMap.put("addCoin", CoinService.class);
		serviceMap.put("updateCoinPhone", CoinService.class);
		serviceMap.put("updateCoinAmountdown", CoinService.class);
		serviceMap.put("deleteCoin", CoinService.class);
		serviceMap.put("queryCoinByPhone", CoinService.class);
		serviceMap.put("gift", CoinService.class);

		/** service interface. Commodity 5 */
		serviceMap.put("queryFoodParamsAll", CommodityService.class);
		serviceMap.put("queryCommodBySecondId", CommodityService.class);
		serviceMap.put("queryCommodByCommodName", CommodityService.class);
		serviceMap.put("queryCommodIndex", CommodityService.class);
		serviceMap.put("commodityShow", CommodityService.class);
		serviceMap.put("queryCommodDetail", CommodityService.class);
		serviceMap.put("querySkuByImageIdAndSize", CommodityService.class);

		/** service interface. ShopCar 9 */
		serviceMap.put("addShop", ShopcarService.class);
		serviceMap.put("queryShopCarByUserId", ShopcarService.class);
		serviceMap.put("upShopCar", ShopcarService.class);
		serviceMap.put("downShopCar", ShopcarService.class);
		serviceMap.put("queryBuyCount", ShopcarService.class);
		serviceMap.put("deleteCar", ShopcarService.class);
		serviceMap.put("updateCarStatus", ShopcarService.class);
		serviceMap.put("queryCarBuyOk", ShopcarService.class);
		serviceMap.put("checkAll", ShopcarService.class);
		serviceMap.put("atOncePay", ShopcarService.class);

		/** service interface. Order 5 */
		serviceMap.put("addOrder", OrderService.class);
		serviceMap.put("queryOrderDtoByUserId", OrderService.class);
		serviceMap.put("updateOrderStatus", OrderService.class);
		serviceMap.put("updateOrderDeleteStatus", OrderService.class);
		serviceMap.put("updateOrderRefundStatus", OrderService.class);
		serviceMap.put("queryOrderNews", OrderService.class);
		serviceMap.put("queryOrderByDiscuss", OrderService.class);
		serviceMap.put("atoncePayOrder", OrderService.class);

		/** service interface. Pay 11 */
		serviceMap.put("queryQuickBanks", ShortcutPayService.class);
		serviceMap.put("queryOnlineBanks", ShortcutPayService.class);
		serviceMap.put("QP0001", ShortcutPayService.class);
		serviceMap.put("QP0002", ShortcutPayService.class);
		serviceMap.put("QP0003", ShortcutPayService.class);
		serviceMap.put("QP0004", ShortcutPayService.class);
		serviceMap.put("QP0005", ShortcutPayService.class);
		serviceMap.put("QP0006", ShortcutPayService.class);
		serviceMap.put("QP0007", ShortcutPayService.class);
		serviceMap.put("QP0008", ShortcutPayService.class);
		serviceMap.put("QP0009", ShortcutPayService.class);
		serviceMap.put("vggleWalletPay", ShortcutPayService.class);
		serviceMap.put("FromBrowToPay", OnlinePayService.class);

		/** service interface. Discuss 4 */
		serviceMap.put("addDiscuss", DiscussService.class);
		serviceMap.put("queryDiscussByCommodityId", DiscussService.class);
		serviceMap.put("queryDiscussByUserId", DiscussService.class);
		serviceMap.put("updateDiscussStatus", DiscussService.class);

		/** service interface. Address 4 */
		serviceMap.put("addAddress", AddressService.class);
		serviceMap.put("updateAddress", AddressService.class);
		serviceMap.put("deleteAddress", AddressService.class);
		serviceMap.put("updateAddrStatus", AddressService.class);
		serviceMap.put("queryAddrByUserId", AddressService.class);

        /** not authentication map. */
        notAuthMap.put("getVerifyCode", null);
        notAuthMap.put("register", null);
        notAuthMap.put("signIn", null);
		notAuthMap.put("checkPhone", null);
		notAuthMap.put("queryCode", null);
		notAuthMap.put("CheckCode", null);
		notAuthMap.put("checkCodeYON", null);
		notAuthMap.put("updataPassword", null);
		notAuthMap.put("querySkuByImageIdAndSize", null);

		/** ========================================= */
		/******************* 后台接口 ********************/
		/** ========================================= */

		/** service interface. back user */
		serviceMap.put("allUser", BackUserService.class);
		serviceMap.put("adminLogin", BackUserService.class);
		serviceMap.put("createAdmin", BackUserService.class);
		serviceMap.put("updateAdmin", BackUserService.class);
		serviceMap.put("alterAdminHavePermission", BackUserService.class);
		serviceMap.put("deleteAdmin", BackUserService.class);
		serviceMap.put("queryPermission", BackUserService.class);

		/** service interface. back order */
		serviceMap.put("queryOrderByCompanyIdOrderStatus", BackOrderService.class);
		serviceMap.put("queryOrderByCompanyIdOrderId", BackOrderService.class);
		serviceMap.put("queryOrderByCompanyIdDiscuss", BackOrderService.class);
		serviceMap.put("queryOrderByCompanyIdDelete", BackOrderService.class);
		serviceMap.put("queryOrderByCompanyIdPayDate", BackOrderService.class);
		serviceMap.put("alterOrderStatusByCompanyId", BackOrderService.class);
		serviceMap.put("deleteOrderByCompanyId", BackOrderService.class);

		/** service interface. back commodity */
		serviceMap.put("insertCloth", BackCommodityService.class);
		serviceMap.put("insertFood", BackCommodityService.class);
		serviceMap.put("updateCloth", BackCommodityService.class);
		serviceMap.put("updateFood", BackCommodityService.class);
		serviceMap.put("queryCommodityByCompanyId", BackCommodityService.class);
		serviceMap.put("queryAllCommodityByCompanyId", BackCommodityService.class);
		serviceMap.put("queryCommodityByCompanyIdAndSkuNull", BackCommodityService.class);
		serviceMap.put("queryCommodityByCompanyIdAndSale", BackCommodityService.class);
		serviceMap.put("queryCommodityByCompanyIdAndSku", BackCommodityService.class);
		serviceMap.put("queryCommodityByCompanyIdAndOnvggleTime", BackCommodityService.class);
		serviceMap.put("insertSizeAndSku", BackCommodityService.class);

		/** service interface. back permission */
		serviceMap.put("queryAllPermission", QueryPermissionService.class);
		serviceMap.put("Action", QueryPermissionService.class);

		/** service interface. back Coin */
		serviceMap.put("insertCoin", BackCoinServer.class);
		serviceMap.put("queryCoinByType", BackCoinServer.class);
		serviceMap.put("ActivaOne", BackCoinServer.class);
		serviceMap.put("ActivaSome", BackCoinServer.class);

		/** 浏览商品不需要登录. */
		notAuthMap.put("queryFoodParamsAll", null);
		notAuthMap.put("queryCommodBySecondId", null);
		notAuthMap.put("queryCommodByCommodName", null);
		notAuthMap.put("queryCommodIndex", null);
		notAuthMap.put("queryCommodDetail", null);
		notAuthMap.put("commodityShow", null);
		notAuthMap.put("adminLogin", null);
		notAuthMap.put("adminLogin", null);
	}

    /**
     * @description 获取token
     * @param token
     * @return
     */
    public static TokenDto getToken(String token) {
        return tokenMap.get(token);
    }

    /**
     * @description 存放token
     * @param token
     * @param tokenDto
     */
    public static void putToken(String token, TokenDto tokenDto) {
        tokenMap.put(token, tokenDto);
    }

    /**
     * @description 存放token
     * @param token
     * @param tokenDto
     */
    public static void removeToken(String token) {
        tokenMap.remove(token);
    }

	/**
	 * 查询用户的CMD
	 * 
	 * @param userDto
	 * @return
	 */
	public static List<String> user_cmd(UserDto userDto) {
		List<String> userCmd = new ArrayList<String>();
		try {
			userCmd = Constants.applicationContext.getBean(QueryPermissionService.class).Action(userDto);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userCmd;
	}

	/**
	 * 查询所有权限
	 * 
	 * @return
	 */
	public static List<PermissionDetailDto> allPermission() {
		List<PermissionDetailDto> permissions = new ArrayList<PermissionDetailDto>();
		PermissionDetailDto permissionDetailDto = new PermissionDetailDto();
		try {
			permissions = Constants.applicationContext.getBean(QueryPermissionService.class).queryAllPermission(permissionDetailDto);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return permissions;
	}
	
	
}