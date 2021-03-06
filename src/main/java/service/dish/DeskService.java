/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package service.dish;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import db.JdbcRepositoryWrapper;
import entity.dish.Desk;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.converter.DeskConverter;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: DeskService.java, v 0.1 2018-06-11 14:33 tangyue Exp $$
 */
@Slf4j
public class DeskService extends JdbcRepositoryWrapper {

    private static final String BASE = "id , org_id, desk_num, url, remark, desk_status, update_time";

    private static final String QUERY_ALL_PAGE = "SELECT "+ BASE +" FROM d_desk ";

    private static final String QUERY_DESK_ID = "SELECT "+ BASE +" FROM d_desk where id = ?";

    private static final String QUERY_DESK_NUM = "SELECT "+ BASE +" FROM d_desk " +
            "where org_id = ? and desk_num = ?";

    private static final String INSERT_DESK = "INSERT INTO d_desk " +
            "(org_id, desk_num, url, remark, desk_status, create_time, update_time) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_DESK = "UPDATE d_desk SET ";

    private static final String UPDATE_DESK_STATUS = UPDATE_DESK +
            "desk_status = ?, update_time = ? where id = ?";

    /**
     * 餐桌列表信息
     * @param params
     * @param resultHandler
     */
    public void deskListPage(JsonObject params, int page, int size, Handler<AsyncResult<List<Desk>>> resultHandler){

        log.info("start desk list params: {}", params);
        JsonArray jsonArray = new JsonArray().add(params.getString("orgId"));
        StringBuffer sb = new StringBuffer(QUERY_ALL_PAGE);
        if (StrUtil.isNotBlank(params.getString("deskNum"))){
            sb.append(" and desk_num = ?");
            jsonArray.add(params.getString("deskNum"));
        }
        sb.append(" order by update_time desc limit ?, ?");
        jsonArray.add(calcPage(page, size)).add(size);
        retrieveMany(jsonArray, sb.toString())
                .map(list -> {
                    List<Desk> desks = new ArrayList<>();
                    list.forEach(json -> desks.add(json.mapTo(Desk.class)));
                    return desks;
                })
                .setHandler(resultHandler);

    }

    /**
     * 查看餐桌信息
     * @param deskId
     * @param resultHandler
     */
    public void deskInfo(String deskId, Handler<AsyncResult<Desk>> resultHandler){

        JsonArray params = new JsonArray().add(deskId);
        retrieveOne(params, QUERY_DESK_ID)
                .map(option -> option.orElse(null))
                .map(d -> d.mapTo(Desk.class))
                .setHandler(resultHandler);
    }

    /**
     * 新增餐桌
     */
    public void addDesk(Desk desk, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("start add desk: {}", desk);
        Date now = new Date();
        desk.setDeskStatus(0);
        desk.setCreateTime(now);
        desk.setUpdateTime(now);
        JsonArray jsonArray = DeskConverter.toJsonArray(desk);
        log.info("insert desk info: {}", jsonArray);
        JsonArray params = new JsonArray().add(desk.getOrgId()).add(desk.getDeskNum());
        retrieveOne(params, QUERY_DESK_NUM)
                .setHandler(d -> {
                    if (d.succeeded()){
                        if (!d.result().isPresent()) {
                            executeNoResult(jsonArray, INSERT_DESK, resultHandler);
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_NO_DATA);
                        }
                    } else {
                        ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                    }
                });
    }

    /**
     * 编辑餐桌
     */
    public void editDesk(Desk desk, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("edit desk info: {}", desk);
        StringBuffer sb = new StringBuffer(UPDATE_DESK);
        JsonArray jsonArray = new JsonArray();
        if (Objects.nonNull(desk.getDeskNum())){
            sb.append(" desk_num = ?,");
            jsonArray.add(desk.getDeskNum());
        }
        if (Objects.nonNull(desk.getUrl())){
            sb.append(" url = ?,");
            jsonArray.add(desk.getUrl());
        }
        if (Objects.nonNull(desk.getRemark())){
            sb.append(" remark = ?,");
            jsonArray.add(desk.getRemark());
        }
        sb.append(" update_time = ? where id = ?");
        jsonArray.add(new Date().toInstant()).add(desk.getId());
        log.info("update desk info: {}", jsonArray);

        if (Objects.nonNull(desk.getDeskNum())) {
            JsonArray params = new JsonArray().add(desk.getOrgId()).add(desk.getDeskNum());
            retrieveOne(params, QUERY_DESK_NUM)
                    .setHandler(d -> {
                        if (d.succeeded()) {
                            if (!d.result().isPresent()
                                    || desk.getId().equals(d.result().get().getLong("id"))){

                                executeNoResult(jsonArray, sb.toString(), resultHandler);
                            } else {
                                ApiRouter.serviceUnavailable(context, CodeEnum.DESK_NUM_EXIST);
                            }
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                        }
                    });
        } else {

            JsonArray param = new JsonArray().add(desk.getId());
            retrieveOne(param, QUERY_DESK_ID)
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
     * 更新餐桌状态
     */
    public void updateDeskStatus(Integer status, Integer deskId, Handler<AsyncResult<Void>> resultHandler){

        log.info("update desk: {}, status: {}",deskId, status);
        JsonArray jsonArray = new JsonArray()
                .add(status)
                .add(new Date().toInstant())
                .add(deskId);
        executeNoResult(jsonArray, UPDATE_DESK_STATUS, resultHandler);
    }
}
