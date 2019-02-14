/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package service.converter;

import java.util.Objects;

import entity.sys.User;
import io.vertx.core.json.JsonArray;

/**
 *
 * @author tangyue
 * @version $Id: userConverter.java, v 0.1 2019-02-13 14:48 tangyue Exp $$
 */
public final class UserConverter {

    public static JsonArray toJsonArray(User user) {
        JsonArray jsonArray = new JsonArray();
        if (Objects.isNull(user)) {
            return jsonArray;
        }
        jsonArray.add(user.getOrgId())
                .add(user.getUserName())
                .add(user.getRealName())
                .add(user.getNickName())
                .add(user.getAvatar())
                .add(user.getPwd())
                .add(user.getSalt())
                .add(user.getOpenId())
                .add(user.getUnionId())
                .add(user.getStatus())
                .add(user.getOnline())
                .add(user.getDelFlag())
                .add(user.getCreateTime().toInstant())
                .add(user.getUpdateTime().toInstant());
        return jsonArray;
    }
}
