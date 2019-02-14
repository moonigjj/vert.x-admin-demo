/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package service.sys;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import db.JdbcRepositoryWrapper;
import entity.sys.Role;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.converter.RoleConverter;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: roleService.java, v 0.1 2019-02-10 15:24 tangyue Exp $$
 */
@Slf4j
public class RoleService extends JdbcRepositoryWrapper {

    private static final String BASE = " id, parent_id, org_id, name, remark, del_flag, create_time, update_time ";

    private static final String QUERY_ALL_PAGE = "SELECT" + BASE + "FROM SYS_ROLE " +
            "where org_id = ? order by id LIMIT ?, ?";

    private static final String QUERY_ROLE_ID = "SELECT" + BASE + "FROM SYS_ROLE " +
            "where org_id = ? and id = ?";

    private static final String QUERY_ROLE_NAME = "SELECT" + BASE + "FROM SYS_ROLE " +
            "where org_id = ? and name = ? limit 1";

    private static final String INSERT_ROLE = "INSERT INTO SYS_ROLE " +
            "(parent_id, org_id, name, remark, del_flag, create_time, update_time) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_ROLE = "UPDATE SYS_ROLE SET ";

    private static final String UPDATE_ROLE_DEL = UPDATE_ROLE +
            "del_flag = ?, update_time = ? where id = ?";


    /**
     * 角色列表信息
     * @param params
     * @param resultHandler
     */
    public void roleListPage(JsonObject params, int page, int size, Handler<AsyncResult<List<JsonObject>>> resultHandler){

        log.info("start role list params: {}", params);
        JsonArray jsonArray = new JsonArray().add(params.getString("orgId"));
        StringBuffer sb = new StringBuffer(QUERY_ALL_PAGE);
        if (StrUtil.isNotBlank(params.getString("roleName"))){
            sb.append(" and name = ?");
            jsonArray.add(params.getString("roleName"));
        }
        sb.append(" order by id desc limit ?, ?");
        jsonArray.add(calcPage(page, size)).add(size);
        retrieveMany(jsonArray, sb.toString())
                .setHandler(resultHandler);

    }

    /**
     * 查看角色信息
     * @param roleId
     * @param resultHandler
     */
    public void roleInfo(String roleId, Handler<AsyncResult<JsonObject>> resultHandler){

        JsonArray params = new JsonArray().add(roleId);
        retrieveOne(params, QUERY_ROLE_ID)
                .map(option -> option.orElse(null))
                .setHandler(resultHandler);
    }

    /**
     * 新增角色
     */
    public void addRole(Role role, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("start add role: {}", role);
        Date now = new Date();
        role.setCreateTime(now);
        role.setUpdateTime(now);
        JsonArray jsonArray = RoleConverter.toJsonArray(role);
        log.info("insert role info: {}", jsonArray);
        JsonArray params = new JsonArray().add(role.getOrgId()).add(role.getName());
        retrieveOne(params, QUERY_ROLE_NAME)
                .setHandler(d -> {
                    if (d.succeeded()){
                        if (!d.result().isPresent()) {
                            executeNoResult(jsonArray, INSERT_ROLE, resultHandler);
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_NO_DATA);
                        }
                    } else {
                        ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                    }
                });
    }

    /**
     * 编辑角色
     */
    public void editRole(Role role, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("edit role info: {}", role);
        StringBuffer sb = new StringBuffer(UPDATE_ROLE);
        JsonArray jsonArray = new JsonArray();
        if (Objects.nonNull(role.getParentId())){
            sb.append(" parent_id = ?,");
            jsonArray.add(role.getParentId());
        }
        if (Objects.nonNull(role.getName())){
            sb.append(" name = ?,");
            jsonArray.add(role.getName());
        }
        if (Objects.nonNull(role.getRemark())){
            sb.append(" remark = ?,");
            jsonArray.add(role.getRemark());
        }
        sb.append(" update_time = ? where id = ?");
        jsonArray.add(new Date().toInstant()).add(role.getId());
        log.info("update role info: {}", jsonArray);
        log.info("update role sql: {}", sb);

        if (Objects.nonNull(role.getName())) {
            JsonArray params = new JsonArray().add(role.getOrgId()).add(role.getName());
            retrieveOne(params, QUERY_ROLE_NAME)
                    .setHandler(d -> {
                        if (d.succeeded()) {
                            if (!d.result().isPresent()
                                    || role.getId().equals(d.result().get().getLong("id"))){

                                executeNoResult(jsonArray, sb.toString(), resultHandler);
                            } else {
                                ApiRouter.serviceUnavailable(context, CodeEnum.ROLE_NUM_EXIST);
                            }
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                        }
                    });
        } else {

            JsonArray param = new JsonArray().add(role.getId());
            retrieveOne(param, QUERY_ROLE_ID)
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
     * 更新角色状态
     */
    public void updateRoleDel(Integer del, Integer roleId, Handler<AsyncResult<Void>> resultHandler){

        log.info("update role: {}, status: {}",roleId, del);
        JsonArray jsonArray = new JsonArray()
                .add(del)
                .add(new Date().toInstant())
                .add(roleId);
        executeNoResult(jsonArray, UPDATE_ROLE_DEL, resultHandler);
    }
}
