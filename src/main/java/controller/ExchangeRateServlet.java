package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ExchangeRateDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import static utils.JsonUtil.toJson;
import static utils.RequestParameterUtil.extractBigDecimal;
import static utils.RequestParameterUtil.extractTrimmedPath;
import static utils.ServletUtil.sendResponse;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService= new ExchangeRateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String codes = extractTrimmedPath(req,"Код валюты пары отсутсвует в адресе"); //todo
    ExchangeRateDto exchangeRate = exchangeRateService.fingExchangeRateByCode(codes);
        sendResponse(resp, HttpServletResponse.SC_OK, toJson(exchangeRate));
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String codes = extractTrimmedPath(req,"Отсутсвует нужное поле формы");
        String inputRate = req.getParameter("rate");
        BigDecimal rate = extractBigDecimal(inputRate);

        ExchangeRateDto exchangeRate = exchangeRateService.updateExchangeRate(codes, rate);
        sendResponse(resp, HttpServletResponse.SC_OK, toJson(exchangeRate));
    }
}
