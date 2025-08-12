package service;

import dao.CurrencyDao;
import dao.ExchangeRateDao;
import dto.CurrencyPair;
import dto.ExchangeRateDto;
import dto.ExchangeRateRequestDto;
import exception.EntityNotFoundException;
import mapper.ExchangeRateMapper;
import model.Currency;
import model.ExchangeRate;
import Formatter.ExchangeRateFormatter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Formatter.ExchangeRateFormatter.buildValidatedExchangeRateDto;
import static Formatter.ExchangeRateFormatter.buildValidatedExchangeRateDtoFromString;
import static exception.ErrorMessages.CURRENCY_NOT_FOUND_MESSAGE_TEMPLATE;
import static exception.ErrorMessages.ParameterError.ExchangeRatesError.EXCHANGE_RATE_NOT_FOUND_TEMPLATE;

public class ExchangeRateService {

    private final ExchangeRateDao dao = ExchangeRateDao.getInstance();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private final ExchangeRateMapper mapper = ExchangeRateMapper.INSTANCE;

    public List<ExchangeRateDto> getAllExchangeRates() {
        return dao.getAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public ExchangeRateDto findExchangeRateByCode(String inputCodes) {
        CurrencyPair codePair = ExchangeRateFormatter.parseCurrencyPair(inputCodes);
        return dao.getByPair(codePair.getBaseCurrency(), codePair.getTargetCurrency())
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCHANGE_RATE_NOT_FOUND_TEMPLATE.formatted(codePair.getBaseCurrency(), codePair.getTargetCurrency()))));
    }

    public ExchangeRateDto createExchangeRate(ExchangeRateRequestDto requestDto) {

        ExchangeRateRequestDto request = buildValidatedExchangeRateDto(requestDto.getBaseCurrencyCode(), requestDto.getTargetCurrencyCode(), requestDto.getRate());
        Currency baseCurrency = currencyDao.findByCode(request.getBaseCurrencyCode())
                .orElseThrow(() -> new EntityNotFoundException(CURRENCY_NOT_FOUND_MESSAGE_TEMPLATE + request.getBaseCurrencyCode()));
        Currency targetCurrency = currencyDao.findByCode(request.getTargetCurrencyCode())
                .orElseThrow(() -> new EntityNotFoundException(CURRENCY_NOT_FOUND_MESSAGE_TEMPLATE + request.getTargetCurrencyCode()));
        ExchangeRate savedRate = dao.save(
                baseCurrency,
                targetCurrency,
                requestDto.getRate()).get();
        return mapper.toDto(savedRate);
    }

    public ExchangeRateDto updateExchangeRate(String inputCodes, BigDecimal newRate) {
        ExchangeRateRequestDto request = buildValidatedExchangeRateDtoFromString(inputCodes, newRate);
      //  CurrencyPair codePair = ExchangeRateFormatter.parseCurrencyPair(inputCodes);
        ExchangeRate exchangeRate = dao.update(request.getBaseCurrencyCode(), request.getTargetCurrencyCode(), newRate);
        return mapper.toDto(exchangeRate);
    }

    public Optional<ExchangeRateDto> findExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        Optional<ExchangeRate> optExchangeRate = dao.getByPair(baseCurrencyCode, targetCurrencyCode);
        return optExchangeRate.map(ExchangeRateMapper.INSTANCE::toDto);
    }

}
