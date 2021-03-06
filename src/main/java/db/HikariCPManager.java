/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package db;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import lombok.extern.slf4j.Slf4j;
import utils.CommonUtil;

/**
 * ConnectionPoolManager接口的HikariCPi连接池实现类
 * @author tangyue
 * @version $Id: HikariCPManager.java, v 0.1 2018-05-11 9:22 tangyue Exp $$
 */
@Slf4j
public final class HikariCPManager {

    private volatile static HikariCPManager INSTANCE = null;
    private final SQLClient client;

    /**
     * 因为是单例，这个唯一的构造器是私有的
     * 主要任务是初始化连接池
     * 从vertx配置中读取u数据库相关配置，然后创建JDBCClient，保存到私有变量
     * @author Leibniz.Hu
     */
    private HikariCPManager() {
        JsonObject config = CommonUtil.getConfig();
        JsonObject dbConfig = new JsonObject()
                .put("provider_class", config.getString("provider_class", "io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider"))
                .put("jdbcUrl", config.getString("jdbcUrl", "jdbc:postgresql://localhost:5432/auth"))
                .put("driverClassName", config.getString("driverClassName", "org.postgresql.Driver"))
                .put("username", config.getString("username", "postgres"))
                .put("password", config.getString("password", "gs2019"))
                .put("minimumIdle", 2)
                .put("maximumPoolSize", 30)
                .put("cachePrepStmts", true)
                .put("prepStmtCacheSize", 250)
                .put("prepStmtCacheSqlLimit", 2048);
        this.client = JDBCClient.createShared(CommonUtil.vertx(), dbConfig, "HikariCP");
    }

    public SQLClient getClient() {
        return this.client;
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
        INSTANCE = new HikariCPManager(); //创建单例实例

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
}
