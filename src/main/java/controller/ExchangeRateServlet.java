package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ExchangeRateDto;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utils.JsonUtil.toJson;
import static utils.RequestParameterUtil.*;
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
        String codes = extractTrimmedPath(req,"Код валюты пары отсутвует в запросе");
        String inputRate = getRateParameter(req);
        validateParameters("Отсутвует нужное поле формы", inputRate);
        BigDecimal rate = extractBigDecimal(inputRate);
        ExchangeRateDto exchangeRate = exchangeRateService.updateExchangeRate(codes, rate);
        sendResponse(resp, HttpServletResponse.SC_OK, toJson(exchangeRate));
    }
    private static String getRateParameter(HttpServletRequest req) throws IOException {
        String stringRate = "";
        BufferedReader reader = req.getReader();
        StringBuilder formBody = new StringBuilder();

        while (reader.ready()) {
            formBody.append(reader.readLine());
        }

        Pattern pattern = Pattern.compile("rate=(.*?)(&|$)");
        Matcher matcher = pattern.matcher(formBody.toString());

        if (matcher.find()) {
            stringRate = matcher.group(1); // Получаем первое найденное значение
        }

        // Замена запятых на точки (для универсального представления дробных чисел)
        stringRate = stringRate.replace(',', '.');

        // Удаляем все неприемлемые символы, оставляя только цифры и точку
        stringRate = stringRate.replaceAll("[^\\d.-]", "");

        return stringRate.trim(); // Возвращаем очищенное число
    }

}
