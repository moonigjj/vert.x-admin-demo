/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package web.router.sys;

import java.util.Objects;

import entity.sys.Org;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.sys.OrgService;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: AuthRouter.java, v 0.1 2019-01-21 13:46 tangyue Exp $$
 */
@Slf4j
public final class OrgRouter extends ApiRouter {

    private OrgService orgService;

    public static Router create(){

        return new OrgRouter().router;
    }

    private OrgRouter(){

        this.router.get("/list/:orgId").handler(this::orgListPage);
        this.router.get("/info/:orgId").handler(this::orgInfo);
        this.router.post("/add").handler(this::addOrg);
        this.router.post("/edit").handler(this::editOrg);
        this.router.post("/del").handler(this::updateOrgDel);

        this.orgService = new OrgService();
    }

    /**
     * 机构分页列表
     * @param context
     */
    private void orgListPage(RoutingContext context){
        String orgId = context.request().getParam("orgId");
        log.info("org list page: {}", orgId);
        String pageNum = context.request().getParam("pageNum");
        String pageSize = context.request().getParam("pageSize");
        if (StrUtil.isBlank(orgId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            Integer page = StrUtil.isNumber(pageNum) ? Integer.parseInt(pageNum) : 1;
            Integer size = StrUtil.isNumber(pageSize) ? Integer.parseInt(pageSize) : 10;
            JsonObject jsonObject = new JsonObject().put("orgId", orgId)
                    .put("orgNum", context.request().getParam("orgNum"));
            this.orgService.orgListPage(jsonObject, page, size, resultHandlerNonEmpty(context));
        }
    }

    /**
     * 添加机构
     * @param context
     */
    private void addOrg(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("add org info:{}", jsonObject);

        Long orgId = jsonObject.getLong("orgId");
        String orgNum = jsonObject.getString("name");
        if (Objects.isNull(orgId) || StrUtil.isBlank(orgNum)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            Org org = jsonObject.mapTo(Org.class);
            orgService.addOrg(org, context, resultVoidHandler(context));
        }
    }

    /**
     * 查看机构信息
     * @param context
     */
    private void orgInfo(RoutingContext context){

        String orgId = context.request().getParam("orgId");
        if (StrUtil.isBlank(orgId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            orgService.orgInfo(Long.parseLong(orgId), resultHandlerNonEmpty(context));
        }
    }

    /**
     * 编辑机构信息
     * @param context
     */
    private void editOrg(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("edit org info: {}", jsonObject);
        Long orgId = jsonObject.getLong("orgId");

        String remark = jsonObject.getString("remark");
        if (Objects.isNull(orgId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            Org org = new Org();
            org.setId(orgId);

            orgService.editOrg(org, context, resultVoidHandler(context));
        }


    }

    /**
     * 更新机构状态
     * @param context
     */
    private void updateOrgDel(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("update org status: {}", jsonObject);
        Integer del = jsonObject.getInteger("del");
        Long orgId = jsonObject.getLong("orgId");
        if (Objects.isNull(del) || Objects.isNull(orgId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            orgService.updateOrgDel(del, orgId, resultVoidHandler(context));
        }
    }
}
