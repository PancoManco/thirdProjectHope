import dao.CurrencyDao;
import dao.ExchangeRateDao;
import dto.CurrencyDto;
import mapper.CurrencyMapper;
import model.Currency;
import model.ExchangeRate;
import validation.CurrencyFormatter;

import java.math.BigDecimal;

public class Runner {

    public static void main(String[] args) {

        ExchangeRateDao dao = ExchangeRateDao.getInstance();
        BigDecimal big = new BigDecimal("1.23456");
        dao.update("USD","2",big);

     //   System.out.println(CurrencyFormatter.getValidCurrencyDTO(dto));

//        System.out.println();
//            System.out.println(exchangeratedao.getByPair("RUB","USD"));
//        System.out.println();
//        System.out.println(exchangeratedao.getAll());
    }
}
