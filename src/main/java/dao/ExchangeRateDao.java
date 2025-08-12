package dao;

import connection.ConnectionPool;
import exception.DuplicateEntryException;
import exception.DBException;
import exception.NotFoundException;
import mapper.DBObjectMapper;
import model.Currency;
import model.ExchangeRate;
import utils.UniqueConstantValidator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static exception.ErrorMessages.ParameterError.ExchangeRatesError.*;

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

    private final static String GET_BY_PAIR_SQL = GET_ALL_SQL + """
                where bc.code = ? and tc.code = ?
            """;

public Optional<ExchangeRate> save(Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
    try (var connection = ConnectionPool.getConnection();
         var statement = connection.prepareStatement(SAVE_EXCHANGE_RATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
       // validateCurrenciesExistence(baseCurrency, targetCurrency);
        statement.setInt(1, baseCurrency.getId());
        statement.setInt(2, targetCurrency.getId());
        statement.setBigDecimal(3, rate);
        statement.executeUpdate();
        var keys = statement.getGeneratedKeys();
        if (keys.next()) {
            return Optional.ofNullable(new ExchangeRate(keys.getInt("id"), baseCurrency, targetCurrency, rate));
        }
        return Optional.empty();
    } catch (SQLException e) {
        if (UniqueConstantValidator.isUniqueConstant(e)) {
            throw new DuplicateEntryException(ERROR_UNIQUE_CONSTRAINT_VIOLATION_EXRATE_TEMPLATE.formatted(baseCurrency.getName() + targetCurrency.getName()), e);
        }
        throw new DBException(FAILED_TO_CREATE_EXCHANGE_RATE);
    }
}


    public List<ExchangeRate> getAll() {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(GET_ALL_SQL)
        ) {
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                exchangeRates.add(DBObjectMapper.mapToExchangeRate(result));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new DBException(FAILED_TO_RETRIEVE_EXCHANGE_RATES);
        }
    }

    public ExchangeRate update(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(UPDATE_EXCHANGE_RATE_SQL)) {
            Optional<ExchangeRate> optionalRate = getByPair(baseCurrencyCode, targetCurrencyCode);
            if (optionalRate.isEmpty()) {   // todo why ????
                throw new NotFoundException("Курс обмена не найден для пары: " + baseCurrencyCode + " → " + targetCurrencyCode);
            }
            ExchangeRate exchangerate = optionalRate.get();
            statement.setBigDecimal(1, rate);
            statement.setInt(2, exchangerate.getId());
            statement.executeUpdate();
            exchangerate.setRate(rate);
            return exchangerate;
        } catch (SQLException e) {
            throw new DBException(FAILED_TO_UPDATE_EXCHANGE_RATE);
        }
    }

    public Optional<ExchangeRate> getByPair(String baseCurrencyCode, String targetCurrencyCode) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(GET_BY_PAIR_SQL)
        ) {
            statement.setString(1, baseCurrencyCode);
            statement.setString(2, targetCurrencyCode);
            var result = statement.executeQuery();
            ExchangeRate exchangeRate = null;
            if (result.next()) {
                exchangeRate = DBObjectMapper.mapToExchangeRate(result);
            }
            return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new DBException(FAILED_TO_RETRIEVE_PAIR_BY_CODE);
        }
    }

    private ExchangeRateDao() {
    }
    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }
}
