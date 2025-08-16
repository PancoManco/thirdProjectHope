package controller;

import connection.ConnectionPool;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ConversionService;
import service.CurrencyService;
import service.ExchangeRateService;

import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
        } catch (Exception e) {
            logger.error("Ошибка при инициализации подключения к БД", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.warn("Не удалось закрыть соединение после инициализации", e);
                }
            }
        }
        CurrencyService currencyService = new CurrencyService();
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        ConversionService conversionService = new ConversionService();
        sce.getServletContext().setAttribute("currencyService", currencyService);
        sce.getServletContext().setAttribute("exchangeRateService", exchangeRateService);
        sce.getServletContext().setAttribute("conversionService", conversionService);
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.close();
    }
}
