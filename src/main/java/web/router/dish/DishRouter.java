/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package web.router.dish;

import java.math.BigDecimal;
import java.util.Objects;

import entity.dish.DishFood;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.dish.DishService;
import service.converter.DishFoodConverter;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: DishRouter.java, v 0.1 2018-06-13 14:50 tangyue Exp $$
 */
@Slf4j
public final class DishRouter extends ApiRouter {

    private DishService dishService;

    public static Router create(){
        return new DishRouter().router;
    }

    private DishRouter(){

        this.router.get("/list/:orgId").handler(this::dishListPage);
        this.router.get("/info/:foodId").handler(this::dishFoodInfo);
        this.router.post("/add").handler(this::addDishFood);
        this.router.post("/edit").handler(this::editDishFood);
        this.router.post("/status").handler(this::updateDishFoodStatus);
        this.router.post("/takeout").handler(this::updateDishFoodTakeout);

        this.dishService = new DishService();
    }

    /**
     * 餐桌分页列表
     * @param context
     */
    private void dishListPage(RoutingContext context){

        String orgId = context.request().getParam("orgId");
        log.info("dishFood list page: {}", orgId);
        String pageNum = context.request().getParam("pageNum");
        String pageSize = context.request().getParam("pageSize");
        if (StrUtil.isBlank(orgId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            Integer page = StrUtil.isNumber(pageNum) ? Integer.parseInt(pageNum) : 1;
            Integer size = StrUtil.isNumber(pageSize) ? Integer.parseInt(pageSize) : 10;
            JsonObject jsonObject = new JsonObject().put("orgId", orgId)
                    .put("dishName", context.request().getParam("dishName"));
            this.dishService.dishListPage(jsonObject, page, size, resultHandlerNonEmpty(context));
        }
    }

    /**
     * 添加餐桌
     * @param context
     */
    private void addDishFood(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("add dishFood info:{}", jsonObject);

        if (!DishFoodConverter.isValid(jsonObject)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            DishFood dishFood = jsonObject.mapTo(DishFood.class);
            dishService.addDish(dishFood, context, resultVoidHandler(context));
        }
    }

    /**
     * 查看餐桌信息
     * @param context
     */
    private void dishFoodInfo(RoutingContext context){

        String foodId = context.request().getParam("foodId");
        if (StrUtil.isBlank(foodId)){
            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {

            dishService.dishInfo(foodId, resultHandlerNonEmpty(context));
        }
    }

    /**
     * 编辑餐桌信息
     * @param context
     */
    private void editDishFood(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("edit dishFood info: {}", jsonObject);
        Long foodId = jsonObject.getLong("foodId");
        Long orgId = jsonObject.getLong("orgId");

        String dishName = jsonObject.getString("dishName");
        Double dishPrice = jsonObject.getDouble("dishPrice");
        Double dishDiscountPrice = jsonObject.getDouble("dishDiscountPrice");
        String dishIcon = jsonObject.getString("dishIcon");
        String remark = jsonObject.getString("remark");
        if (Objects.isNull(foodId) || Objects.isNull(orgId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            DishFood dishFood = new DishFood();
            dishFood.setId(foodId);
            dishFood.setOrgId(orgId);
            dishFood.setDishName(dishName);
            dishFood.setDishPrice(new BigDecimal(dishPrice));
            dishFood.setDishDiscountPrice(new BigDecimal(dishDiscountPrice));
            dishFood.setDishIcon(dishIcon);
            dishFood.setRemark(remark);
            dishService.editDish(dishFood, context, resultVoidHandler(context));
        }


    }

    /**
     * 更新餐桌状态
     * @param context
     */
    private void updateDishFoodStatus(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("update dishFood status: {}", jsonObject);
        Integer status = jsonObject.getInteger("status");
        Integer foodId = jsonObject.getInteger("foodId");
        if (Objects.isNull(status) || Objects.isNull(foodId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            dishService.updateDishStatus(status, foodId, resultVoidHandler(context));
        }
    }


    /**
     * 更新餐桌状态
     * @param context
     */
    private void updateDishFoodTakeout(RoutingContext context){

        JsonObject jsonObject = context.getBodyAsJson();
        log.info("update dishFood takeout: {}", jsonObject);
        Integer takeout = jsonObject.getInteger("takeout");
        Integer foodId = jsonObject.getInteger("foodId");
        if (Objects.isNull(takeout) || Objects.isNull(foodId)){

            serviceUnavailable(context, CodeEnum.SYS_REQUEST);
        } else {
            dishService.updateDishTakeout(takeout, foodId, resultVoidHandler(context));
        }
    }

}
