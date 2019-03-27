/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package web.router.sys;

import java.util.Objects;

import entity.sys.Operation;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.sys.OperationService;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: AuthRouter.java, v 0.1 2019-01-21 13:46 tangyue Exp $$
 */
@Slf4j
public final class OperationRouter extends ApiRouter {

    private OperationService operationService;

    public static Router create(){

        return new OperationRouter().router;
    }

    private OperationRouter(){

        this.router.get("/list/:orgId").handler(this::operationListPage);
        this.router.get("/info/:operationId").handler(this::operationInfo);
        this.router.post("/add").handler(this::addOperation);
        this.router.post("/edit").handler(this::editOperation);

        this.operationService = new OperationService();
    }

    /**
     * 操作分页列表
     * @param context
     */
    private void operationListPage(RoutingContext context){
        String orgId = context.request().getParam("orgId");
        log.info("operation list page: {}", orgId);
        String pageNum = context.request().getParam("pageNum");
        String pageSize = context.request().getParam("pageSize");
        if (StrUtil.isBlank(orgId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            Integer page = StrUtil.isNumber(pageNum) ? Integer.parseInt(pageNum) : 1;
            Integer size = StrUtil.isNumber(pageSize) ? Integer.parseInt(pageSize) : 10;
            JsonObject jsonObject = new JsonObject().put("orgId", orgId)
                    .put("operationNum", context.request().getParam("operationNum"));
            this.operationService.operationListPage(jsonObject, page, size, resultHandlerNonEmpty(context));
        }
    }

    /**
     * 添加操作
     * @param context
     */
    private void addOperation(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("add operation info:{}", jsonObject);

        Long orgId = jsonObject.getLong("orgId");
        String operationNum = jsonObject.getString("operationNum");
        if (Objects.isNull(orgId) || StrUtil.isBlank(operationNum)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            Operation operation = jsonObject.mapTo(Operation.class);
            operationService.addOperation(operation, context, resultVoidHandler(context));
        }
    }

    /**
     * 查看操作信息
     * @param context
     */
    private void operationInfo(RoutingContext context){

        String operationId = context.request().getParam("operationId");
        if (StrUtil.isBlank(operationId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            operationService.operationInfo(Long.parseLong(operationId), resultHandlerNonEmpty(context));
        }
    }

    /**
     * 编辑操作信息
     * @param context
     */
    private void editOperation(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("edit operation info: {}", jsonObject);
        Long operationId = jsonObject.getLong("operationId");
        Long orgId = jsonObject.getLong("orgId");

        String operationNum = jsonObject.getString("operationNum");
        String url = jsonObject.getString("url");
        String remark = jsonObject.getString("remark");
        if (Objects.isNull(operationId) || Objects.isNull(orgId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            Operation operation = new Operation();
            operation.setId(operationId);

            operationService.editOperation(operation, context, resultVoidHandler(context));
        }


    }
}
