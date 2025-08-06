package controller;

import dto.CurrencyDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;

import java.io.IOException;
import java.util.List;

import static utils.JsonUtil.toJson;
import static utils.RequestParameterUtil.validateParameters;
import static utils.ServletUtil.sendResponse;

@WebServlet("/currencies")
public class CurrencyListServlet extends HttpServlet {
    private CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<CurrencyDto> currencies = currencyService.getAllCurrencies();
        sendResponse(resp, HttpServletResponse.SC_OK, toJson(currencies));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        validateParameters(code, name, sign);
        CurrencyDto currencyDto = new CurrencyDto(code, name, sign);
        CurrencyDto currency = currencyService.save(currencyDto);
        sendResponse(resp, HttpServletResponse.SC_CREATED, toJson(currency));
    }
}
