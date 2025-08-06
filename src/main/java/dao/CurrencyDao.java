package dao;

import connection.ConnectionPool;
import exception.AlreadyExistsException;
import exception.DBException;
import mapper.DataMapper;
import model.Currency;
import utils.ConnectionManager;
import utils.UniqueConstantValidator;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static exception.ErrorMessages.CurrencyError.*;
import static exception.ErrorMessages.ParameterError.ERROR_DUPLICATE_VALUES;

public class CurrencyDao {
    private final static CurrencyDao INSTANCE = new CurrencyDao();

    private final static String SAVE_SQL = """
            insert into currencydatabase.currencies(code,fullname,sign) values(?,?,?)
            """;

    private final static String GET_ALL_SQL = """
            select * from currencydatabase.currencies
            """;

    private final static String GET_BY_CODE_SQL = GET_ALL_SQL + """
            where code = ?
            """;

    private CurrencyDao() {
    }

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }

    public Optional<Currency> save(Currency currency) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getName());
            statement.setString(3, currency.getSign());
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                currency.setId(keys.getInt("id"));
            }
            return Optional.of(currency);
        } catch (SQLException e) {
            if (UniqueConstantValidator.isUniqueConstant(e)) {
                throw new AlreadyExistsException(ERROR_DUPLICATE_VALUES.formatted(currency.getCode()), e);
            }
            throw new DBException(ERROR_SAVING_CURRENCY_TEMPLATE.formatted(currency.getCode(),currency.getName(),e.getMessage()));
        }
    }

    public List<Currency> findAll() {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(GET_ALL_SQL)) {
            List<Currency> currencies = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                currencies.add(
                        DataMapper.buildCurrency(result));

            }
            return currencies;
        } catch (SQLException e) {
            throw new DBException(ERROR_GETTING_CURRENCIES_LIST_TEMPLATE.formatted(GET_ALL_SQL,e.getMessage()));
        }

    }

    public Optional<Currency> findByCode(String code) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(GET_BY_CODE_SQL)
        ) {
            statement.setString(1, code);
            var result = statement.executeQuery();
            Currency currency = null;
            if (result.next()) {
                currency = DataMapper.buildCurrency(result);
            }
            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new DBException(ERROR_FINDING_CURRENCY_BY_CODE_TEMPLATE.formatted(code,e.getMessage()));
        }
    }
}
