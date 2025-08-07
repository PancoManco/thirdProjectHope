package service;

import dao.CurrencyDao;
import dto.CurrencyDto;
import dto.CurrencyDtoRequest;
import exception.NotFoundException;
import mapper.CurrencyMapper;
import model.Currency;
import utils.RequestParameterUtil;
import validation.CurrencyFormatter;

import java.util.List;
import java.util.stream.Collectors;

import static exception.ErrorMessages.CURRENCY_NOT_FOUND_MESSAGE_TEMPLATE;

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
                dao.findByCode(code).orElseThrow(() -> new NotFoundException(CURRENCY_NOT_FOUND_MESSAGE_TEMPLATE.formatted(code)))
        );
    }

//    public CurrencyDto save(CurrencyDtoRequest currencyDto) {
//        CurrencyDto validDto = CurrencyFormatter.getValidCurrencyDTO(currencyDto);
//        Currency currencyToSave = mapper.toEntity(validDto);
//        Currency savedCurrency = dao.save(currencyToSave).get();
//        return mapper.toDto(savedCurrency);
//    }
public CurrencyDto save(CurrencyDtoRequest currencyDto) {
    CurrencyFormatter.validateCurrencyDto(currencyDto); // просто проверка
    Currency currencyToSave = mapper.toEntityFromRequest(currencyDto); // mapper берет request и строит entity
    Currency savedCurrency = dao.save(currencyToSave).get();
    return mapper.toDto(savedCurrency);
}

}
