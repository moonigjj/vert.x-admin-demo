/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */

import com.google.common.collect.Lists;

import java.util.List;

import db.HikariCPManager;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import utils.CommonUtil;
import utils.NetworkUtil;
import web.AdminClientServer;

/**
 *
 * @author tangyue
 * @version $Id: Main.java, v 0.1 2018-03-05 13:44 tangyue Exp $$
 */
@Slf4j
public class Main {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
        // create the options for a properties file store
        ConfigStoreOptions jsonFile = new ConfigStoreOptions()
                .setType("file")
                .setFormat("json")
                .setConfig(new JsonObject().put("path", "config.json"));
        // add the options to the chain
        ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(jsonFile);

        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
        retriever.getConfig(ar -> {
            if (ar.succeeded()) {
                log.debug("config: {}", ar.result());
                JsonObject result = ar.result();
                //
                initComponents(result);
                deploy(vertx, AdminClientServer.class, new DeploymentOptions().setWorkerPoolSize(8));
            }

        });
    }
    // 初始化
    private static void initComponents(JsonObject config){
        CommonUtil.init(Vertx.vertx(), config);
        HikariCPManager.init();
        NetworkUtil.init();
    }

    /*public static class ServiceLauncher extends AbstractVerticle {

        @Override
        public void start(Future<Void> startFuture) {

            DeploymentOptions clientOpts = new DeploymentOptions().setWorkerPoolSize(4);
            List<Future> futures = Lists.newArrayList();
            futures.add(deploy(vertx, AdminClientServer.class,clientOpts));
            CompositeFuture.all(
                    futures
            ).setHandler(r -> {
                if (r.succeeded()){
                    startFuture.complete();
                } else {
                    log.error("start future fail: {}", r.cause());
                    startFuture.fail(r.cause());
                }
            });
        }


    }
*/
    private static Future<Void> deploy(Vertx vertx, Class verticle, DeploymentOptions opts){
        Future<Void> done = Future.future();
        String deploymentName = "dish-admin:" + verticle.getName();
        JsonObject config = new JsonObject();

        opts.setConfig(config);

        vertx.deployVerticle(deploymentName, opts, r -> {
            if(r.succeeded()){
                log.info("Successfully deployed verticle: {}", deploymentName);
                done.complete();
            }
            else {
                log.error("Failed to deploy verticle: {}", deploymentName);
                log.error("test: {}", r.cause());
                done.fail(r.cause());
            }
        });

        return done;
    }
}
