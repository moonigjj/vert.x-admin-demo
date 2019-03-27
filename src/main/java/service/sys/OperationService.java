/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package service.sys;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import db.JdbcRepositoryWrapper;
import entity.sys.Operation;
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
 * @version $Id: operationService.java, v 0.1 2019-02-10 15:24 tangyue Exp $$
 */
@Slf4j
public class OperationService extends JdbcRepositoryWrapper {

    private static final String BASE = " id, name, operation, create_time ";

    private static final String QUERY_ALL_PAGE = "SELECT" + BASE + "FROM SYS_OPERATION " +
            "order by id LIMIT ?, ?";

    private static final String QUERY_OPERATION_ID = "SELECT" + BASE + "FROM SYS_OPERATION " +
            "where id = ?";

    private static final String QUERY_OPERATION_NAME = "SELECT" + BASE + "FROM SYS_OPERATION " +
            "where name = ? limit 1";

    private static final String INSERT_OPERATION = "INSERT INTO SYS_OPERATION " +
            "(name, operation, create_time) " +
            "VALUES (?, ?, ?)";

    private static final String UPDATE_OPERATION = "UPDATE SYS_OPERATION SET ";

    /**
     * 操作列表信息
     * @param params
     * @param resultHandler
     */
    public void operationListPage(JsonObject params, int page, int size, Handler<AsyncResult<List<JsonObject>>> resultHandler){

        log.info("start operation list params: {}", params);
        JsonArray jsonArray = new JsonArray().add(params.getString("orgId"));
        StringBuffer sb = new StringBuffer(QUERY_ALL_PAGE);
        if (StrUtil.isNotBlank(params.getString("operationName"))){
            sb.append(" and name = ?");
            jsonArray.add(params.getString("operationName"));
        }
        sb.append(" order by id desc limit ?, ?");
        jsonArray.add(calcPage(page, size)).add(size);
        retrieveMany(jsonArray, sb.toString())
                .setHandler(resultHandler);

    }

    /**
     * 查看操作信息
     * @param operationId
     * @param resultHandler
     */
    public void operationInfo(Long operationId, Handler<AsyncResult<JsonObject>> resultHandler){

        JsonArray params = new JsonArray().add(operationId);
        retrieveOne(params, QUERY_OPERATION_ID)
                .map(option -> option.orElse(null))
                .setHandler(resultHandler);
    }

    /**
     * 新增操作
     */
    public void addOperation(Operation operation, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("start add operation: {}", operation);
        Date now = new Date();
        operation.setCreateTime(now);
        JsonArray jsonArray = new JsonArray()
                .add(operation.getName())
                .add(operation.getOperation())
                .add(operation.getCreateTime().toInstant());
        log.info("insert operation info: {}", jsonArray);
        JsonArray params = new JsonArray().add(operation.getName());
        retrieveOne(params, QUERY_OPERATION_NAME)
                .setHandler(d -> {
                    if (d.succeeded()){
                        if (!d.result().isPresent()) {
                            executeNoResult(jsonArray, INSERT_OPERATION, resultHandler);
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_NO_DATA);
                        }
                    } else {
                        ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                    }
                });
    }

    /**
     * 编辑操作
     */
    public void editOperation(Operation operation, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("edit operation info: {}", operation);
        StringBuffer sb = new StringBuffer(UPDATE_OPERATION);
        JsonArray jsonArray = new JsonArray();
        if (Objects.nonNull(operation.getName())){
            sb.append(" name = ?,");
            jsonArray.add(operation.getName());
        }

        if (Objects.nonNull(operation.getOperation())){
            sb.append(" operation = ?,");
            jsonArray.add(operation.getOperation());
        }
        sb.append(" update_time = ? where id = ?");
        jsonArray.add(new Date().toInstant()).add(operation.getId());
        log.info("update operation info: {}", jsonArray);
        log.info("update operation sql: {}", sb);

        if (Objects.nonNull(operation.getName())) {
            JsonArray params = new JsonArray().add(operation.getName());
            retrieveOne(params, QUERY_OPERATION_NAME)
                    .setHandler(d -> {
                        if (d.succeeded()) {
                            if (!d.result().isPresent()
                                    || operation.getId().equals(d.result().get().getLong("id"))){

                                executeNoResult(jsonArray, sb.toString(), resultHandler);
                            } else {
                                ApiRouter.serviceUnavailable(context, CodeEnum.OPERATION_NUM_EXIST);
                            }
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                        }
                    });
        } else {

            JsonArray param = new JsonArray().add(operation.getId());
            retrieveOne(param, QUERY_OPERATION_ID)
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
