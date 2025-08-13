package Formatter;

import dao.CurrencyDao;
import dto.CurrencyDtoRequest;
import exception.EntityNotFoundException;
import model.Currency;

import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.regex.Pattern;

import static exception.ErrorMessages.ParameterError.*;

public final class CurrencyFormatter {

   private static CurrencyDao currencyDao= CurrencyDao.getInstance();

    public static String getValidCode(String code) {
        code = code.trim();
        Pattern pattern = Pattern.compile("^[A-Z]{3}$");
        if (!pattern.matcher(code).matches()) {
            throw new InvalidParameterException(CURRENCY_CODE_INVALID);
        }
        return code;
    }

    public static String getValidName(String name) {
        name = name.trim();
        Pattern pattern = Pattern.compile("^(?!.*--)[a-zA-Z-]{1,30}$");
        if (!pattern.matcher(name).matches()) {
            throw new InvalidParameterException(CURRENCY_NAME_INVALID);
        }
        return name;
    }

    public static String getValidSign(String sign) {
        sign = sign.trim();
        Pattern pattern = Pattern.compile("^[^\\p{P}\\s]{1,3}$");
        if (!pattern.matcher(sign).matches()) {
            throw new InvalidParameterException(SIGN_INVALID);
        }
        return sign;
    }

    ///  todo need to use
    public static CurrencyDtoRequest getValidCurrencyDto(CurrencyDtoRequest creatingRequest) {
        String code = getValidCode(creatingRequest.getCode());
        String fullName = getValidName(creatingRequest.getName());
        String sign = getValidSign(creatingRequest.getSign());

        return new CurrencyDtoRequest(code, fullName, sign);
    }
    // todo
    public static void validateCurrenciesExistence(String baseCurrency, String targetCurrency)  {
        Optional<Currency> baseOpt = currencyDao.findByCode(baseCurrency);
        Optional<Currency> targetOpt = currencyDao.findByCode(targetCurrency);
        if (!baseOpt.isPresent() || !targetOpt.isPresent()) {
            throw new EntityNotFoundException("Валюта '" + baseCurrency + "' или '" + targetCurrency + "' отсутствует в базе данных. Од");
        }
    }

    private CurrencyFormatter() {
    }
}
