package validation;

import dto.CurrencyPair;

public class ExchangeRateFormatter {

    private static int CODE_LENGTH = 3;

    public static CurrencyPair parseValidCodePair(String inputCodes) {
        String cleanedInputCodes = inputCodes.trim();
        String baseCurrencyCode = cleanedInputCodes.substring(0,CODE_LENGTH);
        String targetCurrencyCode = cleanedInputCodes.substring(CODE_LENGTH,CODE_LENGTH*2);
        return getValidCodePair(baseCurrencyCode,targetCurrencyCode);
    }

    public static CurrencyPair getValidCodePair(String inputBaseCode, String inputTargetCode) {
        String baseCurrencyCode = CurrencyFormatter.getValidCode(inputBaseCode);
        String targetCurrencyCode = CurrencyFormatter.getValidCode(inputTargetCode);
        return new CurrencyPair(baseCurrencyCode,targetCurrencyCode);
    }
}
