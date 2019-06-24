/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package web.router.dish;

import java.util.Objects;

import entity.dish.Desk;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.dish.DeskService;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 * 餐桌
 * @author tangyue
 * @version $Id: DeskRouter.java, v 0.1 2018-06-11 14:32 tangyue Exp $$
 */
@Slf4j
public final class DeskRouter extends ApiRouter {


    private DeskService deskService;

    public static Router create(){
        return new DeskRouter().router;
    }

    private DeskRouter(){

        this.router.get("/list/:orgId").handler(this::deskListPage);
        this.router.get("/info/:deskId").handler(this::deskInfo);
        this.router.post("/add").handler(this::addDesk);
        this.router.post("/edit").handler(this::editDesk);
        this.router.post("/status").handler(this::updateDeskStatus);

        this.deskService = new DeskService();
    }

    /**
     * 餐桌分页列表
     * @param context
     */
    private void deskListPage(RoutingContext context){
        String orgId = context.request().getParam("orgId");
        log.info("desk list page: {}", orgId);
        String pageNum = context.request().getParam("pageNum");
        String pageSize = context.request().getParam("pageSize");
        if (StrUtil.isBlank(orgId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            Integer page = StrUtil.isNumber(pageNum) ? Integer.parseInt(pageNum) : 1;
            Integer size = StrUtil.isNumber(pageSize) ? Integer.parseInt(pageSize) : 10;
            JsonObject jsonObject = new JsonObject().put("orgId", orgId)
                    .put("deskNum", context.request().getParam("deskNum"));
            this.deskService.deskListPage(jsonObject, page, size, resultHandlerNonEmpty(context));
        }
    }

    /**
     * 添加餐桌
     * @param context
     */
    private void addDesk(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("add desk info:{}", jsonObject);

        Long orgId = jsonObject.getLong("orgId");
        String deskNum = jsonObject.getString("deskNum");
        if (Objects.isNull(orgId) || StrUtil.isBlank(deskNum)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            Desk desk = jsonObject.mapTo(Desk.class);
            deskService.addDesk(desk, context, resultVoidHandler(context));
        }
    }

    /**
     * 查看餐桌信息
     * @param context
     */
    private void deskInfo(RoutingContext context){

        String deskId = context.request().getParam("deskId");
        if (StrUtil.isBlank(deskId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            deskService.deskInfo(deskId, resultHandlerNonEmpty(context));
        }
    }

    /**
     * 编辑餐桌信息
     * @param context
     */
    private void editDesk(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("edit desk info: {}", jsonObject);
        Long deskId = jsonObject.getLong("deskId");
        Long orgId = jsonObject.getLong("orgId");

        String deskNum = jsonObject.getString("deskNum");
        String url = jsonObject.getString("url");
        String remark = jsonObject.getString("remark");
        if (Objects.isNull(deskId) || Objects.isNull(orgId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            Desk desk = new Desk();
            desk.setId(deskId);
            desk.setOrgId(orgId);
            desk.setDeskNum(deskNum);
            desk.setUrl(url);
            desk.setRemark(remark);
            deskService.editDesk(desk, context, resultVoidHandler(context));
        }


    }

    /**
     * 更新餐桌状态
     * @param context
     */
    private void updateDeskStatus(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("update desk status: {}", jsonObject);
        Integer status = jsonObject.getInteger("status");
        Integer deskId = jsonObject.getInteger("deskId");
        if (Objects.isNull(status) || Objects.isNull(deskId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            deskService.updateDeskStatus(status, deskId, resultVoidHandler(context));
        }
    }
}
