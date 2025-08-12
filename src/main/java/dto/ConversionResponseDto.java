package dto;

import java.math.BigDecimal;

public class ConversionResponseDto {
    private CurrencyDto baseCurrencyCode;
    private CurrencyDto targetCurrencyCode;
    private  BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public ConversionResponseDto(CurrencyDto baseCurrencyCode, CurrencyDto targetCurrencyCode, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public CurrencyDto getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(CurrencyDto baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public CurrencyDto getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(CurrencyDto targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}
