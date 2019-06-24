/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package service.dish;

import java.util.ArrayList;
import java.util.List;

import db.JdbcRepositoryWrapper;
import entity.dish.Order;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import utils.StrUtil;

/**
 *
 * @author tangyue
 * @version $Id: OrderService.java, v 0.1 2018-06-14 17:24 tangyue Exp $$
 */
@Slf4j
public class OrderService extends JdbcRepositoryWrapper {

    private static final String BASE = "id , org_id, sn, user_id, desk_num, " +
            "order_amount, order_price, order_pay, order_status, pay_status, pay_method, create_time";

    private static final String QUERY_ALL_PAGE = "SELECT "+ BASE +" FROM d_order ";

    private static final String QUERY_ORDER_ID = "SELECT "+ BASE +" FROM d_order where id = ?";


    /**
     * 订单列表
     * @param params
     * @param page
     * @param size
     * @param resultHandler
     */
    public void orderListPage(JsonObject params, int page, int size, Handler<AsyncResult<List<Order>>> resultHandler){

        log.info("start dish list params: {}", params);
        JsonArray jsonArray = new JsonArray().add(params.getString("orgId"));
        StringBuffer sb = new StringBuffer(QUERY_ALL_PAGE);
        if (StrUtil.isNotBlank(params.getString("sn"))){
            sb.append(" and sn = ?");
            jsonArray.add(params.getString("sn"));
        }
        sb.append(" order by create_time desc limit ?, ?");
        jsonArray.add(calcPage(page, size)).add(size);
        retrieveMany(jsonArray, sb.toString())
                .map(list -> {
                    List<Order> orders = new ArrayList<>();
                    list.forEach(json -> orders.add(json.mapTo(Order.class)));
                    return orders;
                })
                .setHandler(resultHandler);
    }

    /**
     * 菜品信息
     * @param orderId 订单id
     * @param resultHandler
     */
    public void orderInfo(String orderId, Handler<AsyncResult<Order>> resultHandler){

        JsonArray params = new JsonArray().add(orderId);
        retrieveOne(params, QUERY_ORDER_ID)
                .map(option -> option.orElse(null))
                .map(order -> order.mapTo(Order.class))
                .setHandler(resultHandler);
    }

}
