/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package db;

import java.util.List;
import java.util.Optional;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

/**
 * Helper and wrapper class for JDBC repository services.
 * @author tangyue
 * @version $Id: JdbcRepositoryWrapper.java, v 0.1 2019-01-23 15:52 tangyue Exp $$
 */
public class JdbcRepositoryWrapper {

    protected final SQLClient client;

    public JdbcRepositoryWrapper() {
        this.client = HikariCPManager.getInstance().getClient();
    }

    protected void executeNoResult(JsonArray params, String sql, Handler<AsyncResult<Void>> resultHandler) {
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

    protected <R> void execute(JsonArray params, String sql, R ret, Handler<AsyncResult<R>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> connection.updateWithParams(sql, params, r -> {
            if (r.succeeded()) {
                resultHandler.handle(Future.succeededFuture(ret));
            } else {
                resultHandler.handle(Future.failedFuture(r.cause()));
            }
            connection.close();
        })));
    }

    protected Future<Optional<JsonObject>> retrieveOne(JsonArray param, String sql) {
        return getConnection()
                .compose(connection -> {
                    Future<Optional<JsonObject>> future = Future.future();
                    connection.queryWithParams(sql, param, r -> {
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

    protected int calcPage(int page, int limit) {
        if (page <= 0)
            return 0;
        return limit * (page - 1);
    }


    protected Future<List<JsonObject>> retrieveMany(JsonArray param, String sql) {
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

    protected Future<List<JsonObject>> retrieveAll(String sql) {
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
     * A helper methods that generates async handler for SQLConnection
     *
     * @return generated handler
     */
    protected <R> Handler<AsyncResult<SQLConnection>> connHandler(Handler<AsyncResult<R>> h1, Handler<SQLConnection> h2) {
        return conn -> {
            if (conn.succeeded()) {
                final SQLConnection connection = conn.result();
                h2.handle(connection);
            } else {
                h1.handle(Future.failedFuture(conn.cause()));
            }
        };
    }

    protected Future<SQLConnection> getConnection() {
        Future<SQLConnection> future = Future.future();
        client.getConnection(future.completer());
        return future;
    }
}
