package mapper;

import dto.ExchangeRateDto;
import dto.ExchangeRateRequestDto;
import model.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CurrencyMapper.class)
public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);
    ExchangeRateDto toDto(ExchangeRate exchangeRate);
    ExchangeRate toEntity(ExchangeRateDto dto);
    ExchangeRate toEntity(ExchangeRateRequestDto requestDto);
    ExchangeRate toEntityFromRequest(ExchangeRateRequestDto dtoRequest);
}