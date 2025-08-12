package service;

import dto.ConversionRequestDto;
import dto.ConversionResponseDto;
import dto.CurrencyPair;
import dto.ExchangeRateDto;
import exception.EntityNotFoundException;
import Formatter.ExchangeRateFormatter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static exception.ErrorMessages.ParameterError.ExchangeRatesError.UNABLE_TO_CONVERT;

public class ConversionService {
    private static final int AMOUNT_SCALE = 2;
    private static final String DEFAULT_CURRENCY_CODE = "USD";
    private static final int SCALE_RATE =6;
    ExchangeRateService exchangeRateService = new ExchangeRateService();

    public ConversionResponseDto convert(ConversionRequestDto conversionRequestDto) {
        CurrencyPair codePair = ExchangeRateFormatter.getValidCurrencyPair(conversionRequestDto.getBaseCurrencyCode(),conversionRequestDto.getTargetCurrencyCode());
        String baseCurrencyCode = codePair.getBaseCurrency();
        String targetCurrencyCode = codePair.getTargetCurrency();
        BigDecimal amount = conversionRequestDto.getBaseAmount();

        return exchangeRateService.fetchExchangeRate(baseCurrencyCode,targetCurrencyCode)
                .map(directExchangeRate -> createDirectConversion(directExchangeRate,amount))
                .orElseGet(()-> exchangeRateService
                        .fetchExchangeRate(baseCurrencyCode,targetCurrencyCode)
                        .map(reversedExchangeRate -> createReversedConversion(reversedExchangeRate,amount))
                        .orElseGet(()->findCrossRateConversion(baseCurrencyCode,targetCurrencyCode,amount)));

    }

    private ConversionResponseDto findCrossRateConversion(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        ExchangeRateDto defaultBaseExchangeRate = exchangeRateService
                .fetchExchangeRate(DEFAULT_CURRENCY_CODE, baseCurrencyCode)
                .orElseThrow(() -> new EntityNotFoundException(UNABLE_TO_CONVERT));

        ExchangeRateDto defaultTargetExchangeRate = exchangeRateService
                .fetchExchangeRate(DEFAULT_CURRENCY_CODE, targetCurrencyCode)
                .orElseThrow(() -> new EntityNotFoundException(UNABLE_TO_CONVERT));

        return createCrossRateConversion(defaultBaseExchangeRate, defaultTargetExchangeRate, amount);
    }

    private ConversionResponseDto createCrossRateConversion(ExchangeRateDto defaultBaseExchangeRate, ExchangeRateDto defaultTargetExchangeRate, BigDecimal amount) {
        BigDecimal crossRate = defaultTargetExchangeRate.getRate().divide(defaultBaseExchangeRate.getRate(),
                SCALE_RATE, RoundingMode.HALF_EVEN);

        BigDecimal convertedAmount = getConvertedAmount(amount, crossRate);

        return new ConversionResponseDto(
                defaultBaseExchangeRate.getTargetCurrency(),
                defaultTargetExchangeRate.getTargetCurrency(),
                crossRate.stripTrailingZeros(), amount, convertedAmount);
    }

    private ConversionResponseDto createReversedConversion(ExchangeRateDto exchangeRateDto, BigDecimal amount) {
        BigDecimal reversedRate = BigDecimal.ONE.divide(exchangeRateDto.getRate(), SCALE_RATE, RoundingMode.HALF_EVEN);
        BigDecimal convertedAmount = getConvertedAmount(amount, reversedRate);
        return new ConversionResponseDto(
                exchangeRateDto.getTargetCurrency(),
                exchangeRateDto.getBaseCurrency(),
                reversedRate.stripTrailingZeros(), amount, convertedAmount);
    }

    private ConversionResponseDto createDirectConversion(ExchangeRateDto exchangeRateDto, BigDecimal amount) {
        BigDecimal rate = exchangeRateDto.getRate();
        BigDecimal convertedAmount = getConvertedAmount(amount, rate);
        return new ConversionResponseDto(
                exchangeRateDto.getBaseCurrency(),
                exchangeRateDto.getTargetCurrency(),
                exchangeRateDto.getRate().stripTrailingZeros(), amount, convertedAmount);
    }

    private BigDecimal getConvertedAmount(BigDecimal amount, BigDecimal rate) {
        return rate.multiply(amount)
                .setScale(AMOUNT_SCALE, RoundingMode.HALF_EVEN);
    }
}
