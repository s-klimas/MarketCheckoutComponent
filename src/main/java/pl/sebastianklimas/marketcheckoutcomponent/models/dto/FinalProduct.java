package pl.sebastianklimas.marketcheckoutcomponent.models.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class FinalProduct {
    private String name;
    private BigDecimal price;
    private int quantity;
    private BigDecimal oldPrice;

    public FinalProduct() {
    }

    public FinalProduct(String name, BigDecimal price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public FinalProduct(String name, BigDecimal price, int quantity, BigDecimal oldPrice) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.oldPrice = oldPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinalProduct that = (FinalProduct) o;
        return quantity == that.quantity && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(oldPrice, that.oldPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, quantity, oldPrice);
    }

    @Override
    public String toString() {
        String result = name + "\t" + price + "$ x" + quantity;
        result += oldPrice != null ? " (Multi-price discount accounted, old price: " + oldPrice + "$)" : "";
        return result;
    }
}
