/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package service.converter;

import java.util.Objects;

import entity.DishFood;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author tangyue
 * @version $Id: DishFoodConverter.java, v 0.1 2018-06-14 10:34 tangyue Exp $$
 */
public final class DishFoodConverter {

    public static JsonArray toJsonArray(DishFood food){
        JsonArray jsonArray = new JsonArray();
        if (Objects.isNull(food)){
            return jsonArray;
        }

        jsonArray.add(food.getMerchantId())
                .add(food.getDishName())
                .add(food.getDishPrice().doubleValue())
                .add(food.getDishDiscountPrice())
                .add(food.getDishIcon())
                .add(food.getDishTakeout())
                .add(food.getRemark())
                .add(food.getDishStatus())
                .add(food.getCreateTime().toInstant())
                .add(food.getUpdateTime().toInstant());

        return jsonArray;
    }

    public static boolean isValid(JsonObject jsonObject){

        boolean valid = false;
        if (Objects.isNull(jsonObject.getValue("merchantId"))){
            return valid;
        }
        if (Objects.isNull(jsonObject.getValue("dishName"))){
            return valid;
        }
        if (Objects.isNull(jsonObject.getValue("dishPrice"))){
            return valid;
        }
        if (Objects.isNull(jsonObject.getValue("dishIcon"))){
            return valid;
        }
        if (Objects.isNull(jsonObject.getValue("dishTakeout"))){
            return valid;
        } else {
            valid = true;
        }
        return valid;
    }

}
