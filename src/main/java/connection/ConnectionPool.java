package connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import exception.DBException;
import utils.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionPool implements AutoCloseable{

    private static HikariConfig config = new HikariConfig(ConnectionPool.class.getClassLoader().getResourceAsStream("hikari.properties"));
    private static HikariDataSource ds;

    static {
        try {
            ds = new HikariDataSource(config);
        } catch (Exception e) {
            throw new DBException("Ошибка при загрузке конфигурации" + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
           return ds.getConnection();
        } catch (SQLException e) {
            throw new DBException("Ошибка получения соединения пула " + e.getMessage());
        }
    }
    private ConnectionPool() {
    }

    @java.lang.Override
    public void close() throws Exception {
        if (ds != null && !ds.isClosed()) {
            ds.close();
        }
    }
}
