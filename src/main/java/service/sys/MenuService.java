/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package service.sys;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import db.JdbcRepositoryWrapper;
import entity.sys.Menu;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.converter.MenuConverter;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: menuService.java, v 0.1 2019-02-10 15:24 tangyue Exp $$
 */
@Slf4j
public class MenuService extends JdbcRepositoryWrapper {

    private static final String BASE = " id, parent_id, name, url, icon, sort_name, status, remark, create_time, update_time ";

    private static final String QUERY_ALL_PAGE = "SELECT" + BASE + "FROM SYS_MENU " +
            "where org_id = ?";

    private static final String QUERY_MENU_ID = "SELECT" + BASE + "FROM SYS_MENU " +
            "where org_id = ? and id = ?";

    private static final String QUERY_MENU_NAME = "SELECT" + BASE + "FROM SYS_MENU " +
            "where org_id = ? and name = ? limit 1";

    private static final String INSERT_MENU = "INSERT INTO SYS_MENU " +
            "(parent_id, name, url, icon, sort_name, status, remark, create_time, update_time) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_MENU = "UPDATE SYS_MENU SET ";

    private static final String UPDATE_MENU_STATUS = UPDATE_MENU +
            "status = ?, update_time = ? where id = ?";


    /**
     * 菜单列表信息
     * @param params
     * @param resultHandler
     */
    public void menuListPage(JsonObject params, int page, int size, Handler<AsyncResult<List<JsonObject>>> resultHandler){

        log.info("start menu list params: {}", params);
        JsonArray jsonArray = new JsonArray().add(params.getString("orgId"));
        StringBuffer sb = new StringBuffer(QUERY_ALL_PAGE);
        if (StrUtil.isNotBlank(params.getString("menuName"))){
            sb.append(" and name = ?");
            jsonArray.add(params.getString("menuName"));
        }
        sb.append(" order by id desc limit ? offset ?");
        jsonArray.add(size).add(calcPage(page, size));
        retrieveMany(jsonArray, sb.toString())
                .setHandler(resultHandler);

    }

    /**
     * 查看菜单信息
     * @param menuId
     * @param resultHandler
     */
    public void menuInfo(Long menuId, Handler<AsyncResult<JsonObject>> resultHandler){

        JsonArray params = new JsonArray().add(menuId);
        retrieveOne(params, QUERY_MENU_ID)
                .map(option -> option.orElse(null))
                .setHandler(resultHandler);
    }

    /**
     * 新增菜单
     */
    public void addMenu(Menu menu, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("start add menu: {}", menu);
        Date now = new Date();
        menu.setCreateTime(now);
        menu.setUpdateTime(now);
        JsonArray jsonArray = MenuConverter.toJsonArray(menu);
        log.info("insert menu info: {}", jsonArray);
        JsonArray params = new JsonArray().add(menu.getName());
        retrieveOne(params, QUERY_MENU_NAME)
                .setHandler(d -> {
                    if (d.succeeded()){
                        if (!d.result().isPresent()) {
                            executeNoResult(jsonArray, INSERT_MENU, resultHandler);
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_NO_DATA);
                        }
                    } else {
                        ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                    }
                });
    }

    /**
     * 编辑菜单
     */
    public void editMenu(Menu menu, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("edit menu info: {}", menu);
        StringBuffer sb = new StringBuffer(UPDATE_MENU);
        JsonArray jsonArray = new JsonArray();
        if (Objects.nonNull(menu.getParentId())){
            sb.append(" parent_id = ?,");
            jsonArray.add(menu.getParentId());
        }
        if (Objects.nonNull(menu.getName())){
            sb.append(" name = ?,");
            jsonArray.add(menu.getName());
        }
        if (Objects.nonNull(menu.getUrl())){
            sb.append(" url = ?,");
            jsonArray.add(menu.getUrl());
        }
        if (Objects.nonNull(menu.getIcon())){
            sb.append(" icon = ?,");
            jsonArray.add(menu.getIcon());
        }
        if (Objects.nonNull(menu.getSortNum())){
            sb.append(" sort_num = ?,");
            jsonArray.add(menu.getSortNum());
        }
        if (Objects.nonNull(menu.getStatus())){
            sb.append(" status = ?,");
            jsonArray.add(menu.getStatus());
        }
        if (Objects.nonNull(menu.getRemark())){
            sb.append(" remark = ?,");
            jsonArray.add(menu.getRemark());
        }

        sb.append(" update_time = ? where id = ?");
        jsonArray.add(new Date().toInstant()).add(menu.getId());
        log.info("update menu info: {}", jsonArray);
        log.info("update menu sql: {}", sb);

        if (Objects.nonNull(menu.getName())) {
            JsonArray params = new JsonArray().add(menu.getName());
            retrieveOne(params, QUERY_MENU_NAME)
                    .setHandler(d -> {
                        if (d.succeeded()) {
                            if (!d.result().isPresent()
                                    || menu.getId().equals(d.result().get().getLong("id"))){

                                executeNoResult(jsonArray, sb.toString(), resultHandler);
                            } else {
                                ApiRouter.serviceUnavailable(context, CodeEnum.MENU_NUM_EXIST);
                            }
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                        }
                    });
        } else {

            JsonArray param = new JsonArray().add(menu.getId());
            retrieveOne(param, QUERY_MENU_ID)
                    .setHandler(d -> {
                        if (d.succeeded()) {
                            if (d.result().isPresent()) {
                                executeNoResult(jsonArray, sb.toString(), resultHandler);
                            } else {
                                ApiRouter.serviceUnavailable(context, CodeEnum.SYS_NO_DATA);
                            }
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                        }
                    });
        }

    }

    /**
     * 更新菜单状态
     */
    public void updateMenuStatus(Integer status, Integer menuId, Handler<AsyncResult<Void>> resultHandler){

        log.info("update menu: {}, status: {}",menuId, status);
        JsonArray jsonArray = new JsonArray()
                .add(status)
                .add(new Date().toInstant())
                .add(menuId);
        executeNoResult(jsonArray, UPDATE_MENU_STATUS, resultHandler);
    }
}
