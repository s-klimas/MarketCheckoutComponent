package pl.sebastianklimas.marketcheckoutcomponent.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal regularPrice;
    @JsonProperty("isMultiPrice")
    private Boolean isMultiPrice = false;
    @JsonProperty("isBundle")
    private Boolean isBundle = false;

    private BigDecimal specialPrice;
    private Integer requiredQuantity;
    private Long bundleWith;
    private BigDecimal bundleDiscount;

    public ProductDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(BigDecimal regularPrice) {
        this.regularPrice = regularPrice;
    }

    public Boolean getMultiPriced() {
        return isMultiPrice;
    }

    public void setMultiPriced(Boolean multiPriced) {
        isMultiPrice = multiPriced;
    }

    public Boolean getBundle() {
        return isBundle;
    }

    public void setBundle(Boolean bundle) {
        isBundle = bundle;
    }

    public BigDecimal getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(BigDecimal specialPrice) {
        this.specialPrice = specialPrice;
    }

    public Integer getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(Integer requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public Long getBundleWith() {
        return bundleWith;
    }

    public void setBundleWith(Long bundleWith) {
        this.bundleWith = bundleWith;
    }

    public BigDecimal getBundleDiscount() {
        return bundleDiscount;
    }

    public void setBundleDiscount(BigDecimal bundleDiscount) {
        this.bundleDiscount = bundleDiscount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto that = (ProductDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(regularPrice, that.regularPrice) && Objects.equals(isMultiPrice, that.isMultiPrice) && Objects.equals(isBundle, that.isBundle) && Objects.equals(specialPrice, that.specialPrice) && Objects.equals(requiredQuantity, that.requiredQuantity) && Objects.equals(bundleWith, that.bundleWith) && Objects.equals(bundleDiscount, that.bundleDiscount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, regularPrice, isMultiPrice, isBundle, specialPrice, requiredQuantity, bundleWith, bundleDiscount);
    }

    @Override
    public String toString() {
        String result = "Product: "+ name + " ID - " + id + " Price - " + regularPrice + " Multi-priced - " + isMultiPrice + " Bundle - " + isBundle;
        result += isMultiPrice ? " Required quantity - " + requiredQuantity + " Special price - " + specialPrice : "";
        result += isBundle ? " Bundle with - " + bundleWith + " Bundle discount - " + bundleDiscount : "";
        return result;
    }
}
