/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package service.sys;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import db.JdbcRepositoryWrapper;
import entity.sys.Permission;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: DeptService.java, v 0.1 2019-02-10 15:24 tangyue Exp $$
 */
@Slf4j
public class PermissionService extends JdbcRepositoryWrapper {

    private static final String BASE = " id, name, remark, create_time ";

    private static final String QUERY_ALL_PAGE = "SELECT" + BASE + "FROM SYS_PERMISSION " +
            "order by id LIMIT ?, ?";

    private static final String QUERY_PERMISSION_ID = "SELECT" + BASE + "FROM SYS_PERMISSION " +
            "where id = ?";

    private static final String QUERY_PERMISSION_NAME = "SELECT" + BASE + "FROM SYS_PERMISSION " +
            "where name = ? limit 1";

    private static final String INSERT_PERMISSION = "INSERT INTO SYS_PERMISSION " +
            "(name, remark, create_time) " +
            "VALUES (?, ?, ?)";

    private static final String UPDATE_PERMISSION = "UPDATE SYS_PERMISSION SET ";

    /**
     * 权限列表信息
     * @param params
     * @param resultHandler
     */
    public void permissionListPage(JsonObject params, int page, int size, Handler<AsyncResult<List<JsonObject>>> resultHandler){

        log.info("start permission list params: {}", params);
        JsonArray jsonArray = new JsonArray().add(params.getString("orgId"));
        StringBuffer sb = new StringBuffer(QUERY_ALL_PAGE);
        if (StrUtil.isNotBlank(params.getString("permissionName"))){
            sb.append(" and name = ?");
            jsonArray.add(params.getString("permissionName"));
        }
        sb.append(" order by id desc limit ?, ?");
        jsonArray.add(calcPage(page, size)).add(size);
        retrieveMany(jsonArray, sb.toString())
                .setHandler(resultHandler);

    }

    /**
     * 查看权限信息
     * @param permissionId
     * @param resultHandler
     */
    public void permissionInfo(String permissionId, Handler<AsyncResult<JsonObject>> resultHandler){

        JsonArray params = new JsonArray().add(permissionId);
        retrieveOne(params, QUERY_PERMISSION_ID)
                .map(option -> option.orElse(null))
                .setHandler(resultHandler);
    }

    /**
     * 新增权限
     */
    public void addPermission(Permission permission, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("start add permission: {}", permission);
        Date now = new Date();
        permission.setCreateTime(now);
        JsonArray jsonArray = new JsonArray()
                .add(permission.getName())
                .add(permission.getRemark())
                .add(permission.getCreateTime().toInstant());
        log.info("insert permission info: {}", jsonArray);
        JsonArray params = new JsonArray().add(permission.getName());
        retrieveOne(params, QUERY_PERMISSION_NAME)
                .setHandler(d -> {
                    if (d.succeeded()){
                        if (!d.result().isPresent()) {
                            executeNoResult(jsonArray, INSERT_PERMISSION, resultHandler);
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_NO_DATA);
                        }
                    } else {
                        ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                    }
                });
    }

    /**
     * 编辑权限
     */
    public void editPermission(Permission permission, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("edit permission info: {}", permission);
        StringBuffer sb = new StringBuffer(UPDATE_PERMISSION);
        JsonArray jsonArray = new JsonArray();
        if (Objects.nonNull(permission.getName())){
            sb.append(" name = ?,");
            jsonArray.add(permission.getName());
        }

        if (Objects.nonNull(permission.getRemark())){
            sb.append(" remark = ?,");
            jsonArray.add(permission.getRemark());
        }
        sb.append(" update_time = ? where id = ?");
        jsonArray.add(new Date().toInstant()).add(permission.getId());
        log.info("update permission info: {}", jsonArray);
        log.info("update permission sql: {}", sb);

        if (Objects.nonNull(permission.getName())) {
            JsonArray params = new JsonArray().add(permission.getName());
            retrieveOne(params, QUERY_PERMISSION_NAME)
                    .setHandler(d -> {
                        if (d.succeeded()) {
                            if (!d.result().isPresent()
                                    || permission.getId().equals(d.result().get().getLong("id"))){

                                executeNoResult(jsonArray, sb.toString(), resultHandler);
                            } else {
                                ApiRouter.serviceUnavailable(context, CodeEnum.PERMISSION_NUM_EXIST);
                            }
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                        }
                    });
        } else {

            JsonArray param = new JsonArray().add(permission.getId());
            retrieveOne(param, QUERY_PERMISSION_ID)
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
}
