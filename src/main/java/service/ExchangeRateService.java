package service;

import dao.CurrencyDao;
import dao.ExchangeRateDao;
import dto.CurrencyPair;
import dto.ExchangeRateDto;
import dto.ExchangeRateRequestDto;
import exception.NotFoundException;
import mapper.ExchangeRateMapper;
import model.ExchangeRate;
import validation.ExchangeRateFormatter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExchangeRateService {

    private final ExchangeRateDao dao = ExchangeRateDao.getInstance();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private final ExchangeRateMapper mapper = ExchangeRateMapper.INSTANCE;

    public List<ExchangeRateDto> getAllExchangeRates() {
        return dao.getAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public ExchangeRateDto fingExchangeRateByCode(String inputCodes) {
        CurrencyPair codePair = ExchangeRateFormatter.parseValidCodePair(inputCodes);
        return dao.getByPair(codePair.getBaseCurrency(), codePair.getTargetCurrency())
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException(String.format("Обменный курс для пары %s%s не найден".formatted( codePair.getBaseCurrency(), codePair.getTargetCurrency()))));
    }

    public ExchangeRateDto createExchangeRate(ExchangeRateRequestDto requestDto) {
        if (requestDto.getBaseCurrencyCode().equalsIgnoreCase(requestDto.getTargetCurrencyCode())) {
            throw new IllegalArgumentException("Нельзя создавать валютную пару из одной и той же валюты");
        }
        ExchangeRate savedRate = dao.save(
                requestDto.getBaseCurrencyCode(),
                requestDto.getTargetCurrencyCode(),
                requestDto.getRate()
        ).orElseThrow(() -> new  IllegalStateException("Не удалось сохранить курс обмена"));
        return mapper.toDto(savedRate);
    }

    public ExchangeRateDto updateExchangeRate(String inputCodes, BigDecimal newRate) {
        CurrencyPair codePair = ExchangeRateFormatter.parseValidCodePair(inputCodes);
        ExchangeRate exchangeRate = dao.update(codePair.getBaseCurrency(), codePair.getTargetCurrency(), newRate);
        return mapper.toDto(exchangeRate);

    }


}
