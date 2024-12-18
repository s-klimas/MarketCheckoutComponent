package pl.sebastianklimas.marketcheckoutcomponent.service;

import org.springframework.stereotype.Service;
import pl.sebastianklimas.marketcheckoutcomponent.models.dto.Bundle;
import pl.sebastianklimas.marketcheckoutcomponent.models.Product;
import pl.sebastianklimas.marketcheckoutcomponent.models.dto.CartDto;
import pl.sebastianklimas.marketcheckoutcomponent.models.dto.FinalProduct;
import pl.sebastianklimas.marketcheckoutcomponent.models.dto.ProductDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarketService {
    public CartDto scanProducts(List<ProductDto> productDtos) {
        Map<ProductDto, Long> productsGrouped = groupProducts(productDtos);

        List<Product> products = mapToProduct(productsGrouped);

        List<Bundle> bundles = getBundles(products);

        List<FinalProduct> finalProducts = mapToFinalProduct(products);

        BigDecimal bundlesDiscount = calculateBundlesDiscount(bundles);

        return new CartDto(finalProducts, calculateFinalSum(finalProducts, bundlesDiscount), bundles, bundlesDiscount);
    }

    private Map<ProductDto, Long> groupProducts(List<ProductDto> productDtos) {
        return productDtos.stream()
                .collect(Collectors.groupingBy(
                        p -> p,
                        Collectors.counting()
                ));
    }

    private List<Product> mapToProduct(Map<ProductDto, Long> productsGrouped) {
        return productsGrouped.keySet().stream()
                .map(productDto -> new Product(
                        productDto.getId(),
                        productDto.getName(),
                        productDto.getRegularPrice(),
                        productsGrouped.get(productDto).intValue(),
                        productDto.getMultiPriced(),
                        productDto.getBundle(),

                        productDto.getMultiPriced() ? productDto.getSpecialPrice() : null,
                        productDto.getMultiPriced() ? productDto.getRequiredQuantity() : null,
                        productDto.getBundle() ? productDto.getBundleWith() : null,
                        productDto.getBundle() ? productDto.getBundleDiscount() : null
                ))
                .toList();
    }

    private List<Bundle> getBundles(List<Product> products) {
        List<Bundle> bundles = new ArrayList<>();
        products.stream()
                .filter(Product::getBundle)
                .forEach(product -> findProductToBundle(products, product.getBundleWith()).ifPresent(bundleProduct -> bundles.addAll(createBundles(product, bundleProduct))));
        return bundles;
    }

    private Optional<Product> findProductToBundle(List<Product> products, long bundleWith) {
        return products.stream()
                .filter(p -> p.getId() == bundleWith)
                .findFirst();
    }

    private List<Bundle> createBundles(Product product, Product bundleProduct) {
        int productsToBundle = product.getQuantity() - product.getAlreadyBundled();
        if (productsToBundle <= 0) return List.of();

        int productsToBundleWith = bundleProduct.getQuantity() - bundleProduct.getAlreadyBundled();
        int maxBundles = Math.min(productsToBundle, productsToBundleWith);

        if (maxBundles <= 0) return List.of();

        List<Bundle> bundles = new ArrayList<>();
        for (int i = 0; i < maxBundles; i++) {
            bundles.add(new Bundle(product.getName() + " x " + bundleProduct.getName(), product.getBundleDiscount()
            ));
        }

        product.addNewBundles(maxBundles);
        bundleProduct.addNewBundles(maxBundles);

        return bundles;
    }

    private List<FinalProduct> mapToFinalProduct(List<Product> products) {
        return products.stream()
                .map(this::createFinalProductFromProduct)
                .flatMap(List::stream)
                .toList();
    }

    private List<FinalProduct> createFinalProductFromProduct(Product product) {
        if (!product.getMultiPrice() || product.getQuantity() < product.getRequiredQuantity()) {
            return List.of(
                    new FinalProduct(
                            product.getName(),
                            product.getRegularPrice(),
                            product.getQuantity()
                    ));
        }
        int quantity = product.getQuantity();
        int requiredQuantity = product.getRequiredQuantity();
        int productsWithoutMultiPrice = quantity % requiredQuantity;

        FinalProduct mainFinalProduct = new FinalProduct(
                product.getName(),
                product.getSpecialPrice(),
                (quantity / requiredQuantity) * requiredQuantity,
                product.getRegularPrice()
        );

        FinalProduct extraFinalProduct = productsWithoutMultiPrice > 0 ? new FinalProduct(product.getName(), product.getRegularPrice(), productsWithoutMultiPrice) : null;

        return extraFinalProduct == null ? List.of(mainFinalProduct) : List.of(mainFinalProduct, extraFinalProduct);
    }

    private BigDecimal calculateFinalSum(List<FinalProduct> products, BigDecimal bundlesDiscount) {
        BigDecimal fullPrice = products.stream()
                .map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return fullPrice.subtract(bundlesDiscount);
    }

    private BigDecimal calculateBundlesDiscount(List<Bundle> bundles) {
        return bundles.stream()
                .map(Bundle::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
