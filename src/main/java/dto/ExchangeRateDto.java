package dto;

import java.math.BigDecimal;


public class ExchangeRateDto {

    CurrencyDto baseCurrency;
    CurrencyDto targetCurrency;
    BigDecimal rate;

    public ExchangeRateDto() {
    }

    public ExchangeRateDto(CurrencyDto baseCurrency, CurrencyDto targetCurrency, BigDecimal rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public CurrencyDto getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyDto baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyDto getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(CurrencyDto targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
