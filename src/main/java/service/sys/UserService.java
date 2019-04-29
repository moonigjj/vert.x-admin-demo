/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import db.JdbcRepositoryWrapper;
import entity.sys.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.converter.UserConverter;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: DeptService.java, v 0.1 2019-02-10 15:24 tangyue Exp $$
 */
@Slf4j
public class UserService extends JdbcRepositoryWrapper {

    private static final String BASE = " id, user_name, real_name, nick_name, avatar, pwd, salt, org_id, open_id, union_id, status, online, del_flag, create_time, update_time ";

    private static final String QUERY_ALL_PAGE = "SELECT" + BASE + "FROM SYS_USER " +
            "where org_id = ?";

    private static final String QUERY_USER_ID = "SELECT" + BASE + "FROM SYS_USER " +
            "where org_id = ? and id = ?";

    private static final String QUERY_USER_NAME = "SELECT" + BASE + "FROM SYS_USER " +
            "where org_id = ? and name = ? limit 1";

    private static final String INSERT_USER = "INSERT INTO SYS_USER " +
            "(user_name, real_name, nick_name, avatar, pwd, salt, org_id, open_id, union_id, status, online, del_flag, create_time, update_time) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_USER = "UPDATE SYS_USER SET ";

    private static final String UPDATE_USER_DEL = UPDATE_USER +
            "del_flag = ?, update_time = ? where id = ?";


    /**
     * 角色列表信息
     * @param params
     * @param resultHandler
     */
    public void userListPage(JsonObject params, int page, int size, Handler<AsyncResult<List<User>>> resultHandler){

        JsonArray jsonArray = new JsonArray().add(params.getString("orgId"));
        StringBuffer sb = new StringBuffer(QUERY_ALL_PAGE);
        if (StrUtil.isNotBlank(params.getString("userName"))){
            sb.append(" and name = ?");
            jsonArray.add(params.getString("userName"));
        }
        sb.append(" order by id desc limit ? offset ?");
        jsonArray.add(size).add(calcPage(page, size));
        retrieveMany(jsonArray, sb.toString())
                .map(list -> {
                    List<User> users = new ArrayList<>();
                    list.forEach(json -> users.add(json.mapTo(User.class)));
                    return users;
                })
                .setHandler(resultHandler);

    }

    /**
     * 查看角色信息
     * @param userId
     * @param resultHandler
     */
    public void userInfo(Long userId, Handler<AsyncResult<User>> resultHandler){

        JsonArray params = new JsonArray().add(userId);
        retrieveOne(params, QUERY_USER_ID)
                .map(option -> option.orElse(null))
                .map(json -> json.mapTo(User.class))
                .setHandler(resultHandler);
    }

    /**
     * 新增角色
     */
    public void addUser(User user, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("start add user: {}", user);
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        JsonArray jsonArray = UserConverter.toJsonArray(user);
        log.info("insert user info: {}", jsonArray);
        JsonArray params = new JsonArray().add(user.getOrgId()).add(user.getUserName());
        retrieveOne(params, QUERY_USER_NAME)
                .setHandler(d -> {
                    if (d.succeeded()){
                        if (!d.result().isPresent()) {
                            executeNoResult(jsonArray, INSERT_USER, resultHandler);
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
    public void editUser(User user, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("edit user info: {}", user);
        StringBuffer sb = new StringBuffer(UPDATE_USER);
        JsonArray jsonArray = new JsonArray();
        if (Objects.nonNull(user.getUserName())){
            sb.append(" user_name = ?,");
            jsonArray.add(user.getUserName());
        }
        if (Objects.nonNull(user.getRealName())){
            sb.append(" real_name = ?,");
            jsonArray.add(user.getRealName());
        }
        if (Objects.nonNull(user.getNickName())){
            sb.append(" nick_name = ?,");
            jsonArray.add(user.getNickName());
        }
        if (Objects.nonNull(user.getAvatar())){
            sb.append(" avatar = ?,");
            jsonArray.add(user.getAvatar());
        }
        if (Objects.nonNull(user.getPwd())){
            sb.append(" pwd = ?,");
            jsonArray.add(user.getPwd());
        }
        if (Objects.nonNull(user.getSalt())){
            sb.append(" salt = ?,");
            jsonArray.add(user.getSalt());
        }
        if (Objects.nonNull(user.getOrgId())){
            sb.append(" org_id = ?,");
            jsonArray.add(user.getOrgId());
        }
        if (Objects.nonNull(user.getOpenId())){
            sb.append(" open_id = ?,");
            jsonArray.add(user.getOpenId());
        }
        if (Objects.nonNull(user.getUnionId())){
            sb.append(" union_id = ?,");
            jsonArray.add(user.getUnionId());
        }
        if (Objects.nonNull(user.getStatus())){
            sb.append(" status = ?,");
            jsonArray.add(user.getStatus());
        }
        if (Objects.nonNull(user.getOnline())){
            sb.append(" online = ?,");
            jsonArray.add(user.getOnline());
        }
        sb.append(" update_time = ? where id = ?");
        jsonArray.add(new Date().toInstant()).add(user.getId());
        log.info("update user info: {}", jsonArray);
        log.info("update user sql: {}", sb);

        if (Objects.nonNull(user.getUserName())) {
            JsonArray params = new JsonArray().add(user.getOrgId()).add(user.getUserName());
            retrieveOne(params, QUERY_USER_NAME)
                    .setHandler(d -> {
                        if (d.succeeded()) {
                            if (!d.result().isPresent()
                                    || user.getId().equals(d.result().get().getLong("id"))){

                                executeNoResult(jsonArray, sb.toString(), resultHandler);
                            } else {
                                ApiRouter.serviceUnavailable(context, CodeEnum.USER_NUM_EXIST);
                            }
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                        }
                    });
        } else {

            JsonArray param = new JsonArray().add(user.getId());
            retrieveOne(param, QUERY_USER_ID)
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
    public void updateUserDel(Integer del, Long userId, Handler<AsyncResult<Void>> resultHandler){

        log.info("update user: {}, status: {}",userId, del);
        JsonArray jsonArray = new JsonArray()
                .add(del)
                .add(new Date().toInstant())
                .add(userId);
        executeNoResult(jsonArray, UPDATE_USER_DEL, resultHandler);
    }
}
