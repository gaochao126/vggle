package com.jiuyi.vggle.common.util;

/**
 * @description 服务端发起验证请求验证移动端(手机)发送的短信
 * @author zhb
 * @createTime 2015年4月21日
 */
public class SmsVerifyKit {

    private String appkey;
    private String phone;
    private String zone;
    private String code;

    /**
     * 
     * @param appkey
     *            应用KEY
     * @param phone
     *            电话号码 xxxxxxxxx
     * @param zone
     *            区号 86
     * @param code
     *            验证码 xx
     */
    public SmsVerifyKit(String appkey, String phone, String zone, String code) {
        super();
        this.appkey = appkey;
        this.phone = phone;
        this.zone = zone;
        this.code = code;
    }

    /**
     * 服务端发起验证请求验证移动端(手机)发送的短信
     * 
     * @return
     * @throws Exception
     */
    public String go() throws Exception {
        String address = "https://api.sms.mob.com/sms/verify";
        MobClient client = null;
        try {
            client = new MobClient(address);
            client.addParam("appkey", appkey).addParam("phone", phone).addParam("zone", zone).addParam("code", code);
            client.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            client.addRequestProperty("Accept", "application/json");
            String result = client.post();
            return result;
        } finally {
            if (client != null) {
                client.release();
            }
        }
    }

    /**
     * 服务端发验证服务端发送的短信
     * 
     * @return
     * @throws Exception
     */
    public String checkcode() throws Exception {

        String address = "https://api.sms.mob.com/sms/checkcode";
        MobClient client = null;
        try {
            client = new MobClient(address);
            client.addParam("appkey", appkey).addParam("phone", phone).addParam("zone", zone).addParam("code", code);
            client.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            client.addRequestProperty("Accept", "application/json");
            String result = client.post();
            return result;
        } finally {
            if (client != null) {
                client.release();
            }
        }
    }

    /**
     * 服务端发起发送短信请求
     * 
     * @return
     * @throws Exception
     */
    public String sendMsg() throws Exception {

        String address = "https://api.sms.mob.com/sms/sendmsg";
        MobClient client = null;
        try {
            client = new MobClient(address);
            client.addParam("appkey", appkey).addParam("phone", phone).addParam("zone", zone);
            client.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            client.addRequestProperty("Accept", "application/json");
            String result = client.post();
            return result;
        } finally {
            if (client != null) {
                client.release();
            }
        }
    }
}