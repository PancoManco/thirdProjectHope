package mapper;

import model.Currency;
import model.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class DBObjectMapper {
    private DBObjectMapper() {
    }
    public static Currency mapToCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("fullname"),
                resultSet.getString("sign"));
    }

    public static ExchangeRate mapToExchangeRate(ResultSet rs) throws SQLException {
        Currency baseCurrency = new Currency(
                rs.getInt("basecurrencyid"),
                rs.getString("base_currency_code"),
                rs.getString("base_fullname"),
                rs.getString("base_sign")
        );
        Currency targetCurrency = new Currency(
                rs.getInt("targetcurrencyid"),
                rs.getString("target_currency_code"),
                rs.getString("target_fullname"),
                rs.getString("target_sign")
        );

        return new ExchangeRate(
                rs.getInt("id"),
                baseCurrency,
                targetCurrency,
                rs.getBigDecimal("rate")
        );
    }
}
