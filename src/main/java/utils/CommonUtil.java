/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package utils;

import com.github.binarywang.wxpay.config.WxPayConfig;

import java.util.HashSet;
import java.util.Set;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import me.chanjar.weixin.mp.util.json.WxMpGsonBuilder;

/**
 *
 * @author tangyue
 * @version $Id: CommonUtil.java, v 0.1 2018-02-28 15:06 tangyue Exp $$
 */
public final class CommonUtil {

    private CommonUtil(){

        throw new RuntimeException("");
    }

    public static String JDBC_URL;
    public static String JDBC_USER;
    public static String JDBC_PSWD;
    public static String JDBC_DRIVER;

    private static Vertx vertx;
    private static Context vertxContext;


    private static WxPayConfig wxPayConfig;

    public static Vertx vertx() {
        return vertx;
    }

    public static Context vertxContext() {
        return vertxContext;
    }

    public static void init(Context vertxContext) {
        CommonUtil.vertx = vertxContext.owner();
        CommonUtil.vertxContext = vertxContext;
        JsonObject config = vertxContext.config();
        JDBC_URL = config.getString("jdbcUrl", "jdbc:mysql://127.0.0.1:3306/dish?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false");
        JDBC_USER = config.getString("jdbcUser", "root");
        JDBC_PSWD = config.getString("jdbcPassword", "123456");
        JDBC_DRIVER = config.getString("jdbcDriver", "com.mysql.cj.jdbc.Driver");

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

    /**
     * 微信公众号授权URL，scope=snsapi_userinfo，可用于获取用户信息
     */
    public final static String OAUTH_INFO_API = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo#wechat_redirect";

    /**
     * 微信公众号授权URL，scope=snsapi_base，只能用于获取用户OpenID
     */
    public final static String OAUTH_BASE_API = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base#wechat_redirect";

    /**
     * 微信公众号获取OpenID的API地址
     */
    public static final String OPENID_API = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * 微信公众号获取用户信息的API地址
     */
    public static final String USERINFO_API = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";


    /**
     * 转换成json
     * @param obj
     * @return
     */
    public static String toJson(Object obj){

        return WxMpGsonBuilder.create().toJson(obj);
    }

    public static Set<String> getAllowedHeaders(){

        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        return allowHeaders;
    }

    public static Set<HttpMethod> getAllowedMethods(){

        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);
        return allowMethods;
    }
}
