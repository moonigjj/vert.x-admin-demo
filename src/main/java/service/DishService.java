/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import db.JdbcRepositoryWrapper;
import entity.DishFood;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import service.converter.DishFoodConverter;
import utils.CodeEnum;
import utils.StrUtil;
import web.ApiRouter;

/**
 *
 * @author tangyue
 * @version $Id: DishService.java, v 0.1 2018-06-11 17:12 tangyue Exp $$
 */
@Slf4j
public class DishService extends JdbcRepositoryWrapper {

    private static final String BASE = "id , merchant_id merchantId, dish_name dishName, dish_price dishPrice, dish_discount_price dishDiscountPrice," +
            " dish_icon dishIcon, dish_is_takeout dishTakeout, remark, dish_status dishStatus, DATE_FORMAT(update_time,'%Y-%m-%d %H:%i:%s') updateTime";

    private static final String QUERY_ALL_PAGE = "SELECT "+ BASE +" FROM dish_food ";

    private static final String QUERY_DISH_ID = "SELECT "+ BASE +" FROM dish_food where id = ?";

    private static final String QUERY_DISH_NAME = "SELECT "+ BASE +" FROM dish_food where merchant_id = ? and dish_name = ?";

    private static final String INSERT_DISH = "INSERT INTO dish_food " +
            "(merchant_id, dish_name, dish_price, dish_discount_price, dish_icon, dish_is_takeout, remark, dish_status, create_time, update_time) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_DISH = "UPDATE dish_food SET ";

    private static final String UPDATE_DISH_STATUS = "UPDATE dish_food SET " +
            "dish_status = ?, update_time = ? where id = ?";


    private static final String UPDATE_DISH_TAKEOUT = "UPDATE dish_food SET " +
            "dish_is_takeout = ?, update_time = ? where id = ?";



    /**
     * 菜品列表
     * @param params
     * @param page
     * @param size
     * @param resultHandler
     */
    public void dishListPage(JsonObject params, int page, int size, Handler<AsyncResult<List<JsonObject>>> resultHandler){

        log.info("start dish list params: {}", params);
        JsonArray jsonArray = new JsonArray().add(params.getString("merchantId"));
        StringBuffer sb = new StringBuffer(QUERY_ALL_PAGE);
        if (StrUtil.isNotBlank(params.getString("dishName"))){
            sb.append(" and dish_name = ?");
            jsonArray.add(params.getString("dishName"));
        }
        sb.append(" order by update_time desc limit ?, ? ");
        jsonArray.add(calcPage(page, size)).add(size);
        retrieveMany(jsonArray, sb.toString())
                .setHandler(resultHandler);
    }

    /**
     * 菜品信息
     * @param dishId 菜品id
     * @param resultHandler
     */
    public void dishInfo(String dishId, Handler<AsyncResult<JsonObject>> resultHandler){

        JsonArray params = new JsonArray().add(dishId);
        retrieveOne(params, QUERY_DISH_ID)
                .map(option -> option.orElse(null))
                .setHandler(resultHandler);
    }

    /**
     * 添加菜品
     * @param food
     * @param context
     * @param resultHandler
     */
    public void addDish(DishFood food, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("start add dish: {}", food);
        Date now = new Date();
        food.setDishStatus(1);
        food.setCreateTime(now);
        food.setUpdateTime(now);
        JsonArray jsonArray = DishFoodConverter.toJsonArray(food);
        log.info("insert food info: {}", jsonArray);
        JsonArray params = new JsonArray().add(food.getMerchantId()).add(food.getDishName());
        retrieveOne(params, QUERY_DISH_NAME)
                .setHandler(d -> {
                    if (d.succeeded()){
                        if (!d.result().isPresent()) {
                            executeNoResult(jsonArray, INSERT_DISH, resultHandler);
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.DISH_NAME_EXIST);
                        }
                    } else {
                        ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                    }
                });
    }

    /**
     * 编辑菜品
     * @param food
     * @param context
     * @param resultHandler
     */
    public void editDish(DishFood food, RoutingContext context, Handler<AsyncResult<Void>> resultHandler){

        log.info("edit dish food info: {}", food);
        StringBuffer sb = new StringBuffer(UPDATE_DISH);
        JsonArray jsonArray = new JsonArray();
        if (Objects.nonNull(food.getDishName())){
            sb.append(" dish_name = ?,");
            jsonArray.add(food.getDishName());
        }
        if (Objects.nonNull(food.getDishPrice())){
            sb.append(" dish_price = ?,");
            jsonArray.add(food.getDishPrice().doubleValue());
        }
        if (Objects.nonNull(food.getDishDiscountPrice())){
            sb.append(" dish_discount_price = ?,");
            jsonArray.add(food.getDishDiscountPrice().doubleValue());
        }
        if (Objects.nonNull(food.getDishIcon())){
            sb.append(" dish_icon = ?,");
            jsonArray.add(food.getDishIcon());
        }
        if (Objects.nonNull(food.getRemark())){
            sb.append(" remark = ?,");
            jsonArray.add(food.getRemark());
        }
        sb.append(" update_time = ? where id = ?");
        jsonArray.add(new Date().toInstant()).add(food.getId());
        log.info("update dish food info: {}", jsonArray);
        log.info("update dish food sql: {}", sb);

        if (Objects.nonNull(food.getDishName())) {
            JsonArray params = new JsonArray().add(food.getMerchantId()).add(food.getDishName());
            retrieveOne(params, QUERY_DISH_NAME)
                    .setHandler(d -> {
                        if (d.succeeded()) {
                            if (!d.result().isPresent()
                                    || food.getId().equals(d.result().get().getLong("id"))){

                                executeNoResult(jsonArray, sb.toString(), resultHandler);
                            } else {
                                ApiRouter.serviceUnavailable(context, CodeEnum.DISH_NAME_EXIST);
                            }
                        } else {
                            ApiRouter.serviceUnavailable(context, CodeEnum.SYS_ERROR);
                        }
                    });
        } else {

            JsonArray param = new JsonArray().add(food.getId());
            retrieveOne(param, QUERY_DISH_ID)
                    .setHandler(d -> {
                        if (d.succeeded() && d.result().isPresent()) {
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
     * 更新菜品状态
     * @param status 状态值
     * @param dishId 菜品id
     * @param resultHandler
     */
    public void updateDishStatus(Integer status, Integer dishId, Handler<AsyncResult<Void>> resultHandler){

        log.info("update dish: {}, status: {}",dishId, status);
        JsonArray jsonArray = new JsonArray()
                .add(status)
                .add(new Date().toInstant())
                .add(dishId);
        executeNoResult(jsonArray, UPDATE_DISH_STATUS, resultHandler);
    }

    /**
     * 更新菜品是否外卖
     * @param takeout 是否外卖状态值
     * @param dishId 菜品id
     * @param resultHandler
     */
    public void updateDishTakeout(Integer takeout, Integer dishId, Handler<AsyncResult<Void>> resultHandler){

        log.info("update dish: {}, takeout: {}",dishId, takeout);
        JsonArray jsonArray = new JsonArray()
                .add(takeout)
                .add(new Date().toInstant())
                .add(dishId);
        executeNoResult(jsonArray, UPDATE_DISH_TAKEOUT, resultHandler);
    }
}
