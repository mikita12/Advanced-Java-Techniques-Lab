package com.catering.service;

import com.catering.entity.*;
import com.catering.repository.ActivityRepository;
import com.catering.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ActivityRepository activityRepository;

    public OrderService(OrderRepository orderRepository, ActivityRepository activityRepository) {
        this.orderRepository = orderRepository;
        this.activityRepository = activityRepository;
    }

    public Order createOrder(Client client, List<Offer> offers, String address, LocalDate deliveryDate) {
        Order order = new Order();
        order.setClient(client);
        order.setOffers(offers);
        order.setAddress(address);
        order.setDeliveryDate(deliveryDate);
        BigDecimal total = offers.stream()
                .map(Offer::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findUnpaid() {
        return orderRepository.findByStatus(OrderStatus.NEW);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public boolean isPaid(Long orderId) {
        Optional<Order> opt = orderRepository.findById(orderId);
        if (opt.isEmpty()) return false;
        Order order = opt.get();
        BigDecimal paid = activityRepository.findByOrderIdAndType(orderId, ActivityType.PAYMENT)
                .stream()
                .map(Activity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return paid.compareTo(order.getTotalAmount()) >= 0;
    }
}
