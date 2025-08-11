package controller;

import dto.CurrencyDto;
import dto.ExchangeRateDto;
import dto.ExchangeRateRequestDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ExchangeRate;
import service.ExchangeRateService;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static utils.JsonUtil.toJson;
import static utils.RequestParameterUtil.extractBigDecimal;
import static utils.ServletUtil.sendResponse;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    ExchangeRateService exchangeRateService = new ExchangeRateService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<ExchangeRateDto> exchangeRates = exchangeRateService.getAllExchangeRates();
        sendResponse(resp, HttpServletResponse.SC_CREATED, toJson(exchangeRates));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String baseCurrency = req.getParameter("baseCurrencyCode");
    String targetCode = req.getParameter("targetCurrencyCode");
    String inputRate = req.getParameter("rate");
    BigDecimal rate = extractBigDecimal(inputRate);// добавить проверку на числа
        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCurrency, targetCode, rate);
    ExchangeRateDto exchangeRateDto =exchangeRateService.createExchangeRate(exchangeRateRequestDto);
    sendResponse(resp,HttpServletResponse.SC_OK,toJson(exchangeRateDto));
    }
}
