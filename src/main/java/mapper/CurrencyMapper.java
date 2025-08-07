package mapper;

import dto.CurrencyDto;
import dto.CurrencyDtoRequest;
import model.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    CurrencyDto toDto(Currency currency);

    Currency toEntity(CurrencyDto dto);
    Currency toEntityFromRequest(CurrencyDtoRequest dtoRequest);
}