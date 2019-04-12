/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package service.sys;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import db.JdbcRepositoryWrapper;
import entity.sys.Org;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.converter.OrgConverter;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: OrgService.java, v 0.1 2019-02-10 15:26 tangyue Exp $$
 */
@Slf4j
public class OrgService extends JdbcRepositoryWrapper {

    private static final String BASE = " id, parent_id, code, name, address, contact, contact_number, del_flag, create_time, update_time ";

    private static final String QUERY_ALL_PAGE = "SELECT" + BASE + "FROM SYS_ORG ";

    private static final String QUERY_ORG_ID = "SELECT" + BASE + "FROM SYS_ORG " +
            "where id = ?";

    private static final String QUERY_ORG_NAME = "SELECT" + BASE + "FROM SYS_ORG " +
            "where name = ? limit 1";

    private static final String INSERT_ORG = "INSERT INTO SYS_ORG " +
            "(parent_id, code, name, address, contact, contact_number, del_flag, create_time, update_time) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_ORG = "UPDATE SYS_ORG SET ";

    private static final String UPDATE_ORG_DEL = UPDATE_ORG +
            "del_flag = ?, update_time = ? where id = ?";

    /**
     * 机构列表信息
     * @param params
     * @param resultHandler
     */
    public void orgListPage(JsonObject params, int page, int size, Handler<AsyncResult<List<JsonObject>>> resultHandler){

        log.info("start org list params: {}", params);
        JsonArray jsonArray = new JsonArray().add(params.getString("orgId"));
        StringBuffer sb = new StringBuffer(QUERY_ALL_PAGE);
        if (StrUtil.isNotBlank(params.getString("orgName"))){
            sb.append(" and name = ?");
            jsonArray.add(params.getString("orgName"));
        }
        sb.append(" order by id desc limit ? offset ?");
        jsonArray.add(size).add(calcPage(page, size));
        retrieveMany(jsonArray, sb.toString())
                .setHandler(resultHandler);

    }

    /**
     * 查看机构信息
     * @param orgId
     * @param resultHandler
     */
    public void orgInfo(Long orgId, Handler<AsyncResult<JsonObject>> resultHandler){

        JsonArray params = new JsonArray().add(orgId);
        retrieveOne(params, QUERY_ORG_ID)
                .map(option -> option.orElse(null))
                .setHandler(resultHandler);
    }

    /**
     * 新增机构
     */
    public void addOrg(Org org, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("start add org: {}", org);
        Date now = new Date();
        org.setCreateTime(now);
        org.setUpdateTime(now);
        JsonArray jsonArray = OrgConverter.toJsonArray(org);
        log.info("insert org info: {}", jsonArray);
        JsonArray params = new JsonArray().add(org.getName());
        retrieveOne(params, QUERY_ORG_NAME)
                .setHandler(d -> {
                    if (d.succeeded()){
                        if (!d.result().isPresent()) {
                            executeNoResult(jsonArray, INSERT_ORG, resultHandler);
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_NO_DATA);
                        }
                    } else {
                        ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                    }
                });
    }

    /**
     * 编辑机构
     */
    public void editOrg(Org org, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("edit org info: {}", org);
        StringBuffer sb = new StringBuffer(UPDATE_ORG);
        JsonArray jsonArray = new JsonArray();
        if (Objects.nonNull(org.getParentId())){
            sb.append(" parent_id = ?,");
            jsonArray.add(org.getParentId());
        }
        if (Objects.nonNull(org.getCode())){
            sb.append(" code = ?,");
            jsonArray.add(org.getCode());
        }
        if (Objects.nonNull(org.getName())){
            sb.append(" name = ?,");
            jsonArray.add(org.getName());
        }
        if (Objects.nonNull(org.getAddress())){
            sb.append(" address = ?,");
            jsonArray.add(org.getAddress());
        }
        if (Objects.nonNull(org.getContact())){
            sb.append(" contact = ?,");
            jsonArray.add(org.getContact());
        }
        if (Objects.nonNull(org.getContactName())){
            sb.append(" contact_name = ?,");
            jsonArray.add(org.getContactName());
        }
        sb.append(" update_time = ? where id = ?");
        jsonArray.add(new Date().toInstant()).add(org.getId());
        log.info("update org info: {}", jsonArray);
        log.info("update org sql: {}", sb);

        if (Objects.nonNull(org.getName())) {
            JsonArray params = new JsonArray().add(org.getName());
            retrieveOne(params, QUERY_ORG_NAME)
                    .setHandler(d -> {
                        if (d.succeeded()) {
                            if (!d.result().isPresent()
                                    || org.getId().equals(d.result().get().getLong("id"))){

                                executeNoResult(jsonArray, sb.toString(), resultHandler);
                            } else {
                                ApiRouter.serviceUnavailable(context, CodeEnum.ORG_NUM_EXIST);
                            }
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                        }
                    });
        } else {

            JsonArray param = new JsonArray().add(org.getId());
            retrieveOne(param, QUERY_ORG_ID)
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
     * 更新机构状态
     */
    public void updateOrgDel(Integer del, Long orgId, Handler<AsyncResult<Void>> resultHandler){

        log.info("update org: {}, status: {}",orgId, del);
        JsonArray jsonArray = new JsonArray()
                .add(del)
                .add(new Date().toInstant())
                .add(orgId);
        executeNoResult(jsonArray, UPDATE_ORG_DEL, resultHandler);
    }
}
