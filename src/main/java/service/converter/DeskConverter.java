/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package service.converter;

import java.util.Objects;

import entity.Desk;
import io.vertx.core.json.JsonArray;

/**
 *
 * @author tangyue
 * @version $Id: DeskConverter.java, v 0.1 2018-06-11 15:54 tangyue Exp $$
 */
public class DeskConverter {

    public static JsonArray toJsonArray(Desk desk){
        JsonArray jsonArray = new JsonArray();
        if (Objects.isNull(desk)){
            return jsonArray;
        }
        jsonArray.add(desk.getMerchantId())
                .add(desk.getDeskNum())
                .add(desk.getUrl())
                .add(desk.getRemark())
                .add(desk.getDeskStatus())
                .add(desk.getCreateTime().toInstant())
                .add(desk.getUpdateTime().toInstant());
        return jsonArray;
    }
}
