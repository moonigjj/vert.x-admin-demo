/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package utils;

import com.github.binarywang.wxpay.config.WxPayConfig;

import io.vertx.core.json.JsonObject;

/**
 *
 * @author tangyue
 * @version $Id: WechatUtil.java, v 0.1 2019-01-21 17:13 tangyue Exp $$
 */
public final class WechatUtil {

    private static WxPayConfig wxPayConfig;

    public static void init() {

        JsonObject config = CommonUtil.getConfig();
        //
        wxPayConfig = new WxPayConfig();
        // 微信公众号的appid
        wxPayConfig.setAppId(config.getString("appId"));
        // 微信支付商户号
        wxPayConfig.setMchId(config.getString("pay.mchId"));
        // 微信支付商户密钥
        wxPayConfig.setMchKey(config.getString("pay.mchKey"));
        // apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定
        wxPayConfig.setKeyPath(config.getString("pay.keyPath"));
    }

    public static WxPayConfig getWxPayConfig() {
        return wxPayConfig;
    }
}
