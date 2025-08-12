package Formatter;

import dto.ConversionRequestDto;
import dto.CurrencyPair;
import dto.ExchangeRateRequestDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidParameterException;

import static Formatter.CurrencyFormatter.getValidCode;
import static exception.ErrorMessages.ParameterError.*;

public final class ExchangeRateFormatter {

    private static int CODE_LENGTH = 3;

    public static final BigDecimal MIN_RATE = new BigDecimal("0.000001");
    public static final BigDecimal MAX_RATE = new BigDecimal("1000000");
    public static final int SCALE_RATE = 6;

    public static final BigDecimal MIN_AMOUNT = new BigDecimal("0.01");
    public static final BigDecimal MAX_AMOUNT = new BigDecimal("9999999");

    public static ExchangeRateRequestDto getValidExchangeRate(String inputCodes, BigDecimal inputRate) {
        CurrencyPair codePair = parseValidCodePair(inputCodes);
        return getValidExchangeRate(codePair.getBaseCurrency(), codePair.getTargetCurrency(), inputRate);
    }

    public static ExchangeRateRequestDto getValidExchangeRate(String inputBaseCode,
                                                              String inputTargetCode, BigDecimal inputRate) {
        String baseCurrencyCode = getValidCode(inputBaseCode);
        String targetCurrencyCode = getValidCode(inputTargetCode);

        validateCurrenciesAreDifferent(baseCurrencyCode, targetCurrencyCode);

        BigDecimal rate = getValidRoundedRate(inputRate);
        return new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rate);
    }

    public static ConversionRequestDto getValidConversionRequest(ConversionRequestDto conversionRequestDto) {
        String baseCurrencyCode = getValidCode(conversionRequestDto.getBaseCurrencyCode());
        String targetCurrencyCode = getValidCode(conversionRequestDto.getTargetCurrencyCode());
        validateCurrenciesAreDifferent(baseCurrencyCode, targetCurrencyCode);

        BigDecimal rate = getValidAmount(conversionRequestDto.getBaseAmount());
        return new ConversionRequestDto(baseCurrencyCode, targetCurrencyCode, rate);
    }


    public static CurrencyPair parseValidCodePair(String inputCodes) {
        String cleanedInputCodes = inputCodes.trim();
        validateCurrencyPairLength(cleanedInputCodes);
        String baseCurrencyCode = cleanedInputCodes.substring(0,CODE_LENGTH);
        String targetCurrencyCode = cleanedInputCodes.substring(CODE_LENGTH,CODE_LENGTH*2);
        return getValidCurrencyPair(baseCurrencyCode,targetCurrencyCode);
    }

    public static CurrencyPair getValidCurrencyPair(String inputBaseCode, String inputTargetCode) {
        String baseCurrencyCode = getValidCode(inputBaseCode);
        String targetCurrencyCode = getValidCode(inputTargetCode);
        return new CurrencyPair(baseCurrencyCode,targetCurrencyCode);
    }

    private static void validateCurrencyPairLength(String cleanedCodes) {
        final int codePairLength = CODE_LENGTH * 2;
        if (cleanedCodes.length() != codePairLength) {
            throw new InvalidParameterException(CURRENCY_CODE_INVALID);
        }
    }

    private static void validateCurrenciesAreDifferent(String baseCurrencyCode, String targetCurrencyCode) {
        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            throw new InvalidParameterException(CURRENCIES_CODES_EQUAL);
        }
    }

    private static BigDecimal getValidRoundedRate(BigDecimal exchangeRate) {
        return checkNumberInRange(exchangeRate.setScale(SCALE_RATE, RoundingMode.HALF_EVEN), MIN_RATE, MAX_RATE);
    }

    private static BigDecimal getValidAmount(BigDecimal amountToExchange) {
        return checkNumberInRange(amountToExchange, MIN_AMOUNT, MAX_AMOUNT);
    }

    private static BigDecimal checkNumberInRange(BigDecimal decimal, BigDecimal min, BigDecimal max) {
        if (decimal.compareTo(min) < 0 || decimal.compareTo(max) > 0) {
            throw new InvalidParameterException(NUMBER_NOT_IN_RANGE_TEMPLATE.formatted(min, max));
        }
        return decimal;
    }


    private ExchangeRateFormatter() {}
}
