import dao.CurrencyDao;
import dto.CurrencyDto;
import mapper.CurrencyMapper;
import model.Currency;
import validation.CurrencyFormatter;

public class Runner {

    public static void main(String[] args) {

        CurrencyDao dao = CurrencyDao.getInstance();
        Currency currency = new Currency(200, "YYY", "YFYFUDU", "%");

        CurrencyDto dto = CurrencyMapper.INSTANCE.toDto(currency);
        System.out.println(CurrencyMapper.INSTANCE.toDto(currency));
        System.out.println(
                dto.getCode()
        );
        System.out.println(dto.getName());
        System.out.println("Code");


        System.out.println(CurrencyFormatter.getValidCode(dto.getCode()));


        System.out.println(CurrencyFormatter.getValidCurrencyDTO(dto));

//        System.out.println();
//            System.out.println(exchangeratedao.getByPair("RUB","USD"));
//        System.out.println();
//        System.out.println(exchangeratedao.getAll());
    }
}
