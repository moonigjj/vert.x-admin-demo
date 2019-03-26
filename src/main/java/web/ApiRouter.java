/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package web;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import utils.CodeEnum;
import utils.CommonUtil;
import web.model.ReturnModel;

/**
 *
 * @author tangyue
 * @version $Id: ApiRouter.java, v 0.1 2018-05-17 15:04 tangyue Exp $$
 */
public class ApiRouter {

    protected Vertx vertx = CommonUtil.vertx();

    protected Router router;

    protected ApiRouter() {
        this.router = Router.router(vertx);
    }


    /**
     * Validate if a user exists in the request scope.
     */
    protected void requireLogin(RoutingContext context, BiConsumer<RoutingContext, JsonObject> biHandler) {
        Optional<JsonObject> principal = Optional.ofNullable(context.request().getHeader("user-principal"))
                .map(JsonObject::new);
        if (principal.isPresent()) {
            biHandler.accept(context, principal.get());
        } else {
            context.response()
                    .setStatusCode(401)
                    .end(new JsonObject().put("message", "need_auth").encode());
        }
    }
    // helper result handler within a request context

    /**
     * This method generates handler for async methods in REST APIs.
     * Use the result directly and use given {@code converter} to convert result to string
     * as the response. The content type is JSON.
     *
     * @param context   routing context instance
     * @param converter a converter that converts result to a string
     * @param <T>       result type
     * @return generated handler
     */
    protected <T> Handler<AsyncResult<T>> resultHandler(RoutingContext context, Function<ReturnModel, String> converter) {
        return ar -> {
            if (ar.succeeded()) {
                T res = ar.result();

                if (res == null) {
                    serviceUnavailable(context, CodeEnum.SYS_REQUEST);
                } else {

                    ReturnModel returnModel = new ReturnModel();
                    returnModel.setSuc(true);
                    returnModel.setResult(res);
                    context.response()
                            .putHeader("content-type", "application/json")
                            .end(converter.apply(returnModel));
                }
            } else {
                serviceUnavailable(context, CodeEnum.SYS_ERROR);
                ar.cause().printStackTrace();
            }
        };
    }

    /**
     * This method generates handler for async methods in REST APIs.
     * The result requires non-empty. If empty, return <em>404 Not Found</em> status.
     * The content type is JSON.
     *
     * @param context routing context instance
     * @param <T>     result type
     * @return generated handler
     */
    protected <T> Handler<AsyncResult<T>> resultHandlerNonEmpty(RoutingContext context) {
        return ar -> {
            if (ar.succeeded()) {
                T res = ar.result();
                if (res == null) {
                    serviceUnavailable(context, CodeEnum.SYS_REQUEST);
                } else {
                    serviceAvailable(context, res);
                }
            } else {
                serviceUnavailable(context, CodeEnum.SYS_ERROR);
                ar.cause().printStackTrace();
            }
        };
    }

    /**
     * 无返回值
     * @param context
     * @return
     */
    protected Handler<AsyncResult<Void>> resultVoidHandler(RoutingContext context) {
        return ar -> {
            if (ar.succeeded()) {
                serviceAvailable(context, "");
            } else {
                serviceUnavailable(context, CodeEnum.SYS_ERROR);
                ar.cause().printStackTrace();
            }
        };
    }

    public static   <T> void serviceAvailable(RoutingContext context, T data) {
        ReturnModel returnModel = new ReturnModel();
        returnModel.setSuc(true);
        returnModel.setResult(data);
        context.response()
                .putHeader("content-type", "application/json")
                .end(Json.encodePrettily(returnModel));
    }


    public static void serviceUnavailable(RoutingContext context, CodeEnum cause) {

        ReturnModel returnModel = new ReturnModel();
        returnModel.setSuc(false);
        returnModel.setReason(cause.getMessage());
        context.response()
                .putHeader("content-type", "application/json")
                .end(Json.encodePrettily(returnModel));
    }

    public static void serviceUnavailable(RoutingContext context, String message) {
        serviceUnavailable(context, message);
    }
}
