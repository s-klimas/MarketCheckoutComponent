package pl.sebastianklimas.marketcheckoutcomponent.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sebastianklimas.marketcheckoutcomponent.models.dto.CartDto;
import pl.sebastianklimas.marketcheckoutcomponent.models.dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MarketServiceTest {
    private MarketService marketService;

    @BeforeEach
    void init() {
        marketService = new MarketService();
    }

    @Test
    void test_ScanProducts_withoutAnyProduct_shouldReturnEmptyList() {
        // given
        List<ProductDto> emptyList = List.of();

        // when
        CartDto result = marketService.scanProducts(emptyList);

        // then
        assertEquals(0, result.getProducts().size());
        assertEquals(BigDecimal.ZERO, result.getSumPrice());
        assertEquals(0, result.getBundles().size());
        assertEquals(BigDecimal.ZERO, result.getBundlesDiscount());
    }

    @Test
    void test_ScanProducts_withOneItemRegularPrice_shouldReturnSamePrice() {
        // given
        ProductDto milk = new ProductDto();
        milk.setId(1L);
        milk.setName("Mleko");
        milk.setRegularPrice(BigDecimal.valueOf(6.99));

        List<ProductDto> products = List.of(milk);

        // when
        CartDto result = marketService.scanProducts(products);

        // then
        assertEquals(1, result.getProducts().size());

        assertEquals("Mleko", result.getProducts().get(0).getName());
        assertEquals(1, result.getProducts().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(6.99), result.getProducts().get(0).getPrice());

        assertEquals(BigDecimal.valueOf(6.99), result.getSumPrice());
        assertEquals(0, result.getBundles().size());
        assertEquals(BigDecimal.ZERO, result.getBundlesDiscount());
    }

    @Test
    void test_ScanProduct_withOnlyMultiPriceProduct_shouldReturnLowerPrice() {
        // given
        ProductDto lego = new ProductDto();
        lego.setId(2L);
        lego.setName("Lego");
        lego.setRegularPrice(BigDecimal.valueOf(29.99));
        lego.setMultiPriced(true);
        lego.setSpecialPrice(BigDecimal.valueOf(24.99));
        lego.setRequiredQuantity(3);

        List<ProductDto> products = List.of(lego, lego, lego);

        // when
        CartDto result = marketService.scanProducts(products);

        // then
        assertEquals(1, result.getProducts().size());

        assertEquals("Lego", result.getProducts().get(0).getName());
        assertEquals(3, result.getProducts().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(24.99), result.getProducts().get(0).getPrice());
        assertEquals(BigDecimal.valueOf(29.99), result.getProducts().get(0).getOldPrice());

        assertEquals(BigDecimal.valueOf(24.99).multiply(BigDecimal.valueOf(3)), result.getSumPrice());
        assertEquals(0, result.getBundles().size());
        assertEquals(BigDecimal.ZERO, result.getBundlesDiscount());
    }

    @Test
    void test_ScanProduct_withMultiPriceAndRegularProduct_shouldReturnListWithLowerPriceForMultiPriceAndWithoutChangeForRegular() {
        // given
        ProductDto milk = new ProductDto();
        milk.setId(1L);
        milk.setName("Mleko");
        milk.setRegularPrice(BigDecimal.valueOf(6.99));

        ProductDto lego = new ProductDto();
        lego.setId(2L);
        lego.setName("Lego");
        lego.setRegularPrice(BigDecimal.valueOf(29.99));
        lego.setMultiPriced(true);
        lego.setSpecialPrice(BigDecimal.valueOf(24.99));
        lego.setRequiredQuantity(3);

        List<ProductDto> products = List.of(milk, lego, lego, lego);

        // when
        CartDto result = marketService.scanProducts(products);

        // then
        assertEquals(2, result.getProducts().size());

        assertEquals("Mleko", result.getProducts().get(1).getName());
        assertEquals(1, result.getProducts().get(1).getQuantity());
        assertEquals(BigDecimal.valueOf(6.99), result.getProducts().get(1).getPrice());

        assertEquals("Lego", result.getProducts().get(0).getName());
        assertEquals(3, result.getProducts().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(24.99), result.getProducts().get(0).getPrice());
        assertEquals(BigDecimal.valueOf(29.99), result.getProducts().get(0).getOldPrice());

        assertEquals(BigDecimal.valueOf(24.99).multiply(BigDecimal.valueOf(3)).add(BigDecimal.valueOf(6.99)), result.getSumPrice());
        assertEquals(0, result.getBundles().size());
        assertEquals(BigDecimal.ZERO, result.getBundlesDiscount());
    }

    @Test
    void test_ScanProduct_withOnlyTwoBundledProducts_shouldReturnListOfTwoProductsWithRegularPriceButSubtractBundleDiscountFromOneBundle() {
        // given
        ProductDto console = new ProductDto();
        console.setId(3L);
        console.setName("Konsola");
        console.setRegularPrice(BigDecimal.valueOf(1500));
        console.setBundle(true);
        console.setBundleWith(4L);
        console.setBundleDiscount(BigDecimal.valueOf(200));

        ProductDto game = new ProductDto();
        game.setId(4L);
        game.setName("Gra komputerowa");
        game.setRegularPrice(BigDecimal.valueOf(50));

        List<ProductDto> products = List.of(console, game);

        // when
        CartDto result = marketService.scanProducts(products);

        // then
        assertEquals(2, result.getProducts().size());

        assertEquals("Konsola", result.getProducts().get(0).getName());
        assertEquals(1, result.getProducts().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(1500), result.getProducts().get(0).getPrice());

        assertEquals("Gra komputerowa", result.getProducts().get(1).getName());
        assertEquals(1, result.getProducts().get(1).getQuantity());
        assertEquals(BigDecimal.valueOf(50), result.getProducts().get(1).getPrice());

        assertEquals(BigDecimal.valueOf(1500).add(BigDecimal.valueOf(50)).subtract(BigDecimal.valueOf(200)), result.getSumPrice());
        assertEquals(1, result.getBundles().size());
        assertEquals(BigDecimal.valueOf(200), result.getBundlesDiscount());
        assertEquals(BigDecimal.valueOf(200), result.getBundles().get(0).getDiscount());
        assertEquals("Konsola x Gra komputerowa", result.getBundles().get(0).getName());
    }

    @Test
    void test_ScanProduct_withTwoBundledProductsAndOneRegular_shouldReturnListOfThreeWithRegularPriceAndBundleDiscount() {
        // given
        ProductDto milk = new ProductDto();
        milk.setId(1L);
        milk.setName("Mleko");
        milk.setRegularPrice(BigDecimal.valueOf(6.99));

        ProductDto console = new ProductDto();
        console.setId(3L);
        console.setName("Konsola");
        console.setRegularPrice(BigDecimal.valueOf(1500));
        console.setBundle(true);
        console.setBundleWith(4L);
        console.setBundleDiscount(BigDecimal.valueOf(200));

        ProductDto game = new ProductDto();
        game.setId(4L);
        game.setName("Gra komputerowa");
        game.setRegularPrice(BigDecimal.valueOf(50));

        List<ProductDto> products = List.of(console, game, milk);

        // when
        CartDto result = marketService.scanProducts(products);

        // then
        assertEquals(3, result.getProducts().size());

        assertEquals("Mleko", result.getProducts().get(2).getName());
        assertEquals(1, result.getProducts().get(2).getQuantity());
        assertEquals(BigDecimal.valueOf(6.99), result.getProducts().get(2).getPrice());

        assertEquals("Konsola", result.getProducts().get(0).getName());
        assertEquals(1, result.getProducts().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(1500), result.getProducts().get(0).getPrice());

        assertEquals("Gra komputerowa", result.getProducts().get(1).getName());
        assertEquals(1, result.getProducts().get(1).getQuantity());
        assertEquals(BigDecimal.valueOf(50), result.getProducts().get(1).getPrice());

        assertEquals(BigDecimal.valueOf(1500).add(BigDecimal.valueOf(50)).add(BigDecimal.valueOf(6.99)).subtract(BigDecimal.valueOf(200)), result.getSumPrice());
        assertEquals(1, result.getBundles().size());
        assertEquals(BigDecimal.valueOf(200), result.getBundlesDiscount());
        assertEquals(BigDecimal.valueOf(200), result.getBundles().get(0).getDiscount());
        assertEquals("Konsola x Gra komputerowa", result.getBundles().get(0).getName());
    }

    @Test
    void test_ScanProduct_withTwoBundledProductsAndThreeMultiPriceProduct_shouldReturnListOfThreeWithSpecialPriceProductsAndBundleDiscount() {
        // given
        ProductDto lego = new ProductDto();
        lego.setId(2L);
        lego.setName("Lego");
        lego.setRegularPrice(BigDecimal.valueOf(29.99));
        lego.setMultiPriced(true);
        lego.setSpecialPrice(BigDecimal.valueOf(24.99));
        lego.setRequiredQuantity(3);

        ProductDto console = new ProductDto();
        console.setId(3L);
        console.setName("Konsola");
        console.setRegularPrice(BigDecimal.valueOf(1500));
        console.setBundle(true);
        console.setBundleWith(4L);
        console.setBundleDiscount(BigDecimal.valueOf(200));

        ProductDto game = new ProductDto();
        game.setId(4L);
        game.setName("Gra komputerowa");
        game.setRegularPrice(BigDecimal.valueOf(50));

        List<ProductDto> products = List.of(lego, lego, lego, console, game);

        // when
        CartDto result = marketService.scanProducts(products);

        // then
        assertEquals(3, result.getProducts().size());

        assertEquals("Lego", result.getProducts().get(1).getName());
        assertEquals(3, result.getProducts().get(1).getQuantity());
        assertEquals(BigDecimal.valueOf(24.99), result.getProducts().get(1).getPrice());
        assertEquals(BigDecimal.valueOf(29.99), result.getProducts().get(1).getOldPrice());

        assertEquals("Konsola", result.getProducts().get(0).getName());
        assertEquals(1, result.getProducts().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(1500), result.getProducts().get(0).getPrice());

        assertEquals("Gra komputerowa", result.getProducts().get(2).getName());
        assertEquals(1, result.getProducts().get(2).getQuantity());
        assertEquals(BigDecimal.valueOf(50), result.getProducts().get(2).getPrice());

        assertEquals(BigDecimal.valueOf(1500).add(BigDecimal.valueOf(50)).add(BigDecimal.valueOf(24.99).multiply(BigDecimal.valueOf(3))).subtract(BigDecimal.valueOf(200)), result.getSumPrice());
        assertEquals(1, result.getBundles().size());
        assertEquals(BigDecimal.valueOf(200), result.getBundlesDiscount());
        assertEquals(BigDecimal.valueOf(200), result.getBundles().get(0).getDiscount());
        assertEquals("Konsola x Gra komputerowa", result.getBundles().get(0).getName());
    }

    @Test
    void test_ScanProduct_withTwoBundledThreeMultiPriceAndOneRegularProduct_shouldReturnListOfFourWithSpecialAndRegularPriceAndBundleDiscount() {
        // given
        ProductDto milk = new ProductDto();
        milk.setId(1L);
        milk.setName("Mleko");
        milk.setRegularPrice(BigDecimal.valueOf(6.99));

        ProductDto lego = new ProductDto();
        lego.setId(2L);
        lego.setName("Lego");
        lego.setRegularPrice(BigDecimal.valueOf(29.99));
        lego.setMultiPriced(true);
        lego.setSpecialPrice(BigDecimal.valueOf(24.99));
        lego.setRequiredQuantity(3);

        ProductDto console = new ProductDto();
        console.setId(3L);
        console.setName("Konsola");
        console.setRegularPrice(BigDecimal.valueOf(1500));
        console.setBundle(true);
        console.setBundleWith(4L);
        console.setBundleDiscount(BigDecimal.valueOf(200));

        ProductDto game = new ProductDto();
        game.setId(4L);
        game.setName("Gra komputerowa");
        game.setRegularPrice(BigDecimal.valueOf(50));

        List<ProductDto> products = List.of(milk, lego, lego, lego, console, game);

        // when
        CartDto result = marketService.scanProducts(products);

        // then
        assertEquals(4, result.getProducts().size());

        assertEquals("Mleko", result.getProducts().get(3).getName());
        assertEquals(1, result.getProducts().get(3).getQuantity());
        assertEquals(BigDecimal.valueOf(6.99), result.getProducts().get(3).getPrice());

        assertEquals("Lego", result.getProducts().get(1).getName());
        assertEquals(3, result.getProducts().get(1).getQuantity());
        assertEquals(BigDecimal.valueOf(24.99), result.getProducts().get(1).getPrice());
        assertEquals(BigDecimal.valueOf(29.99), result.getProducts().get(1).getOldPrice());

        assertEquals("Konsola", result.getProducts().get(0).getName());
        assertEquals(1, result.getProducts().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(1500), result.getProducts().get(0).getPrice());

        assertEquals("Gra komputerowa", result.getProducts().get(2).getName());
        assertEquals(1, result.getProducts().get(2).getQuantity());
        assertEquals(BigDecimal.valueOf(50), result.getProducts().get(2).getPrice());

        assertEquals(BigDecimal.valueOf(1500).add(BigDecimal.valueOf(50)).add(BigDecimal.valueOf(24.99).multiply(BigDecimal.valueOf(3))).add(BigDecimal.valueOf(6.99)).subtract(BigDecimal.valueOf(200)), result.getSumPrice());
        assertEquals(1, result.getBundles().size());
        assertEquals(BigDecimal.valueOf(200), result.getBundlesDiscount());
        assertEquals(BigDecimal.valueOf(200), result.getBundles().get(0).getDiscount());
        assertEquals("Konsola x Gra komputerowa", result.getBundles().get(0).getName());
    }

    @Test
    void test_ScanProduct_withMultiPriceProductButNotEnoughOfThemToTriggerSpecialPriceForAllOfThem_shouldReturnListWithSomeSpecialPriceAndSomeRegularPrice() {
        // given
        ProductDto lego = new ProductDto();
        lego.setId(2L);
        lego.setName("Lego");
        lego.setRegularPrice(BigDecimal.valueOf(29.99));
        lego.setMultiPriced(true);
        lego.setSpecialPrice(BigDecimal.valueOf(24.99));
        lego.setRequiredQuantity(3);

        List<ProductDto> products = List.of(lego, lego, lego, lego, lego);

        // when
        CartDto result = marketService.scanProducts(products);

        // then
        assertEquals(2, result.getProducts().size());

        assertEquals("Lego", result.getProducts().get(0).getName());
        assertEquals(3, result.getProducts().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(24.99), result.getProducts().get(0).getPrice());
        assertEquals(BigDecimal.valueOf(29.99), result.getProducts().get(0).getOldPrice());

        assertEquals("Lego", result.getProducts().get(1).getName());
        assertEquals(2, result.getProducts().get(1).getQuantity());
        assertEquals(BigDecimal.valueOf(29.99), result.getProducts().get(1).getPrice());

        assertEquals(BigDecimal.valueOf(29.99).multiply(BigDecimal.valueOf(2)).add(BigDecimal.valueOf(24.99).multiply(BigDecimal.valueOf(3))), result.getSumPrice());
        assertEquals(0, result.getBundles().size());
        assertEquals(BigDecimal.ZERO, result.getBundlesDiscount());
    }

    @Test
    void test_ScanProduct_withOneBundleProduct_shouldReturnRegularPriceWithoutBundleDiscount() {
        // given
        ProductDto console = new ProductDto();
        console.setId(3L);
        console.setName("Konsola");
        console.setRegularPrice(BigDecimal.valueOf(1500));
        console.setBundle(true);
        console.setBundleWith(4L);
        console.setBundleDiscount(BigDecimal.valueOf(200));

        List<ProductDto> products = List.of(console);

        // when
        CartDto result = marketService.scanProducts(products);

        // then
        assertEquals(1, result.getProducts().size());

        assertEquals("Konsola", result.getProducts().get(0).getName());
        assertEquals(1, result.getProducts().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(1500), result.getProducts().get(0).getPrice());

        assertEquals(BigDecimal.valueOf(1500), result.getSumPrice());
        assertEquals(0, result.getBundles().size());
    }

    @Test
    void test_ScanProduct_withTwoWayBundle_shouldReturnListWithOneBundle() {
        // given
        ProductDto console = new ProductDto();
        console.setId(3L);
        console.setName("Konsola");
        console.setRegularPrice(BigDecimal.valueOf(1500));
        console.setBundle(true);
        console.setBundleWith(4L);
        console.setBundleDiscount(BigDecimal.valueOf(200));

        ProductDto game = new ProductDto();
        game.setId(4L);
        game.setName("Gra komputerowa");
        game.setRegularPrice(BigDecimal.valueOf(50));
        game.setBundle(true);
        game.setBundleWith(3L);
        game.setBundleDiscount(BigDecimal.valueOf(200));

        List<ProductDto> products = List.of(console, game);

        // when
        CartDto result = marketService.scanProducts(products);

        // then
        assertEquals(2, result.getProducts().size());

        assertEquals("Konsola", result.getProducts().get(1).getName());
        assertEquals(1, result.getProducts().get(1).getQuantity());
        assertEquals(BigDecimal.valueOf(1500), result.getProducts().get(1).getPrice());

        assertEquals("Gra komputerowa", result.getProducts().get(0).getName());
        assertEquals(1, result.getProducts().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(50), result.getProducts().get(0).getPrice());

        assertEquals(BigDecimal.valueOf(1500).add(BigDecimal.valueOf(50)).subtract(BigDecimal.valueOf(200)), result.getSumPrice());
        assertEquals(1, result.getBundles().size());
        assertEquals(BigDecimal.valueOf(200), result.getBundlesDiscount());
        assertEquals(BigDecimal.valueOf(200), result.getBundles().get(0).getDiscount());
        assertEquals("Gra komputerowa x Konsola", result.getBundles().get(0).getName());
    }

    @Test
    void test_ScanProduct_withTwoTheSameRegularProduct_shouldReturnListOfOneWithQuantitySetOnTwo() {
        // given
        ProductDto milk = new ProductDto();
        milk.setId(1L);
        milk.setName("Mleko");
        milk.setRegularPrice(BigDecimal.valueOf(6.99));

        List<ProductDto> products = List.of(milk, milk);

        // when
        CartDto result = marketService.scanProducts(products);

        // then
        assertEquals(1, result.getProducts().size());

        assertEquals("Mleko", result.getProducts().get(0).getName());
        assertEquals(2, result.getProducts().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(6.99), result.getProducts().get(0).getPrice());

        assertEquals(BigDecimal.valueOf(6.99).multiply(BigDecimal.valueOf(2)), result.getSumPrice());
        assertEquals(0, result.getBundles().size());
        assertEquals(BigDecimal.ZERO, result.getBundlesDiscount());
    }

    @Test
    void test_ScanProduct_withACoupleOfBundledProducts_shouldReturnListWithMoreThenOneBundle() {
        // given
        ProductDto console = new ProductDto();
        console.setId(3L);
        console.setName("Konsola");
        console.setRegularPrice(BigDecimal.valueOf(1500));
        console.setBundle(true);
        console.setBundleWith(4L);
        console.setBundleDiscount(BigDecimal.valueOf(200));

        ProductDto game = new ProductDto();
        game.setId(4L);
        game.setName("Gra komputerowa");
        game.setRegularPrice(BigDecimal.valueOf(50));

        ProductDto milk = new ProductDto();
        milk.setId(1L);
        milk.setName("Mleko");
        milk.setRegularPrice(BigDecimal.valueOf(6.99));
        milk.setBundle(true);
        milk.setBundleWith(6L);
        milk.setBundleDiscount(BigDecimal.valueOf(0.5));

        ProductDto cereals = new ProductDto();
        cereals.setId(6L);
        cereals.setName("Płatki śniadaniowe");
        cereals.setRegularPrice(BigDecimal.valueOf(9.99));

        List<ProductDto> products = List.of(console, game, milk, cereals);

        // when
        CartDto result = marketService.scanProducts(products);

        // then
        assertEquals(4, result.getProducts().size());

        assertEquals("Konsola", result.getProducts().get(1).getName());
        assertEquals(1, result.getProducts().get(1).getQuantity());
        assertEquals(BigDecimal.valueOf(1500), result.getProducts().get(1).getPrice());

        assertEquals("Gra komputerowa", result.getProducts().get(3).getName());
        assertEquals(1, result.getProducts().get(3).getQuantity());
        assertEquals(BigDecimal.valueOf(50), result.getProducts().get(3).getPrice());

        assertEquals("Mleko", result.getProducts().get(2).getName());
        assertEquals(1, result.getProducts().get(2).getQuantity());
        assertEquals(BigDecimal.valueOf(6.99), result.getProducts().get(2).getPrice());

        assertEquals("Płatki śniadaniowe", result.getProducts().get(0).getName());
        assertEquals(1, result.getProducts().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(9.99), result.getProducts().get(0).getPrice());

        assertEquals(BigDecimal.valueOf(1500).add(BigDecimal.valueOf(50)).add(BigDecimal.valueOf(6.99)).add(BigDecimal.valueOf(9.99)).subtract(BigDecimal.valueOf(200)).subtract(BigDecimal.valueOf(0.5)), result.getSumPrice());
        assertEquals(2, result.getBundles().size());
        assertEquals(BigDecimal.valueOf(200.5), result.getBundlesDiscount());
        assertEquals(BigDecimal.valueOf(200), result.getBundles().get(0).getDiscount());
        assertEquals("Konsola x Gra komputerowa", result.getBundles().get(0).getName());
        assertEquals(BigDecimal.valueOf(0.5), result.getBundles().get(1).getDiscount());
        assertEquals("Mleko x Płatki śniadaniowe", result.getBundles().get(1).getName());
    }
}