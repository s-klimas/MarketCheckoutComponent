package pl.sebastianklimas.marketcheckoutcomponent.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import pl.sebastianklimas.marketcheckoutcomponent.models.dto.CartDto;
import pl.sebastianklimas.marketcheckoutcomponent.models.dto.ProductDto;
import pl.sebastianklimas.marketcheckoutcomponent.service.MarketService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MarketCheckoutController {
    private final MarketService marketService;

    public MarketCheckoutController(MarketService marketService) {
        this.marketService = marketService;
    }

    @PostMapping("/checkout")
    @ResponseBody
    public ResponseEntity<CartDto> marketController(@RequestBody List<ProductDto> products) {
        return ResponseEntity.ok(marketService.scanProducts(products));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException() {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Cannot read request data, make sure, you're using JSON file with array of products structured like this: [{\"id\": 1, \"name\": \"Mleko\", \"regularPrice\": 9.99}], for more info check out GitHub project.");

        return ResponseEntity.badRequest().body(body);
    }
}
