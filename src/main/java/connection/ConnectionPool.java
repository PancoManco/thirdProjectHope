package connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import exception.DBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertiesUtil;
import java.sql.Connection;
import java.sql.SQLException;

import static exception.ErrorMessages.DataBaseError.ERROR_CONNECTION_POOL;
import static exception.ErrorMessages.DataBaseError.ERROR_LOADING_PROPERTIES;

public final class ConnectionPool {
    private static HikariConfig config = new HikariConfig();
    private static final HikariDataSource dataSource;
    static {
        try {
            config.setJdbcUrl(PropertiesUtil.get("db.url"));
            config.setUsername(PropertiesUtil.get("db.username"));
            config.setPassword(PropertiesUtil.get("db.password"));
            config.setDriverClassName(PropertiesUtil.get("db.driver"));
            config.setMaximumPoolSize(Integer.parseInt(PropertiesUtil.get("hikari.maximum.pool.size")));
            config.setMinimumIdle(Integer.parseInt(PropertiesUtil.get("hikari.minimum.idle")));
            config.setConnectionTimeout(Integer.parseInt(PropertiesUtil.get("hikari.connection.timeout")));
            config.setIdleTimeout(Integer.parseInt(PropertiesUtil.get("hikari.idle.connection.timeout")));
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new DBException(ERROR_LOADING_PROPERTIES);
        }
    }
    public static Connection getConnection() {
        try {
           return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DBException(ERROR_CONNECTION_POOL);
        }
    }

    public static void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
    private ConnectionPool() {
    }
}
