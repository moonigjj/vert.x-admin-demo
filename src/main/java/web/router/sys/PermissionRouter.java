/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package web.router.sys;

import java.util.Objects;

import entity.sys.Permission;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.sys.PermissionService;
import utils.CodeEnum;
import utils.StrUtil;
import utils.ValidationUtils;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: AuthRouter.java, v 0.1 2019-01-21 13:46 tangyue Exp $$
 */
@Slf4j
public final class PermissionRouter extends ApiRouter {

    private PermissionService permissionService;

    public static Router create(){

        return new PermissionRouter().router;
    }

    private PermissionRouter(){

        this.router.get("/list/:orgId").handler(this::permissionListPage);
        this.router.get("/info/:permissionId").handler(this::permissionInfo);
        this.router.post("/add").handler(this::addPermission);
        this.router.post("/edit").handler(this::editPermission);

        this.permissionService = new PermissionService();
    }

    /**
     * 权限分页列表
     * @param context
     */
    private void permissionListPage(RoutingContext context){
        String orgId = context.request().getParam("orgId");
        log.info("permission list page: {}", orgId);
        String pageNum = context.request().getParam("pageNum");
        String pageSize = context.request().getParam("pageSize");
        if (StrUtil.isBlank(orgId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            Integer page = StrUtil.isNumber(pageNum) ? Integer.parseInt(pageNum) : 1;
            Integer size = StrUtil.isNumber(pageSize) ? Integer.parseInt(pageSize) : 10;
            JsonObject jsonObject = new JsonObject().put("orgId", orgId)
                    .put("permissionNum", context.request().getParam("permissionNum"));
            this.permissionService.permissionListPage(jsonObject, page, size, resultHandlerNonEmpty(context));
        }
    }

    /**
     * 添加权限
     * @param context
     */
    private void addPermission(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("add permission info:{}", jsonObject);

        Permission permission = jsonObject.mapTo(Permission.class);
        String message = ValidationUtils.validate(permission);
        if (Objects.nonNull(message)){

            serviceUnavailable(context, message);
        } else {

            permissionService.addPermission(permission, context, resultVoidHandler(context));
        }
    }

    /**
     * 查看权限信息
     * @param context
     */
    private void permissionInfo(RoutingContext context){

        String permissionId = context.request().getParam("permissionId");
        if (StrUtil.isBlank(permissionId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            permissionService.permissionInfo(Long.parseLong(permissionId), resultHandlerNonEmpty(context));
        }
    }

    /**
     * 编辑权限信息
     * @param context
     */
    private void editPermission(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("edit permission info: {}", jsonObject);
        Long permissionId = jsonObject.getLong("permissionId");
        Long orgId = jsonObject.getLong("orgId");

        String permissionNum = jsonObject.getString("permissionNum");
        String url = jsonObject.getString("url");
        String remark = jsonObject.getString("remark");
        if (Objects.isNull(permissionId) || Objects.isNull(orgId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            Permission permission = new Permission();
            permission.setId(permissionId);

            permissionService.editPermission(permission, context, resultVoidHandler(context));
        }


    }
    
}
