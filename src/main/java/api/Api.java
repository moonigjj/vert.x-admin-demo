/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package api;

/**
 *
 * @author tangyue
 * @version $Id: Api.java, v 0.1 2019-01-21 14:01 tangyue Exp $$
 */
public interface Api {

    /**
     * 微信公众号授权URL，scope=snsapi_userinfo，可用于获取用户信息
     */
    String OAUTH_INFO_API = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo#wechat_redirect";

    /**
     * 微信公众号授权URL，scope=snsapi_base，只能用于获取用户OpenID
     */
    String OAUTH_BASE_API = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base#wechat_redirect";

    /**
     * 微信公众号获取OpenID的API地址
     */
    String OPENID_API = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * 微信公众号获取用户信息的API地址
     */
    String USERINFO_API = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
}
