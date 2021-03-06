/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package utils;

import java.util.HashSet;
import java.util.Set;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tangyue
 * @version $Id: CommonUtil.java, v 0.1 2018-02-28 15:06 tangyue Exp $$
 */
@Slf4j
public final class CommonUtil {

    private CommonUtil(){

        throw new RuntimeException("");
    }

    private static Vertx vertx;
    private static JsonObject config;


    public static Vertx vertx() {
        return vertx;
    }


    public static JsonObject getConfig() {
        return config;
    }

    public static void init(Vertx vertx, JsonObject config) {
        CommonUtil.vertx = vertx;
        CommonUtil.config = config;
    }

    public static Set<String> getAllowedHeaders(){

        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        return allowHeaders;
    }

    public static Set<HttpMethod> getAllowedMethods(){

        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);
        return allowMethods;
    }
}
