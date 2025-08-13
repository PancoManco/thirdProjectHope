package service;

import Formatter.ExchangeRateFormatter;
import dto.ConversionRequestDto;
import dto.ConversionResponseDto;
import dto.ExchangeRateDto;
import exception.EntityNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static exception.ErrorMessages.ExchangeRatesError.CURRENCY_NOT_FOUND;


public class ConversionService {

    private static final int AMOUNT_SCALE = 2;
    private static final String DEFAULT_CURRENCY_CODE = "USD";
    private static final int SCALE_RATE =6;
    ExchangeRateService exchangeRateService = new ExchangeRateService();

    public ConversionResponseDto convert(ConversionRequestDto conversionRequestDto) {
        ConversionRequestDto request = ExchangeRateFormatter.buildValidatedConversionRequestDto(conversionRequestDto);
        //    CurrencyPair codePair = ExchangeRateFormatter.validateCurrencyPair(conversionRequestDto.getBaseCurrencyCode(),conversionRequestDto.getTargetCurrencyCode());
        String baseCurrencyCode = request.getBaseCurrencyCode();
        String targetCurrencyCode = request.getTargetCurrencyCode();
        BigDecimal amount = request.getBaseAmount();

        return exchangeRateService.findExchangeRate(baseCurrencyCode,targetCurrencyCode)
                .map(directExchangeRate -> createDirectConversion(directExchangeRate,amount))
                .orElseGet(()-> exchangeRateService
                        .findExchangeRate(baseCurrencyCode,targetCurrencyCode)
                        .map(reversedExchangeRate -> createReversedConversion(reversedExchangeRate,amount))
                        .orElseGet(()->findCrossRateConversion(baseCurrencyCode,targetCurrencyCode,amount)));

    }

    private ConversionResponseDto findCrossRateConversion(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        ExchangeRateDto defaultBaseExchangeRate = exchangeRateService
                .findExchangeRate(DEFAULT_CURRENCY_CODE, baseCurrencyCode)
                .orElseThrow(() -> new EntityNotFoundException(CURRENCY_NOT_FOUND));

        ExchangeRateDto defaultTargetExchangeRate = exchangeRateService
                .findExchangeRate(DEFAULT_CURRENCY_CODE, targetCurrencyCode)
                .orElseThrow(() -> new EntityNotFoundException(CURRENCY_NOT_FOUND));

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
