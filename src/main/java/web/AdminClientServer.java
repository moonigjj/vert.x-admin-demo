package web; /**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */

import db.HikariCPManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import lombok.extern.slf4j.Slf4j;
import utils.CodeEnum;
import utils.CommonUtil;
import web.router.DeskRouter;
import web.router.DishRouter;
import web.router.OrderRouter;
import web.router.sys.DeptRouter;
import web.router.sys.MenuRouter;
import web.router.sys.OperationRouter;
import web.router.sys.OrgRouter;
import web.router.sys.PermissionRouter;
import web.router.sys.RoleRouter;
import web.router.sys.UserRouter;

/**
 * 客户端程序
 * @author tangyue
 * @version $Id: server.ClientServer.java, v 0.1 2018-03-05 13:51 tangyue Exp $$
 */
@Slf4j
public class AdminClientServer extends AbstractVerticle {

    private HttpServer server;

    @Override
    public void start() {

        initComponents();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create())
                .handler(CorsHandler.create("*")
                        .allowedHeaders(CommonUtil.getAllowedHeaders())
                        .allowedMethods(CommonUtil.getAllowedMethods()));

        router.mountSubRouter("/desk", DeskRouter.create());
        router.mountSubRouter("/dish", DishRouter.create());
        router.mountSubRouter("/order", OrderRouter.create());

        router.mountSubRouter("/dept", DeptRouter.create());
        router.mountSubRouter("/menu", MenuRouter.create());
        router.mountSubRouter("/operation", OperationRouter.create());
        router.mountSubRouter("/org", OrgRouter.create());
        router.mountSubRouter("/permission", PermissionRouter.create());
        router.mountSubRouter("/role", RoleRouter.create());
        router.mountSubRouter("/user", UserRouter.create());

        router.route().failureHandler(f -> {
            log.error("request error: {}", f.failure());
            f.response().setStatusCode(CodeEnum.SYS_ERROR.getCode()).end(CodeEnum.SYS_ERROR.getMessage());
        });
        Integer port = config().getInteger("serverPort", 8086);
        this.server = vertx.createHttpServer();
        this.server.requestHandler(router :: accept).listen(port);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        HikariCPManager.close();
        this.server.close(res -> log.info("HTTP服务器关闭" + (res.succeeded()?"成功":"失败")));
    }

    // 初始化
    private void initComponents(){
        CommonUtil.init(context);
        HikariCPManager.init();
    }



}
