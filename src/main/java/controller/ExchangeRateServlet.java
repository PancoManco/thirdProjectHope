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
    String codes = extractValidatedPath(req,"Код валюты пары отсутсвует в адресе"); //todo
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
        String codes = extractValidatedPath(req,"Код валюты пары отсутвует в запросе"); // todo
        String inputRate = getRateParameter(req);
        validateParameters("Отсутвует нужное поле формы", inputRate); // todo
        BigDecimal rate = extractBigDecimal(inputRate);
        ExchangeRateDto exchangeRate = exchangeRateService.updateExchangeRate(codes, rate);
        sendResponse(resp, HttpServletResponse.SC_OK, toJson(exchangeRate));
    }


}
