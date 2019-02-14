/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package web.router.sys;

import java.util.Objects;

import entity.sys.Role;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.sys.RoleService;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: AuthRouter.java, v 0.1 2019-01-21 13:46 tangyue Exp $$
 */
@Slf4j
public final class RoleRouter extends ApiRouter {

    private RoleService roleService;

    public static Router create(){

        return new RoleRouter().router;
    }

    private RoleRouter(){

        this.router.get("/list/:orgId").handler(this::roleListPage);
        this.router.get("/info/:roleId").handler(this::roleInfo);
        this.router.post("/add").handler(this::addRole);
        this.router.post("/edit").handler(this::editRole);
        this.router.post("/del").handler(this::updateRoleDel);

        this.roleService = new RoleService();
    }

    /**
     * 角色分页列表
     * @param context
     */
    private void roleListPage(RoutingContext context){
        String orgId = context.request().getParam("orgId");
        log.info("role list page: {}", orgId);
        String pageNum = context.request().getParam("pageNum");
        String pageSize = context.request().getParam("pageSize");
        if (StrUtil.isBlank(orgId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            Integer page = StrUtil.isNumber(pageNum) ? Integer.parseInt(pageNum) : 1;
            Integer size = StrUtil.isNumber(pageSize) ? Integer.parseInt(pageSize) : 10;
            JsonObject jsonObject = new JsonObject().put("orgId", orgId)
                    .put("roleNum", context.request().getParam("roleNum"));
            this.roleService.roleListPage(jsonObject, page, size, resultHandlerNonEmpty(context));
        }
    }

    /**
     * 添加角色
     * @param context
     */
    private void addRole(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("add role info:{}", jsonObject);

        Long orgId = jsonObject.getLong("orgId");
        String roleNum = jsonObject.getString("roleNum");
        if (Objects.isNull(orgId) || StrUtil.isBlank(roleNum)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            Role role = jsonObject.mapTo(Role.class);
            roleService.addRole(role, context, resultVoidHandler(context));
        }
    }

    /**
     * 查看角色信息
     * @param context
     */
    private void roleInfo(RoutingContext context){

        String roleId = context.request().getParam("roleId");
        if (StrUtil.isBlank(roleId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            roleService.roleInfo(roleId, resultHandlerNonEmpty(context));
        }
    }

    /**
     * 编辑角色信息
     * @param context
     */
    private void editRole(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("edit role info: {}", jsonObject);
        Long roleId = jsonObject.getLong("roleId");
        Long orgId = jsonObject.getLong("orgId");

        String roleNum = jsonObject.getString("roleNum");
        String url = jsonObject.getString("url");
        String remark = jsonObject.getString("remark");
        if (Objects.isNull(roleId) || Objects.isNull(orgId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            Role role = new Role();
            role.setId(roleId);

            role.setRemark(remark);
            roleService.editRole(role, context, resultVoidHandler(context));
        }


    }

    /**
     * 更新角色状态
     * @param context
     */
    private void updateRoleDel(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("update Role status: {}", jsonObject);
        Integer del = jsonObject.getInteger("del");
        Long roleId = jsonObject.getLong("roleId");
        if (Objects.isNull(del) || Objects.isNull(roleId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            roleService.updateRoleDel(del, roleId, resultVoidHandler(context));
        }
    }
}
