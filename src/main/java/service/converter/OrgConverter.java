/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package service.converter;

import java.util.Objects;

import entity.sys.Org;
import io.vertx.core.json.JsonArray;

/**
 *
 * @author tangyue
 * @version $Id: OrgConverter.java, v 0.1 2019-02-14 17:20 tangyue Exp $$
 */
public final class OrgConverter {

    public static JsonArray toJsonArray(Org org) {
        JsonArray jsonArray = new JsonArray();
        if (Objects.isNull(org)) {
            return jsonArray;
        }
        jsonArray.add(org.getParentId())
                .add(org.getCode())
                .add(org.getName())
                .add(org.getAddress())
                .add(org.getContact())
                .add(org.getContactName())
                .add(org.getDelFlag())
                .add(org.getCreateTime().toInstant())
                .add(org.getUpdateTime().toInstant());
        return jsonArray;
    }
}
