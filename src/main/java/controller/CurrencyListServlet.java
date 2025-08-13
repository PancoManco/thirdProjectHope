package controller;

import dto.CurrencyDto;
import dto.CurrencyDtoRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;

import java.io.IOException;
import java.util.List;

import static exception.ErrorMessages.ParameterError.REQUIRED_FORM_FIELD_MISSING;
import static utils.JsonUtil.toJson;
import static utils.RequestParameterUtil.validateParameters;
import static utils.ServletUtil.sendResponse;

@WebServlet("/currencies")
public class CurrencyListServlet extends HttpServlet {
    private CurrencyService currencyService;

    @Override
    public void init() throws ServletException {
        this.currencyService = (CurrencyService) getServletContext().getAttribute("currencyService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<CurrencyDto> currencies = currencyService.getAllCurrencies();
        sendResponse(resp, HttpServletResponse.SC_OK, toJson(currencies));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        validateParameters(REQUIRED_FORM_FIELD_MISSING, code, name, sign);
        CurrencyDtoRequest currencyDto = new CurrencyDtoRequest(code, name, sign);
        CurrencyDto currency = currencyService.save(currencyDto);
        sendResponse(resp, HttpServletResponse.SC_CREATED, toJson(currency));
    }
}
