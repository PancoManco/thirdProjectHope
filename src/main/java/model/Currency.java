package model;

public class Currency {
    int id;
    String code;
    String name;
    String sign;

    public Currency() {
    }

    public Currency(int id, String code, String name, String sign) {

        this.id = id;
        this.name = name;
        this.code = code;

        this.sign = sign;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
