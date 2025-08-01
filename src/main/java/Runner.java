import dao.CurrencyDao;
import mapper.CurrencyMapper;
import model.Currency;
import org.mapstruct.factory.Mappers;

public class Runner {

    public static void main(String[] args) {

        var currencyDao = CurrencyDao.getInstance();
       Currency currency = new Currency(3,"SHI","SHITCOINT","%");
//     //   currencyDao.create(currency);

System.out.println(currencyDao.findAll());
       CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);
            INSTANCE.toDto(currency);
//        var exchangeratedao = ExchangeRateDao.getInstance();
//        System.out.println();
//            System.out.println(exchangeratedao.getByPair("RUB","USD"));
//        System.out.println();
//        System.out.println(exchangeratedao.getAll());
    }
}
