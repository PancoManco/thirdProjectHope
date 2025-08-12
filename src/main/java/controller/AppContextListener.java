package controller;

import connection.ConnectionPool;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import service.ConversionService;
import service.CurrencyService;
import service.ExchangeRateService;

public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
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
