package validation;

import dto.CurrencyDto;

import java.security.InvalidParameterException;
import java.util.regex.Pattern;

import static exception.ErrorMessages.ParameterError.*;

public final class CurrencyFormatter {
    private CurrencyFormatter() {
    }

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
        Pattern pattern = Pattern.compile("^[a-zA-z ]{1,30}$");
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

    public static CurrencyDto getValidCurrencyDTO(CurrencyDto currencyDto) {
        String code = getValidCode(currencyDto.getCode());
        String fullName = getValidName(currencyDto.getName());
        String sign = getValidSign(currencyDto.getSign());
        CurrencyDto validCurrencyDto = new CurrencyDto(code, fullName, sign);
        return validCurrencyDto;
    }

/*
    public static boolean isValidCode(String code) {
        code = code.trim();
        Pattern pattern = Pattern.compile("^[A-Z]{3}$");
        if (!pattern.matcher(code).matches()) {
            throw new InvalidParameterException(CURRENCY_CODE_INVALID);
        }
        return true;
    }

    public static boolean isValidName(String name) {
        name = name.trim();
        Pattern pattern = Pattern.compile("^[a-zA-z ]{1,30}$");
        if (!pattern.matcher(name).matches()) {
            throw new InvalidParameterException(CURRENCY_NAME_INVALID);
        }
        return true;
    }

    public static boolean isValidSign(String sign) {
        sign = sign.trim();
        Pattern pattern = Pattern.compile("^[^\\p{P}\\s]{1,3}$");
        if (!pattern.matcher(sign).matches()) {
            throw new InvalidParameterException(SIGN_INVALID);
        }
        return true;
    }
*/


}
