/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package service.converter;

import java.util.Objects;

import entity.sys.Dept;
import io.vertx.core.json.JsonArray;

/**
 *
 * @author tangyue
 * @version $Id: DeptConverter.java, v 0.1 2019-02-13 14:48 tangyue Exp $$
 */
public final class DeptConverter {

    public static JsonArray toJsonArray(Dept dept) {
        JsonArray jsonArray = new JsonArray();
        if (Objects.isNull(dept)) {
            return jsonArray;
        }
        jsonArray.add(dept.getOrgId())
                .add(dept.getName())
                .add(dept.getLevel())
                .add(dept.getRemark())
                .add(dept.getParentId())
                .add(dept.getDelFlag())
                .add(dept.getCreateTime().toInstant())
                .add(dept.getUpdateTime().toInstant());
        return jsonArray;
    }
}
