/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package service.sys;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import db.JdbcRepositoryWrapper;
import entity.sys.Dept;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.converter.DeptConverter;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: DeptService.java, v 0.1 2019-02-10 15:24 tangyue Exp $$
 */
@Slf4j
public class DeptService extends JdbcRepositoryWrapper {

    private static final String BASE = " id, org_id, name, level, remark, parent_id, del_flag, create_time, update_time ";

    private static final String QUERY_ALL_PAGE = "SELECT" + BASE + "FROM SYS_DEPT " +
            "where org_id = ? order by id LIMIT ?, ?";

    private static final String QUERY_DEPT_ID = "SELECT" + BASE + "FROM SYS_DEPT " +
            "where org_id = ? and id = ?";

    private static final String QUERY_DEPT_NAME = "SELECT" + BASE + "FROM SYS_DEPT " +
            "where org_id = ? and name = ? limit 1";

    private static final String INSERT_DEPT = "INSERT INTO SYS_DEPT " +
            "(org_id, name, level, remark, parent_id, del_flag, create_time, update_time) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_DEPT = "UPDATE SYS_DEPT SET ";

    private static final String UPDATE_DEPT_DEL = UPDATE_DEPT +
            "del_flag = ?, update_time = ? where id = ?";


    /**
     * 部门列表信息
     * @param params
     * @param resultHandler
     */
    public void deptListPage(JsonObject params, int page, int size, Handler<AsyncResult<List<JsonObject>>> resultHandler){

        log.info("start dept list params: {}", params);
        JsonArray jsonArray = new JsonArray().add(params.getString("orgId"));
        StringBuffer sb = new StringBuffer(QUERY_ALL_PAGE);
        if (StrUtil.isNotBlank(params.getString("deptName"))){
            sb.append(" and name = ?");
            jsonArray.add(params.getString("deptName"));
        }
        sb.append(" order by id desc limit ?, ?");
        jsonArray.add(calcPage(page, size)).add(size);
        retrieveMany(jsonArray, sb.toString())
                .setHandler(resultHandler);

    }

    /**
     * 查看部门信息
     * @param deptId
     * @param resultHandler
     */
    public void deptInfo(Long deptId, Handler<AsyncResult<JsonObject>> resultHandler){

        JsonArray params = new JsonArray().add(deptId);
        retrieveOne(params, QUERY_DEPT_ID)
                .map(option -> option.orElse(null))
                .setHandler(resultHandler);
    }

    /**
     * 新增部门
     */
    public void addDept(Dept dept, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("start add dept: {}", dept);
        Date now = new Date();
        dept.setCreateTime(now);
        dept.setUpdateTime(now);
        JsonArray jsonArray = DeptConverter.toJsonArray(dept);
        log.info("insert dept info: {}", jsonArray);
        JsonArray params = new JsonArray().add(dept.getOrgId()).add(dept.getName());
        retrieveOne(params, QUERY_DEPT_NAME)
                .setHandler(d -> {
                    if (d.succeeded()){
                        if (!d.result().isPresent()) {
                            executeNoResult(jsonArray, INSERT_DEPT, resultHandler);
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_NO_DATA);
                        }
                    } else {
                        ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                    }
                });
    }

    /**
     * 编辑部门
     */
    public void editDept(Dept dept, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("edit dept info: {}", dept);
        StringBuffer sb = new StringBuffer(UPDATE_DEPT);
        JsonArray jsonArray = new JsonArray();
        if (Objects.nonNull(dept.getName())){
            sb.append(" name = ?,");
            jsonArray.add(dept.getName());
        }
        if (Objects.nonNull(dept.getLevel())){
            sb.append(" level = ?,");
            jsonArray.add(dept.getLevel());
        }
        if (Objects.nonNull(dept.getParentId())){
            sb.append(" parent_id = ?,");
            jsonArray.add(dept.getParentId());
        }
        if (Objects.nonNull(dept.getRemark())){
            sb.append(" remark = ?,");
            jsonArray.add(dept.getRemark());
        }
        sb.append(" update_time = ? where id = ?");
        jsonArray.add(new Date().toInstant()).add(dept.getId());
        log.info("update dept info: {}", jsonArray);
        log.info("update dept sql: {}", sb);

        if (Objects.nonNull(dept.getName())) {
            JsonArray params = new JsonArray().add(dept.getOrgId()).add(dept.getName());
            retrieveOne(params, QUERY_DEPT_NAME)
                    .setHandler(d -> {
                        if (d.succeeded()) {
                            if (!d.result().isPresent()
                                    || dept.getId().equals(d.result().get().getLong("id"))){

                                executeNoResult(jsonArray, sb.toString(), resultHandler);
                            } else {
                                ApiRouter.serviceUnavailable(context, CodeEnum.DEPT_NUM_EXIST);
                            }
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                        }
                    });
        } else {

            JsonArray param = new JsonArray().add(dept.getId());
            retrieveOne(param, QUERY_DEPT_ID)
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
     * 更新部门状态
     */
    public void updateDeptDel(Integer del, Long deptId, Handler<AsyncResult<Void>> resultHandler){

        log.info("update dept: {}, status: {}",deptId, del);
        JsonArray jsonArray = new JsonArray()
                .add(del)
                .add(new Date().toInstant())
                .add(deptId);
        executeNoResult(jsonArray, UPDATE_DEPT_DEL, resultHandler);
    }

}
