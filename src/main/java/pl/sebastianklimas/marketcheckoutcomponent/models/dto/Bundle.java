package pl.sebastianklimas.marketcheckoutcomponent.models.dto;

import java.math.BigDecimal;

public class Bundle {
    private String name;
    private BigDecimal discount;

    public Bundle() {
    }

    public Bundle(String name, BigDecimal discount) {
        this.name = name;
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Bundle \"" + name + "\" discount: " + discount;
    }
}
