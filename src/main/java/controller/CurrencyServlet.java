package controller;

import dto.CurrencyDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;

import java.io.IOException;

import static exception.ErrorMessages.ParameterError.CURRENCY_CODE_MISSING;
import static utils.JsonUtil.toJson;
import static utils.RequestParameterUtil.extractValidatedPath;
import static utils.ServletUtil.sendResponse;


@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyService currencyService;

    @Override
    public void init() throws ServletException {
        this.currencyService = (CurrencyService) getServletContext().getAttribute("currencyService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = extractValidatedPath(req, CURRENCY_CODE_MISSING);
        CurrencyDto currencyDto = currencyService.getCurrencyByCode(code);
        sendResponse(resp, HttpServletResponse.SC_OK, toJson(currencyDto));
    }
}
