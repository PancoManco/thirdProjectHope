package dao;

import connection.ConnectionPool;
import exception.AlreadyExistsException;
import exception.DBException;
import mapper.BuilderObj;
import model.Currency;
import model.ExchangeRate;
import utils.UniqueConstantValidator;
import validation.CurrencyFormatter;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static exception.ErrorMessages.ParameterError.ERROR_DUPLICATE_VALUES;
import static exception.ErrorMessages.ParameterError.ExchangeRatesError.ERROR_DUPLICATE_EXCHANGE_RATE_VALUES;
import static validation.CurrencyFormatter.validateCurrenciesExistence;

public class ExchangeRateDao {

    private final static ExchangeRateDao INSTANCE = new ExchangeRateDao();
    private final static CurrencyDao currencyDao = CurrencyDao.getInstance();

    private final static String SAVE_EXCHANGE_RATE_SQL = """
            Insert into currencydatabase.exchangerates(basecurrencyid, targetcurrencyid, rate)  
            values(?,?,?)
            """;

    private final static String GET_ALL_SQL = """
                select e.id, e.basecurrencyid, e.targetcurrencyid, e.rate,
                       bc.code as base_currency_code, bc.fullname as base_fullname, bc.sign as base_sign,
                       tc.code as target_currency_code, tc.fullname as target_fullname, tc.sign as target_sign
                from currencydatabase.exchangerates e
                         left join currencydatabase.currencies bc on e.basecurrencyid = bc.id
                         left join currencydatabase.currencies tc on e.targetcurrencyid = tc.id
            """;

    private final static String UPDATE_EXCHANGE_RATE_SQL = """
            UPDATE currencydatabase.exchangerates
            SET rate = ?
            WHERE id = ?
            """;

    private final static String GET_BY_PAIR_SQL =GET_ALL_SQL + """
    where bc.code = ? and tc.code = ?
""";

    public Optional<ExchangeRate> save(String baseCurrency, String targetCurrency, BigDecimal rate) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(SAVE_EXCHANGE_RATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            validateCurrenciesExistence(baseCurrency, targetCurrency);
            Currency base = currencyDao.findByCode(baseCurrency).get();
            Currency target = currencyDao.findByCode(targetCurrency).get();
            statement.setInt(1, base.getId());
            statement.setInt(2, target.getId());
            statement.setBigDecimal(3, rate);
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
           if (keys.next()) {
               return  Optional.ofNullable(new ExchangeRate(keys.getInt("id"),base,target,rate));
            }
            return Optional.empty();
        } catch (SQLException e) {
            if (UniqueConstantValidator.isUniqueConstant(e)) {
                throw new AlreadyExistsException(ERROR_DUPLICATE_EXCHANGE_RATE_VALUES.formatted(baseCurrency+targetCurrency), e);
            }
            throw new DBException("Ошибка при создании курса обмена валют. Проблемы с доступом к БД! ");
        }
    }

    public List<ExchangeRate> getAll() {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(GET_ALL_SQL)
        ) {
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                exchangeRates.add(BuilderObj.buildExchangeRate(result));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new DBException("Ошибка при получение списка курса обмена валют.Проблемы с доступом к БД!");
        }

    }


    public ExchangeRate update(String basecurrencycode, String targetcurrencycode, BigDecimal rate) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(UPDATE_EXCHANGE_RATE_SQL)) {

            Optional<ExchangeRate> optionalRate = getByPair(basecurrencycode, targetcurrencycode);
            if (optionalRate.isEmpty()) {
                throw new DBException("Курс обмена не найден для пары: " + basecurrencycode + " → " + targetcurrencycode);
            }
            ExchangeRate exchangerate = optionalRate.get();
            statement.setBigDecimal(1, rate);
            statement.setInt(2, exchangerate.getId());
            statement.executeUpdate();
            exchangerate.setRate(rate);
            return exchangerate;
        } catch (SQLException e) {
            throw new DBException("Ошибка при обновление курса обмена валют.Проблемы с доступом к БД!");
        }
    }

    public Optional<ExchangeRate> getByPair(String basecurrencycode, String targetcurrencycode) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(GET_BY_PAIR_SQL)
        ) {
            statement.setString(1, basecurrencycode);
            statement.setString(2, targetcurrencycode);
            var result = statement.executeQuery();
            ExchangeRate exchangeRate = null;
            if (result.next()) {
                exchangeRate = BuilderObj.buildExchangeRate(result);
            }
            return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new DBException("Ошибка при получение пары обмена курса валют. Проблемы с доступом к БД!");
        }
    }

    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }
    private ExchangeRateDao() {
    }
}
