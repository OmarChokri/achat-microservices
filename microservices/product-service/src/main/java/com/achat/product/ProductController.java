package com.achat.product;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final List<Product> products = List.of(
            new Product("p-1", "Laptop", 1200.0, "Electronics"),
            new Product("p-2", "Mouse", 35.0, "Accessories"),
            new Product("p-3", "Keyboard", 85.0, "Accessories")
    );

    @GetMapping("/products")
    @Retry(name = "productService")
    @RateLimiter(name = "productService")
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProducts")
    public List<Product> getProducts() {
        return products;
    }

    @GetMapping("/products/{id}")
    @Retry(name = "productService")
    @RateLimiter(name = "productService")
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProduct")
    public Product getProductById(@PathVariable String id) {
        return products.stream()
                .filter(product -> product.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    public List<Product> fallbackProducts(Throwable throwable) {
        return List.of(new Product("fallback-1", "Unavailable product list", 0.0, "System"));
    }

    public Product fallbackProduct(String id, Throwable throwable) {
        return new Product(id, "Fallback product", 0.0, "System");
    }
}
