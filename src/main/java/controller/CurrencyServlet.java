package controller;

import dto.CurrencyDto;
import exception.EmptyException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import utils.JsonUtil;
import validation.CurrencyFormatter;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

import static utils.JsonUtil.errorToJson;

@WebServlet("/currencies")
public class CurrencyServlet extends HttpServlet {
    private CurrencyService currencyService = new CurrencyService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CurrencyDto> currencies = currencyService.getAllCurrencies();
        JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, currencies);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      /*
        CurrencyDto currencyDto = new ObjectMapper().readValue(req.getReader(), CurrencyDto.class);
        CurrencyDto createdCurrency = currencyService.create(currencyDto);
        JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_CREATED, createdCurrency); */
        try {
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String sign = req.getParameter("sign");
            CurrencyFormatter.validateParameters(code, name, sign);
            CurrencyDto currencyDto = new CurrencyDto(code,name,sign);
            CurrencyDto currency = currencyService.save(currencyDto);
            JsonUtil.sendJsonResponse(resp,HttpServletResponse.SC_CREATED,currency);
        }
        catch (EmptyException | InvalidParameterException e) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
        }

    }
}
