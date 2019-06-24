/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package service.dish;

import java.util.List;

import db.JdbcRepositoryWrapper;
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

    private static final String BASE = "id , merchant_id merchantId, order_num orderNum, user_id userId, desk_num deskNum, " +
            "buyer_name buyerName, dish_amount amount, dish_price price, pay_status payStatus, DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s') createTime";

    private static final String QUERY_ALL_PAGE = "SELECT "+ BASE +" FROM dish_order ";

    private static final String QUERY_ORDER_ID = "SELECT "+ BASE +" FROM dish_order where id = ?";


    /**
     * 订单列表
     * @param params
     * @param page
     * @param size
     * @param resultHandler
     */
    public void orderListPage(JsonObject params, int page, int size, Handler<AsyncResult<List<JsonObject>>> resultHandler){

        log.info("start dish list params: {}", params);
        JsonArray jsonArray = new JsonArray().add(params.getString("merchantId"));
        StringBuffer sb = new StringBuffer(QUERY_ALL_PAGE);
        if (StrUtil.isNotBlank(params.getString("orderNum"))){
            sb.append(" and orderNum = ?");
            jsonArray.add(params.getString("orderNum"));
        }
        sb.append(" order by create_time desc limit ?, ?");
        jsonArray.add(calcPage(page, size)).add(size);
        retrieveMany(jsonArray, sb.toString())
                .setHandler(resultHandler);
    }

    /**
     * 菜品信息
     * @param orderId 订单id
     * @param resultHandler
     */
    public void orderInfo(String orderId, Handler<AsyncResult<JsonObject>> resultHandler){

        JsonArray params = new JsonArray().add(orderId);
        retrieveOne(params, QUERY_ORDER_ID)
                .map(option -> option.orElse(null))
                .setHandler(resultHandler);
    }

}
