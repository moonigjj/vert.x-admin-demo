/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package web.router.sys;

import java.util.Objects;

import entity.sys.Dept;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.sys.DeptService;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: AuthRouter.java, v 0.1 2019-01-21 13:46 tangyue Exp $$
 */
@Slf4j
public final class DeptRouter extends ApiRouter {

    private DeptService deptService;

    public static Router create(){

        return new DeptRouter().router;
    }

    private DeptRouter(){

        this.router.get("/list/:orgId").handler(this::deptListPage);
        this.router.get("/info/:deptId").handler(this::deptInfo);
        this.router.post("/add").handler(this::addDept);
        this.router.post("/edit").handler(this::editDept);
        this.router.post("/del").handler(this::updateDeptDel);

        this.deptService = new DeptService();
    }

    /**
     * 部门分页列表
     * @param context
     */
    private void deptListPage(RoutingContext context){
        String orgId = context.request().getParam("orgId");
        log.info("dept list page: {}", orgId);
        String pageNum = context.request().getParam("pageNum");
        String pageSize = context.request().getParam("pageSize");
        if (StrUtil.isBlank(orgId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            Integer page = StrUtil.isNumber(pageNum) ? Integer.parseInt(pageNum) : 1;
            Integer size = StrUtil.isNumber(pageSize) ? Integer.parseInt(pageSize) : 10;
            JsonObject jsonObject = new JsonObject().put("orgId", orgId)
                    .put("deptNum", context.request().getParam("deptNum"));
            this.deptService.deptListPage(jsonObject, page, size, resultHandlerNonEmpty(context));
        }
    }

    /**
     * 添加部门
     * @param context
     */
    private void addDept(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("add dept info:{}", jsonObject);

        Long orgId = jsonObject.getLong("orgId");
        String deptNum = jsonObject.getString("name");
        if (Objects.isNull(orgId) || StrUtil.isBlank(deptNum)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            Dept dept = jsonObject.mapTo(Dept.class);
            deptService.addDept(dept, context, resultVoidHandler(context));
        }
    }

    /**
     * 查看部门信息
     * @param context
     */
    private void deptInfo(RoutingContext context){

        String deptId = context.request().getParam("deptId");
        if (StrUtil.isBlank(deptId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            deptService.deptInfo(deptId, resultHandlerNonEmpty(context));
        }
    }

    /**
     * 编辑部门信息
     * @param context
     */
    private void editDept(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("edit dept info: {}", jsonObject);
        Long deptId = jsonObject.getLong("deptId");
        Long orgId = jsonObject.getLong("orgId");

        String deptNum = jsonObject.getString("deptNum");
        String url = jsonObject.getString("url");
        String remark = jsonObject.getString("remark");
        if (Objects.isNull(deptId) || Objects.isNull(orgId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            Dept dept = new Dept();
            dept.setId(deptId);

            dept.setRemark(remark);
            deptService.editDept(dept, context, resultVoidHandler(context));
        }


    }

    /**
     * 更新部门状态
     * @param context
     */
    private void updateDeptDel(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("update dept status: {}", jsonObject);
        Integer del = jsonObject.getInteger("del");
        Integer deptId = jsonObject.getInteger("deptId");
        if (Objects.isNull(del) || Objects.isNull(deptId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            deptService.updateDeptDel(del, deptId, resultVoidHandler(context));
        }
    }
}
