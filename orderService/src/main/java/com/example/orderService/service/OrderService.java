package com.example.orderService.service;

import com.example.orderService.dto.Product;
import com.example.orderService.entity.Order;
import com.example.orderService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repo;
    private final RestTemplate restTemplate;

    @Value("${product.service.url}")
    private String productServiceUrl;

    public OrderService(OrderRepository repo, RestTemplate restTemplate) {
        this.repo = repo;
        this.restTemplate = restTemplate;
    }

    public List<Order> getAll() {
        return repo.findAll();
    }

    public Order getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public Order create(Order order) {

        Product product =
                restTemplate.getForObject(
                        productServiceUrl + "/api/products/" + order.getProductId(),
                        Product.class
                );

        if (product == null || product.getQuantity() < order.getQuantity()) {
            throw new RuntimeException("Product unavailable");
        }

        order.setTotalPrice(product.getPrice() * order.getQuantity());
        order.setStatus("Created");

        return repo.save(order);
    }
}
