/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package service.converter;

import java.util.Objects;

import entity.sys.Menu;
import io.vertx.core.json.JsonArray;

/**
 *
 * @author tangyue
 * @version $Id: menuConverter.java, v 0.1 2019-02-13 14:48 tangyue Exp $$
 */
public final class MenuConverter {

    public static JsonArray toJsonArray(Menu menu) {
        JsonArray jsonArray = new JsonArray();
        if (Objects.isNull(menu)) {
            return jsonArray;
        }
        jsonArray.add(menu.getParentId())
                .add(menu.getName())
                .add(menu.getUrl())
                .add(menu.getIcon())
                .add(menu.getSortNum())
                .add(menu.getStatus())
                .add(menu.getRemark())
                .add(menu.getCreateTime().toInstant())
                .add(menu.getUpdateTime().toInstant());
        return jsonArray;
    }
}
