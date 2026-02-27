package com.example.orderService.service;

import com.example.orderService.dto.Product;
import com.example.orderService.entity.Order;
import com.example.orderService.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository repo;
    private final RestTemplate restTemplate;
    private final FeatureFlagService featureFlagService;

    @Value("${product.service.url}")
    private String PRODUCT_URL;

    public OrderService(
            OrderRepository repo,
            RestTemplate restTemplate,
            FeatureFlagService featureFlagService
    ) {
        this.repo = repo;
        this.restTemplate = restTemplate;
        this.featureFlagService = featureFlagService;
    }

    public List<Order> getAll() {
        return repo.findAll();
    }

    public Order getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public Order create(Order order) {

    Product product =
            restTemplate.getForObject(PRODUCT_URL + order.getProductId(), Product.class);

    if (product == null || product.getQuantity() < order.getQuantity()) {
        throw new RuntimeException("Product unavailable");
    }

    double totalPrice = product.getPrice() * order.getQuantity();

    if (featureFlagService.isBulkOrderDiscountEnabled() && order.getQuantity() > 5) {
        totalPrice = totalPrice * 0.85;
    }

    order.setTotalPrice(totalPrice);
    order.setStatus("Created");

    Order savedOrder = repo.save(order);

    if (featureFlagService.isOrderNotificationsEnabled()) {
        log.info(
            "Order confirmation: orderId={}, productId={}, quantity={}, totalPrice={}",
            savedOrder.getId(),
            savedOrder.getProductId(),
            savedOrder.getQuantity(),
            savedOrder.getTotalPrice()
        );
    }

    return savedOrder;
}
}
