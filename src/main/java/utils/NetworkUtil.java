/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package utils;

import java.util.Objects;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author tangyue
 * @version $Id: NetworkUtils.java, v 0.1 2019-01-21 14:20 tangyue Exp $$
 */
public final class NetworkUtil {

    private static HttpClient client;

    public enum ContentType {
        JSON, XML, FORM
    }

    public static void init() {
        Vertx vertx = CommonUtil.vertx();
        if (Objects.isNull(vertx)) {
            throw new RuntimeException("init CommonUtil");
        }
        client = vertx.createHttpClient(
                new HttpClientOptions()
                        .setLogActivity(false)
                        .setKeepAlive(true)
                        .setIdleTimeout(10)
                        .setConnectTimeout(2000).setMaxWaitQueueSize(10).setMaxPoolSize(100)
        );
    }

    public static void asyncPostStringWithData(String url, String body, ContentType type) {
        asyncPostStringWithData(url, body, type, "UTF-8");
    }

    public static void asyncPostStringWithData(String url, String body, ContentType type, String encode) {
        checkInitialized();
        HttpClientRequest req = client.requestAbs(HttpMethod.POST, url);
        switch (type) {
            case XML:
                req.putHeader("content-type", "application/xml;charset=" + encode);
                break;
            case JSON:
                req.putHeader("content-type", "application/json;charset=" + encode);
                break;
            case FORM:
                req.putHeader("content-type", "application/x-www-form-urlencoded" + encode);
                break;
        }
        req.end(body, encode);
    }

    public static void asyncPostJson(String url, Handler<JsonObject> callback) {
        asyncRequestJson(HttpMethod.POST, url, callback);
    }

    public static void asyncPostJson(String url, Future<JsonObject> callback) {
        asyncRequestJson(HttpMethod.POST, url, callback);
    }

    public static void asyncPostString(String url, Handler<String> callback) {
        asyncRequestString(HttpMethod.POST, url, callback);
    }

    public static void asyncGetJson(String url, Handler<JsonObject> callback) {
        asyncRequestJson(HttpMethod.GET, url, callback);
    }

    public static void asyncGetString(String url, Handler<String> callback) {
        asyncRequestString(HttpMethod.GET, url, callback);
    }

    private static void asyncRequestString(HttpMethod method, String url, Handler<String> callback){
        checkInitialized();
        client.requestAbs(method, url, resp -> resp.bodyHandler(buf -> callback.handle(buf.toString()))).end();
    }

    private static void asyncRequestJson(HttpMethod method, String url, Handler<JsonObject> callback){
        checkInitialized();
        client.requestAbs(method, url, resp -> resp.bodyHandler(buf -> {
            callback.handle(buf.toJsonObject());
        })).end();
    }

    private static void asyncRequestJson(HttpMethod method, String url, Future<JsonObject> callback){
        checkInitialized();
        client.requestAbs(method, url, resp -> resp.bodyHandler(buf -> callback.complete(buf.toJsonObject()))).end();
    }

    private static void checkInitialized() {
        if(client == null){
            throw new IllegalStateException("Please set Vertx before you call getSubRouter()!!!");
        }
    }

}
