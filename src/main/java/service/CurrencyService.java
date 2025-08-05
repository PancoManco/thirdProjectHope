package service;

import dao.CurrencyDao;
import dto.CurrencyDto;
import exception.NotFoundException;
import mapper.CurrencyMapper;
import model.Currency;
import validation.CurrencyFormatter;

import java.util.List;
import java.util.stream.Collectors;

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
                dao.findByCode(code).orElseThrow(() -> new NotFoundException("Валюта с кодом '" + code + "' не найдена"))
        );
    }

    public CurrencyDto save(CurrencyDto currencyDto) {
        CurrencyDto validDto = CurrencyFormatter.getValidCurrencyDTO(currencyDto);
        Currency currencyToSave = mapper.toEntity(validDto);
      Currency savedCurrency = dao.save(currencyToSave).get();
        return mapper.toDto(savedCurrency);
    }
}
