package controller;

import dto.ExchangeRateDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;

import java.io.IOException;
import java.math.BigDecimal;

import static exception.ErrorMessages.ExchangeRatesError.EXCHANGE_PAIR_CODE_MISSING;
import static exception.ErrorMessages.ParameterError.REQUIRED_FORM_FIELD_MISSING;
import static utils.JsonUtil.toJson;
import static utils.RequestParameterUtil.*;
import static utils.ServletUtil.sendResponse;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService;

    @Override
    public void init() throws ServletException {
        this.exchangeRateService = (ExchangeRateService) getServletContext().getAttribute("exchangeRateService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codes = extractValidatedPath(req, EXCHANGE_PAIR_CODE_MISSING);
        ExchangeRateDto exchangeRate = exchangeRateService.findExchangeRateByCode(codes);
        sendResponse(resp, HttpServletResponse.SC_OK, toJson(exchangeRate));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
            return;
        }
        this.doPatch(req, resp);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codes = extractValidatedPath(req, EXCHANGE_PAIR_CODE_MISSING);
        String inputRate = getRateParameter(req);
        validateParameters(REQUIRED_FORM_FIELD_MISSING, inputRate);
        BigDecimal rate = extractBigDecimal(inputRate);
        ExchangeRateDto exchangeRate = exchangeRateService.updateExchangeRate(codes, rate);
        sendResponse(resp, HttpServletResponse.SC_OK, toJson(exchangeRate));
    }


}
