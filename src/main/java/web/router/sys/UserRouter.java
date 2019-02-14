/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package web.router.sys;

import java.util.Objects;

import entity.sys.User;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.sys.UserService;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: AuthRouter.java, v 0.1 2019-01-21 13:46 tangyue Exp $$
 */
@Slf4j
public final class UserRouter extends ApiRouter {

    private service.sys.UserService userService;

    public static Router create(){

        return new UserRouter().router;
    }

    private UserRouter(){

        this.router.get("/list/:orgId").handler(this::userListPage);
        this.router.get("/info/:userId").handler(this::userInfo);
        this.router.post("/add").handler(this::addUser);
        this.router.post("/edit").handler(this::editUser);
        this.router.post("/del").handler(this::updateUserDel);

        this.userService = new UserService();
    }

    /**
     * 用户分页列表
     * @param context
     */
    private void userListPage(RoutingContext context){
        String orgId = context.request().getParam("orgId");
        log.info("user list page: {}", orgId);
        String pageNum = context.request().getParam("pageNum");
        String pageSize = context.request().getParam("pageSize");
        if (StrUtil.isBlank(orgId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            Integer page = StrUtil.isNumber(pageNum) ? Integer.parseInt(pageNum) : 1;
            Integer size = StrUtil.isNumber(pageSize) ? Integer.parseInt(pageSize) : 10;
            JsonObject jsonObject = new JsonObject().put("orgId", orgId)
                    .put("userNum", context.request().getParam("userNum"));
            this.userService.userListPage(jsonObject, page, size, resultHandlerNonEmpty(context));
        }
    }

    /**
     * 添加用户
     * @param context
     */
    private void addUser(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("add user info:{}", jsonObject);

        Long orgId = jsonObject.getLong("orgId");
        String UserNum = jsonObject.getString("name");
        if (Objects.isNull(orgId) || StrUtil.isBlank(UserNum)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            User user = jsonObject.mapTo(User.class);
            userService.addUser(user, context, resultVoidHandler(context));
        }
    }

    /**
     * 查看用户信息
     * @param context
     */
    private void userInfo(RoutingContext context){

        String userId = context.request().getParam("userId");
        if (StrUtil.isBlank(userId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            userService.userInfo(userId, resultHandlerNonEmpty(context));
        }
    }

    /**
     * 编辑用户信息
     * @param context
     */
    private void editUser(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("edit user info: {}", jsonObject);
        Long UserId = jsonObject.getLong("userId");
        Long orgId = jsonObject.getLong("orgId");

        String userNum = jsonObject.getString("userNum");
        String url = jsonObject.getString("url");
        String remark = jsonObject.getString("remark");
        if (Objects.isNull(UserId) || Objects.isNull(orgId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            User User = new User();
            User.setId(UserId);

            userService.editUser(User, context, resultVoidHandler(context));
        }


    }

    /**
     * 更新用户状态
     * @param context
     */
    private void updateUserDel(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("update user status: {}", jsonObject);
        Integer del = jsonObject.getInteger("del");
        Long userId = jsonObject.getLong("userId");
        if (Objects.isNull(del) || Objects.isNull(userId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            userService.updateUserDel(del, userId, resultVoidHandler(context));
        }
    }
}
