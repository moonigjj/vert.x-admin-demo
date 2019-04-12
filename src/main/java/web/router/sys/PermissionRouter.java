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
 * @author tangyue
 * @version $Id: AuthRouter.java, v 0.1 2019-01-21 13:46 tangyue Exp $$
 */
@Slf4j
public final class PermissionRouter extends ApiRouter {

    private PermissionService permissionService;

    public static Router create() {

        return new PermissionRouter().router;
    }

    private PermissionRouter() {

        this.router.get("/list").handler(this::permissionListPage);
        this.router.get("/info/:permissionId").handler(this::permissionInfo);
        this.router.post("/add").handler(this::addPermission);
        this.router.post("/edit").handler(this::editPermission);

        this.permissionService = new PermissionService();
    }

    /**
     * 权限分页列表
     */
    private void permissionListPage(RoutingContext context) {
        String pageNum = context.request().getParam("pageNum");
        String pageSize = context.request().getParam("pageSize");

        Integer page = StrUtil.isNumber(pageNum) ? Integer.parseInt(pageNum) : 1;
        Integer size = StrUtil.isNumber(pageSize) ? Integer.parseInt(pageSize) : 10;
        JsonObject jsonObject = new JsonObject()
                .put("permissionName", context.request().getParam("permissionName"));
        this.permissionService.permissionListPage(jsonObject, page, size, resultHandlerNonEmpty(context));

    }

    /**
     * 添加权限
     */
    private void addPermission(RoutingContext context) {

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("add permission info:{}", jsonObject);

        Permission permission = jsonObject.mapTo(Permission.class);
        String message = ValidationUtils.validate(permission);
        if (Objects.nonNull(message)) {

            serviceUnavailable(context, message);
        } else {

            permissionService.addPermission(permission, context, resultVoidHandler(context));
        }
    }

    /**
     * 查看权限信息
     */
    private void permissionInfo(RoutingContext context) {

        String permissionId = context.request().getParam("permissionId");
        if (StrUtil.isBlank(permissionId)) {
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            permissionService.permissionInfo(Long.parseLong(permissionId), resultHandlerNonEmpty(context));
        }
    }

    /**
     * 编辑权限信息
     */
    private void editPermission(RoutingContext context) {

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("edit permission info: {}", jsonObject);

        Permission permission = jsonObject.mapTo(Permission.class);
        String message = ValidationUtils.validate(permission);
        if (Objects.isNull(message)) {

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            permissionService.editPermission(permission, context, resultVoidHandler(context));
        }


    }

}
