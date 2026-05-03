package com.catering.controller;

import com.catering.entity.Activity;
import com.catering.entity.Order;
import com.catering.service.ActivityService;
import com.catering.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;
    private final OrderService orderService;

    public ActivityController(ActivityService activityService, OrderService orderService) {
        this.activityService = activityService;
        this.orderService = orderService;
    }

    @GetMapping("/order/{orderId}")
    public List<Activity> getByOrder(@PathVariable Long orderId) {
        return activityService.findByOrder(orderId);
    }

    public record PaymentRequest(LocalDate date, BigDecimal amount) {}
    public record DeliveryRequest(LocalDate date) {}

    @PostMapping("/order/{orderId}/payment")
    public ResponseEntity<?> registerPayment(@PathVariable Long orderId,
                                             @RequestBody PaymentRequest req) {
        Order order = orderService.findById(orderId).orElse(null);
        if (order == null) return ResponseEntity.notFound().build();
        Activity activity = activityService.registerPayment(order, req.date(), req.amount());
        return ResponseEntity.ok(activity);
    }

    @PostMapping("/order/{orderId}/delivery")
    public ResponseEntity<?> registerDelivery(@PathVariable Long orderId,
                                              @RequestBody DeliveryRequest req) {
        Order order = orderService.findById(orderId).orElse(null);
        if (order == null) return ResponseEntity.notFound().build();
        Activity activity = activityService.registerDelivery(order, req.date());
        return ResponseEntity.ok(activity);
    }
}