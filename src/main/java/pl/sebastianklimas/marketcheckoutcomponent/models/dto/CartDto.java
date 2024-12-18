package pl.sebastianklimas.marketcheckoutcomponent.models.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartDto {
    private List<FinalProduct> products;
    private BigDecimal sumPrice;
    private List<Bundle> bundles;
    private BigDecimal bundlesDiscount;

    public CartDto() {
    }

    public CartDto(List<FinalProduct> products, BigDecimal sumPrice, List<Bundle> bundles, BigDecimal bundlesDiscount) {
        this.products = products;
        this.sumPrice = sumPrice;
        this.bundles = bundles;
        this.bundlesDiscount = bundlesDiscount;
    }

    public List<FinalProduct> getProducts() {
        return products;
    }

    public void setProducts(List<FinalProduct> products) {
        this.products = products;
    }

    public BigDecimal getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(BigDecimal sumPrice) {
        this.sumPrice = sumPrice;
    }

    public List<Bundle> getBundles() {
        return bundles;
    }

    public void setBundles(List<Bundle> bundles) {
        this.bundles = bundles;
    }

    public BigDecimal getBundlesDiscount() {
        return bundlesDiscount;
    }

    public void setBundlesDiscount(BigDecimal bundlesDiscount) {
        this.bundlesDiscount = bundlesDiscount;
    }

    @Override
    public String toString() {
        return "CartDto{" +
                "products=" + products +
                ", sumPrice=" + sumPrice +
                ", bundles=" + bundles +
                ", bundlesDiscount=" + bundlesDiscount +
                '}';
    }
}
