package dto;

public class CurrencyDto {
    int id;
    String name;
    String code;

    String sign;

    public CurrencyDto() {
    }

    public CurrencyDto(int id,String code, String name, String sign) {
        this.id=id;
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public void setId(int id) {this.id =id;}
    public int getId() { return id; }
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
