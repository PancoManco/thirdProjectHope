package controller;

import dto.ConversionRequestDto;
import dto.ConversionResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ConversionService;

import java.io.IOException;
import java.math.BigDecimal;

import static exception.ErrorMessages.ParameterError.CURRENCY_CODE_MISSING;
import static utils.JsonUtil.toJson;
import static utils.RequestParameterUtil.extractBigDecimal;
import static utils.RequestParameterUtil.validateParameters;
import static utils.ServletUtil.sendResponse;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    ConversionService conversionService;

    @Override
    public void init() throws ServletException {
        this.conversionService = (ConversionService) getServletContext().getAttribute("conversionService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String inputAmount = req.getParameter("amount");
        validateParameters(CURRENCY_CODE_MISSING, baseCurrencyCode, targetCurrencyCode); // дописать ошибку
        BigDecimal inputAmountBigDecimal = extractBigDecimal(inputAmount);
        ConversionRequestDto requestDto = new ConversionRequestDto(baseCurrencyCode, targetCurrencyCode, inputAmountBigDecimal);

        ConversionResponseDto responseDto = conversionService.convert(requestDto);
        sendResponse(resp, HttpServletResponse.SC_OK, toJson(responseDto));
    }

}
