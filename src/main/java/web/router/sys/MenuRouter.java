/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package web.router.sys;

import java.util.Objects;

import entity.sys.Menu;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.sys.MenuService;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: AuthRouter.java, v 0.1 2019-01-21 13:46 tangyue Exp $$
 */
@Slf4j
public final class MenuRouter extends ApiRouter {

    private MenuService menuService;

    public static Router create(){

        return new MenuRouter().router;
    }

    private MenuRouter(){

        this.router.get("/list/:orgId").handler(this::menuListPage);
        this.router.get("/info/:menuId").handler(this::menuInfo);
        this.router.post("/add").handler(this::addMenu);
        this.router.post("/edit").handler(this::editMenu);

        this.menuService = new MenuService();
    }

    /**
     * 部门分页列表
     * @param context
     */
    private void menuListPage(RoutingContext context){
        String orgId = context.request().getParam("orgId");
        log.info("menu list page: {}", orgId);
        String pageNum = context.request().getParam("pageNum");
        String pageSize = context.request().getParam("pageSize");
        if (StrUtil.isBlank(orgId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            Integer page = StrUtil.isNumber(pageNum) ? Integer.parseInt(pageNum) : 1;
            Integer size = StrUtil.isNumber(pageSize) ? Integer.parseInt(pageSize) : 10;
            JsonObject jsonObject = new JsonObject().put("orgId", orgId)
                    .put("menuNum", context.request().getParam("menuNum"));
            this.menuService.menuListPage(jsonObject, page, size, resultHandlerNonEmpty(context));
        }
    }

    /**
     * 添加部门
     * @param context
     */
    private void addMenu(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("add menu info:{}", jsonObject);

        Long orgId = jsonObject.getLong("orgId");
        String menuNum = jsonObject.getString("name");
        if (Objects.isNull(orgId) || StrUtil.isBlank(menuNum)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            Menu menu = jsonObject.mapTo(Menu.class);
            menuService.addMenu(menu, context, resultVoidHandler(context));
        }
    }

    /**
     * 查看部门信息
     * @param context
     */
    private void menuInfo(RoutingContext context){

        String menuId = context.request().getParam("menuId");
        if (StrUtil.isBlank(menuId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            menuService.menuInfo(Long.parseLong(menuId), resultHandlerNonEmpty(context));
        }
    }

    /**
     * 编辑部门信息
     * @param context
     */
    private void editMenu(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("edit menu info: {}", jsonObject);
        Long menuId = jsonObject.getLong("menuId");
        Long orgId = jsonObject.getLong("orgId");

        String url = jsonObject.getString("url");
        String remark = jsonObject.getString("remark");
        if (Objects.isNull(menuId) || Objects.isNull(orgId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            Menu menu = new Menu();
            menu.setId(menuId);

            menuService.editMenu(menu, context, resultVoidHandler(context));
        }


    }
    
}
