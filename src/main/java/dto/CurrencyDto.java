package dto;

public class CurrencyDto {
    String code;
    String name;
    String sign;

    public CurrencyDto() {
    }


    public CurrencyDto(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
