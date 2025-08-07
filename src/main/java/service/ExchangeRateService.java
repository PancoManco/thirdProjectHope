package service;

import dao.CurrencyDao;
import dao.ExchangeRateDao;
import dto.CurrencyDto;
import dto.ExchangeRateDto;
import dto.ExchangeRateRequestDto;
import exception.NotFoundException;
import mapper.ExchangeRateMapper;
import model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeRateService {

    private final ExchangeRateDao dao = ExchangeRateDao.getInstance();
    private final ExchangeRateMapper mapper = ExchangeRateMapper.INSTANCE;

    public List<ExchangeRateDto> getAllExchangeRates() {
        return dao.getAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public ExchangeRateDto getByCurrencyPair(String baseCode, String targetCode) {
        return dao.getByPair(baseCode, targetCode)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Курс обмена для пары %s -> %s не найден", baseCode, targetCode)));

    }
    /*
    public ExchangeRateDto createExchangeRate(ExchangeRateDto dto) {
        ExchangeRate exchangeRateToSave = mapper.toEntity(dto);
        // Важно: Currency у ExchangeRate должен иметь id, иначе сохранение не пройдет!
        ExchangeRate savedRate = dao.save(
                exchangeRateToSave.getBaseCurrency().getCode(),
                exchangeRateToSave.getTargetCurrency().getCode(),
                exchangeRateToSave.getRate()
        ).orElseThrow(() -> new IllegalStateException("Не удалось сохранить курс обмена"));
        return mapper.toDto(savedRate);
    } */

    public ExchangeRateDto createExchangeRate(ExchangeRateRequestDto requestDto) {
         ExchangeRate exchangeRateToSave = mapper.toEntity(requestDto);
        ExchangeRate savedRate = dao.save(
                requestDto.getBaseCurrencyCode(),
                requestDto.getTargetCurrencyCode(),
                requestDto.getRate()
        ).orElseThrow(() -> new IllegalStateException("Не удалось сохранить курс обмена"));
        return mapper.toDto(savedRate);

        //CurrencyDto
    }

    public boolean updateExchangeRate(String baseCode, String targetCode, BigDecimal newRate) {
        return dao.update(baseCode, targetCode, newRate);
    }
}
