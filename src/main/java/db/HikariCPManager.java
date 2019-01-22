/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package db;

import java.util.List;
import java.util.Optional;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import utils.CommonUtil;

import static utils.CommonUtil.JDBC_DRIVER;
import static utils.CommonUtil.JDBC_PSWD;
import static utils.CommonUtil.JDBC_URL;
import static utils.CommonUtil.JDBC_USER;

/**
 * ConnectionPoolManager接口的HikariCPi连接池实现类
 * @author tangyue
 * @version $Id: HikariCPManager.java, v 0.1 2018-05-11 9:22 tangyue Exp $$
 */

public class HikariCPManager {

    private volatile static HikariCPManager INSTANCE = null;
    private SQLClient client;

    /**
     * 因为是单例，这个唯一的构造器是私有的
     * 主要任务是初始化连接池
     * 从vertx配置中读取u数据库相关配置，然后创建JDBCClient，保存到私有变量
     * @author Leibniz.Hu
     * @param ctx
     */
    private HikariCPManager(Context ctx) {
        JsonObject vertxConfig = ctx.config();
        JsonObject config = new JsonObject()
                .put("provider_class", vertxConfig.getString("provider_class", "io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider"))
                .put("jdbcUrl", JDBC_URL)
                .put("driverClassName", JDBC_DRIVER)
                .put("username", JDBC_USER)
                .put("password", JDBC_PSWD)
                .put("minimumIdle", vertxConfig.getInteger("minimumIdle", 2))
                .put("maximumPoolSize", vertxConfig.getInteger("maximumPoolSize", 10));
        this.client = PostgreSQLClient.createShared(ctx.owner(), config, "HikariCP");
    }

    /**
     * 初始化的方法
     * 传入Vertx对象，用于调用私有构造器，产生单例对象
     */
    public static HikariCPManager init() {
        if (INSTANCE != null) {
            throw new RuntimeException("HikariCPManager is already initialized, please do not call init() any more!!!");
        }
        Vertx vertx = CommonUtil.vertx();
        if(vertx == null){
            throw new RuntimeException("请先初始化Constants类！");
        }
        INSTANCE = new HikariCPManager(CommonUtil.vertxContext()); //创建单例实例

        INSTANCE.log.info("HikariCP连接池初始化成功！");
        return INSTANCE;
    }

    /**
     * 获取单例对象的方法
     */
    public static HikariCPManager getInstance() {
        if (INSTANCE == null) {
            throw new RuntimeException("HikariCPManager is still not initialized!!!");
        }
        return INSTANCE;
    }

    public static void close(){
        INSTANCE.client.close(res -> INSTANCE.log.info("HikariCP连接池关闭" + (res.succeeded()?"成功":"失败")));
    }

    public SQLClient getSQLClient() {
        return this.client;
    }

    /**
     * Suitable for `add`, `exists` operation.
     *
     * @param params        query params
     * @param sql           sql
     * @param resultHandler async result handler
     */
    public void executeNoResult(JsonArray params, String sql, Handler<AsyncResult<Void>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> {
            connection.updateWithParams(sql, params, r -> {
                if (r.succeeded()) {
                    resultHandler.handle(Future.succeededFuture());
                } else {
                    resultHandler.handle(Future.failedFuture(r.cause()));
                }
                connection.close();
            });
        }));
    }

    /**
     * 无返回结果
     * @param params
     * @param sql
     * @return
     */
    public Future<Void> executeNoResult(JsonArray params, String sql) {
        return getConnection()
                .compose(connection -> {
                    Future<Void> future = Future.future();
                    connection.updateWithParams(sql, params, r -> {
                        if (r.succeeded()) {
                            future.complete();
                        } else {
                            future.fail(r.cause());
                        }
                        connection.close();
                    });
                    return future;
                });
    }

    /**
     * 批量插入--带参数
     * @param params
     * @param sql
     */
    public Future<Void> batchParams(List<JsonArray> params, String sql) {

        return getConnection()
                .compose(connection -> {
                    Future<Void> future = Future.future();
                    connection.batchWithParams(sql, params, r -> {
                        if (r.succeeded()) {
                            future.complete();
                        } else {
                            future.fail(r.cause());
                        }
                        connection.close();
                    });
                    return future;
                });
    }

    public  <R> void execute(JsonArray params, String sql, R ret, Handler<AsyncResult<R>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> {
            connection.updateWithParams(sql, params, r -> {
                if (r.succeeded()) {
                    resultHandler.handle(Future.succeededFuture(ret));
                } else {
                    resultHandler.handle(Future.failedFuture(r.cause()));
                }
                connection.close();
            });
        }));
    }

    public  Future<Optional<JsonObject>> queryOne(JsonArray params, String sql) {
        return getConnection()
                .compose(connection -> {
                    Future<Optional<JsonObject>> future = Future.future();
                    connection.queryWithParams(sql, params, r -> {
                        if (r.succeeded()) {
                            List<JsonObject> resList = r.result().getRows();
                            if (resList == null || resList.isEmpty()) {
                                future.complete(Optional.empty());
                            } else {
                                future.complete(Optional.of(resList.get(0)));
                            }
                        } else {
                            future.fail(r.cause());
                        }
                        connection.close();
                    });
                    return future;
                });
    }

    public int calcPage(int page, int limit) {
        if (page <= 0)
            return 0;
        return limit * (page - 1);
    }

/*    public Future<List<JsonObject>> queryByPage(int page, int limit, String sql) {
        JsonArray params = new JsonArray().add(calcPage(page, limit)).add(limit);
        return getConnection().compose(connection -> {
            Future<List<JsonObject>> future = Future.future();
            connection.queryWithParams(sql, params, r -> {
                if (r.succeeded()) {
                    future.complete(r.result().getRows());
                } else {
                    future.fail(r.cause());
                }
                connection.close();
            });
            return future;
        });
    }*/

    /**
     * 条件查询
     * @param param
     * @param sql
     * @return
     */
    public Future<List<JsonObject>> queryMany(JsonArray param, String sql) {
        return getConnection().compose(connection -> {
            Future<List<JsonObject>> future = Future.future();
            connection.queryWithParams(sql, param, r -> {
                if (r.succeeded()) {
                    future.complete(r.result().getRows());
                } else {
                    future.fail(r.cause());
                }
                connection.close();
            });
            return future;
        });
    }
    /**
     * 获取所有
     * @param sql
     * @return
     */
    public Future<List<JsonObject>> queryAll(String sql) {
        return getConnection().compose(connection -> {
            Future<List<JsonObject>> future = Future.future();
            connection.query(sql, r -> {
                if (r.succeeded()) {
                    future.complete(r.result().getRows());
                } else {
                    future.fail(r.cause());
                }
                connection.close();
            });
            return future;
        });
    }

    /**
     * 删除一条记录
     * @param id
     * @param sql
     * @param resultHandler
     * @param <K>
     */
    /*protected <K> void removeOne(K id, String sql, Handler<AsyncResult<Void>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> {
            JsonArray params = new JsonArray().add(id);
            connection.updateWithParams(sql, params, r -> {
                if (r.succeeded()) {
                    resultHandler.handle(Future.succeededFuture());
                } else {
                    resultHandler.handle(Future.failedFuture(r.cause()));
                }
                connection.close();
            });
        }));
    }*/

    /**
     * 删除所有
     * @param sql
     * @param resultHandler
     *//*
    protected void removeAll(String sql, Handler<AsyncResult<Void>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> {
            connection.update(sql, r -> {
                if (r.succeeded()) {
                    resultHandler.handle(Future.succeededFuture());
                } else {
                    resultHandler.handle(Future.failedFuture(r.cause()));
                }
                connection.close();
            });
        }));
    }*/


    /**
     * A helper methods that generates async handler for SQLConnection
     *
     * @return generated handler
     */
    public  <R> Handler<AsyncResult<SQLConnection>> connHandler(Handler<AsyncResult<R>> h1, Handler<SQLConnection> h2) {
        return conn -> {
            if (conn.succeeded()) {
                final SQLConnection connection = conn.result();
                h2.handle(connection);
            } else {
                h1.handle(Future.failedFuture(conn.cause()));
            }
        };
    }

    /**
     * 获取连接
     * @return
     */
    private Future<SQLConnection> getConnection() {
        Future<SQLConnection> future = Future.future();
        client.getConnection(future.completer());
        return future;
    }
}
