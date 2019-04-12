/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package web.router;

import api.Api;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import utils.NetworkUtil;
import web.ApiRouter;

/**
 * 微信登录认证
 * @author tangyue
 * @version $Id: AuthRouter.java, v 0.1 2019-01-21 13:46 tangyue Exp $$
 */
@Slf4j
public final class AuthRouter extends ApiRouter {

    public static Router create(){

        return new AuthRouter().router;
    }

    private AuthRouter(){
        this.router.post("/auth").handler(this::auth);
    }

    private void auth(RoutingContext context) {
        String code = context.request().getParam("code");
        log.info("auth weixin code: {}", code);
        NetworkUtil.asyncPostJson(Api.OPENID_API, resp -> {
            
            log.info("response: {}", resp);
        });
    }
}
