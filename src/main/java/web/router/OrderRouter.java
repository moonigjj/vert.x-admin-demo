/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package web.router;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.log4j.Log4j2;
import service.OrderService;
import utils.CodeEnum;
import utils.CommonUtil;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: OrderRouter.java, v 0.1 2018-06-14 17:24 tangyue Exp $$
 */
@Log4j2
public class OrderRouter extends ApiRouter {

    private Vertx vertx = CommonUtil.vertx();

    private Router router;

    private OrderService orderService;

    public static Router create(){
        return new OrderRouter().router;
    }

    private OrderRouter(){

        this.router = Router.router(this.vertx);
        this.router.get("/list/:merchantId").handler(this::orderListPage);
        this.router.get("/info/:orderId").handler(this::dishFoodInfo);

        this.orderService = new OrderService();
    }

    /**
     * 餐桌分页列表
     * @param context
     */
    private void orderListPage(RoutingContext context){

        String merchantId = context.request().getParam("merchantId");
        log.info("order list page: {}", merchantId);
        String pageNum = context.request().getParam("pageNum");
        String pageSize = context.request().getParam("pageSize");
        if (StrUtil.isBlank(merchantId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            Integer page = StrUtil.isNumber(pageNum) ? Integer.parseInt(pageNum) : 1;
            Integer size = StrUtil.isNumber(pageSize) ? Integer.parseInt(pageSize) : 10;
            JsonObject jsonObject = new JsonObject().put("merchantId", merchantId)
                    .put("orderNum", context.request().getParam("orderNum"));
            this.orderService.orderListPage(jsonObject, page, size, resultHandlerNonEmpty(context));
        }
    }


    /**
     * 查看餐桌信息
     * @param context
     */
    private void dishFoodInfo(RoutingContext context){

        String orderId = context.request().getParam("orderId");
        if (StrUtil.isBlank(orderId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            this.orderService.orderInfo(orderId, resultHandlerNonEmpty(context));
        }
    }
}
