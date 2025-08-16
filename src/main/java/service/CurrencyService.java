package service;

import Validation.CurrencyValidator;
import dao.CurrencyDao;
import dto.CurrencyDto;
import dto.CurrencyDtoRequest;
import exception.EntityNotFoundException;
import mapper.CurrencyMapper;
import model.Currency;

import java.util.List;
import java.util.stream.Collectors;

import static exception.ErrorMessages.CurrencyError.CURRENCY_NOT_FOUND_MESSAGE_TEMPLATE;

public class CurrencyService {

    private final CurrencyDao dao = CurrencyDao.getInstance();
    private final CurrencyMapper mapper = CurrencyMapper.INSTANCE;

    public List<CurrencyDto> getAllCurrencies() {
        return dao.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CurrencyDto getCurrencyByCode(String code) {
        return mapper.toDto(
                dao.findByCode(code).orElseThrow(() -> new EntityNotFoundException(CURRENCY_NOT_FOUND_MESSAGE_TEMPLATE.formatted(code)))
        );
    }

    public CurrencyDto save(CurrencyDtoRequest currencyDto) {
        CurrencyDtoRequest validRequestDto = CurrencyValidator.getValidCurrencyDto(currencyDto);
        Currency currencyToSave = mapper.toEntityFromRequest(validRequestDto);
        Currency savedCurrency = dao.save(currencyToSave).get();
        return mapper.toDto(savedCurrency);
    }
}
