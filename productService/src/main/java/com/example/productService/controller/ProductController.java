package com.example.productService.controller;


import com.example.productService.entity.Product;
import com.example.productService.repository.ProductRepository;
import com.example.productService.service.FeatureFlagService;
import com.example.productService.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;
    private final FeatureFlagService featureFlagService;
    private final ProductRepository repo;

    public ProductController(ProductService service, FeatureFlagService featureFlagService, ProductRepository repo) {
        this.service = service;
        this.featureFlagService = featureFlagService;
        this.repo = repo;
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return service.create(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/premium")
    public List<Product> getPremiumProducts() {
        boolean premiumEnabled = featureFlagService.isPremiumPricingEnabled();

        return repo.findAll()
                .stream()
                .map(p -> {
                    if (premiumEnabled) {
                        // create a copy instead of mutating the managed entity
                        return new Product(
                                p.getId(),
                                p.getName(),
                                p.getPrice() * 0.9,
                                p.getQuantity()
                        );
                    }
                    return p;
                })
                .toList();
    }
}
