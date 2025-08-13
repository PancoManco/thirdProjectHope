package Validation;

import dto.ConversionRequestDto;
import dto.CurrencyPair;
import dto.ExchangeRateRequestDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidParameterException;

import static Validation.CurrencyValidator.getValidCode;
import static exception.ErrorMessages.ParameterError.*;

public final class ExchangeRateValidator {

    private static final int CURRENCY_CODE_LENGTH = 3;
    private static final int CURRENCY_PAIR_LENGTH = 6;

    public static final BigDecimal MIN_RATE = new BigDecimal("0.000001");
    public static final BigDecimal MAX_RATE = new BigDecimal("1000000");
    public static final int SCALE_RATE = 6;

    public static final BigDecimal MIN_AMOUNT = new BigDecimal("0.01");
    public static final BigDecimal MAX_AMOUNT = new BigDecimal("9999999");

    public static ConversionRequestDto buildValidatedConversionRequestDto(ConversionRequestDto dto) {
        CurrencyPair pair = validateCurrencyPair(dto.getBaseCurrencyCode(), dto.getTargetCurrencyCode());
        BigDecimal amount = getValidAmount(dto.getBaseAmount());
        //  validateCurrenciesAreDifferent(pair.getBaseCurrency(), pair.getTargetCurrency());
        return new ConversionRequestDto(pair.getBaseCurrency(), pair.getTargetCurrency(), amount);
    }

    public static ExchangeRateRequestDto buildValidatedExchangeRateDto(String inputBaseCode,
                                                              String inputTargetCode, BigDecimal inputRate) {
        String baseCurrencyCode = getValidCode(inputBaseCode);
        String targetCurrencyCode = getValidCode(inputTargetCode);

        validateCurrenciesAreDifferent(baseCurrencyCode, targetCurrencyCode);

        BigDecimal rate = getValidRoundedRate(inputRate);
        return new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rate);
    }


    public static ExchangeRateRequestDto buildValidatedExchangeRateDtoFromString(String inputCodes, BigDecimal inputRate) {
        CurrencyPair pair = parseCurrencyPair(inputCodes);
        return new ExchangeRateRequestDto(
                pair.getBaseCurrency(),
                pair.getTargetCurrency(),
                getValidRoundedRate(inputRate)
        );
    }

    public static CurrencyPair parseCurrencyPair(String input) {
        String cleanedInputCodes = input.trim();
        if (cleanedInputCodes.length() != CURRENCY_PAIR_LENGTH) {
            throw new InvalidParameterException(CURRENCY_CODE_INVALID);
        }
        return validateCurrencyPair(
                cleanedInputCodes.substring(0, CURRENCY_CODE_LENGTH),
                cleanedInputCodes.substring(CURRENCY_CODE_LENGTH)
        );
    }

    public static CurrencyPair validateCurrencyPair(String inputBaseCode, String inputTargetCode) {
        String baseCurrencyCode = getValidCode(inputBaseCode);
        String targetCurrencyCode = getValidCode(inputTargetCode);
        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            throw new InvalidParameterException(CURRENCIES_CODES_EQUAL);
        }
        return new CurrencyPair(baseCurrencyCode,targetCurrencyCode);
    }

    private static void validateCurrenciesAreDifferent(String baseCurrencyCode, String targetCurrencyCode) {
        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            throw new InvalidParameterException(CURRENCIES_CODES_EQUAL);
        }
    }

    private static BigDecimal getValidRoundedRate(BigDecimal exchangeRate) {
        BigDecimal scaled = exchangeRate.setScale(SCALE_RATE, RoundingMode.HALF_EVEN);
        return checkNumberInRange(scaled, MIN_RATE, MAX_RATE);
    }

    private static BigDecimal getValidAmount(BigDecimal amountToExchange) {
        return checkNumberInRange(amountToExchange, MIN_AMOUNT, MAX_AMOUNT);
    }

    private static BigDecimal checkNumberInRange(BigDecimal value, BigDecimal min, BigDecimal max) {
        if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
            throw new InvalidParameterException(NUMBER_NOT_IN_RANGE_TEMPLATE.formatted(min, max));
        }
        return value;
    }

    private ExchangeRateValidator() {}
}
