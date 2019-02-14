/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package service.converter;

import java.util.Objects;

import entity.sys.Role;
import io.vertx.core.json.JsonArray;

/**
 *
 * @author tangyue
 * @version $Id: roleConverter.java, v 0.1 2019-02-13 14:48 tangyue Exp $$
 */
public final class RoleConverter {

    public static JsonArray toJsonArray(Role role) {
        JsonArray jsonArray = new JsonArray();
        if (Objects.isNull(role)) {
            return jsonArray;
        }
        jsonArray.add(role.getParentId())
                .add(role.getOrgId())
                .add(role.getName())
                .add(role.getRemark())
                .add(role.getDelFlag())
                .add(role.getCreateTime().toInstant())
                .add(role.getUpdateTime().toInstant());
        return jsonArray;
    }
}
