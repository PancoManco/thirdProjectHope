package Validation;

import dto.CurrencyDtoRequest;

import java.security.InvalidParameterException;
import java.util.regex.Pattern;

import static exception.ErrorMessages.ParameterError.*;

public final class CurrencyValidator {

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
        Pattern pattern = Pattern.compile("^(?!.*--)[a-zA-Z\\s-]{1,30}$");
        if (!pattern.matcher(name).matches()) {
            throw new InvalidParameterException(CURRENCY_NAME_INVALID);
        }
        return name;
    }

    public static String getValidSign(String sign) {
        sign = sign.trim();
        Pattern pattern = Pattern.compile("^[^\\p{Z}\\p{P}-]{1,3}$");
        if (!pattern.matcher(sign).matches()) {
            throw new InvalidParameterException(SIGN_INVALID);
        }
        return sign;
    }


    public static CurrencyDtoRequest getValidCurrencyDto(CurrencyDtoRequest creatingRequest) {
        String code = getValidCode(creatingRequest.getCode());
        String fullName = getValidName(creatingRequest.getName());
        String sign = getValidSign(creatingRequest.getSign());

        return new CurrencyDtoRequest(code, fullName, sign);
    }

    private CurrencyValidator() {
    }
}
