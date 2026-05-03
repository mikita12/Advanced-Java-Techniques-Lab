package com.catering.controller;

import com.catering.entity.Client;
import com.catering.entity.Offer;
import com.catering.entity.Order;
import com.catering.service.ClientService;
import com.catering.service.OfferService;
import com.catering.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final ClientService clientService;
    private final OfferService offerService;

    public OrderController(OrderService orderService, ClientService clientService, OfferService offerService) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.offerService = offerService;
    }

    @GetMapping
    public List<Order> getAll() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/unpaid")
    public List<Order> getUnpaid() {
        return orderService.findUnpaid();
    }

    @GetMapping("/{id}/paid")
    public ResponseEntity<Boolean> isPaid(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.isPaid(id));
    }

    // DTO do tworzenia zamówienia
    public record CreateOrderRequest(
            Long clientId,
            List<Long> offerIds,
            String address,
            LocalDate deliveryDate
    ) {}

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateOrderRequest req) {
        Client client = clientService.findById(req.clientId()).orElse(null);
        if (client == null) return ResponseEntity.badRequest().body("Client not found");

        List<Offer> offers = req.offerIds().stream()
                .map(id -> offerService.findById(id).orElse(null))
                .filter(o -> o != null)
                .toList();

        Order order = orderService.createOrder(client, offers, req.address(), req.deliveryDate());
        return ResponseEntity.ok(order);
    }
}