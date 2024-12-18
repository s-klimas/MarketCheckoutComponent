package pl.sebastianklimas.marketcheckoutcomponent.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private long id;
    private String name;
    private BigDecimal regularPrice;
    private int quantity;
    private Boolean isMultiPrice;
    private Boolean isBundle;
    private BigDecimal specialPrice;
    private Integer requiredQuantity;
    private Long bundleWith;
    private BigDecimal bundleDiscount;
    private int alreadyBundled;

    public Product() {
    }

    public Product(long id, String name, BigDecimal regularPrice, int quantity, Boolean isMultiPrice, Boolean isBundle, BigDecimal specialPrice, Integer requiredQuantity, Long bundleWith, BigDecimal bundleDiscount) {
        this.id = id;
        this.name = name;
        this.regularPrice = regularPrice;
        this.quantity = quantity;
        this.isMultiPrice = isMultiPrice;
        this.isBundle = isBundle;
        this.specialPrice = specialPrice;
        this.requiredQuantity = requiredQuantity;
        this.bundleWith = bundleWith;
        this.bundleDiscount = bundleDiscount;
        this.alreadyBundled = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Boolean getMultiPrice() {
        return isMultiPrice;
    }

    public void setMultiPrice(Boolean multiPrice) {
        isMultiPrice = multiPrice;
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

    public int getAlreadyBundled() {
        return alreadyBundled;
    }

    public void setAlreadyBundled(int alreadyBundled) {
        this.alreadyBundled = alreadyBundled;
    }

    public void addNewBundles(int newBundles) {
        this.alreadyBundled += newBundles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && quantity == product.quantity && alreadyBundled == product.alreadyBundled && Objects.equals(name, product.name) && Objects.equals(regularPrice, product.regularPrice) && Objects.equals(isMultiPrice, product.isMultiPrice) && Objects.equals(isBundle, product.isBundle) && Objects.equals(specialPrice, product.specialPrice) && Objects.equals(requiredQuantity, product.requiredQuantity) && Objects.equals(bundleWith, product.bundleWith) && Objects.equals(bundleDiscount, product.bundleDiscount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, regularPrice, quantity, isMultiPrice, isBundle, specialPrice, requiredQuantity, bundleWith, bundleDiscount, alreadyBundled);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", regularPrice=" + regularPrice +
                ", quantity=" + quantity +
                ", isMultiPrice=" + isMultiPrice +
                ", isBundle=" + isBundle +
                ", specialPrice=" + specialPrice +
                ", requiredQuantity=" + requiredQuantity +
                ", bundleWith=" + bundleWith +
                ", bundleDiscount=" + bundleDiscount +
                ", alreadyBundled=" + alreadyBundled +
                '}';
    }
}
