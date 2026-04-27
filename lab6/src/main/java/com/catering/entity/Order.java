package com.catering.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Offer> offers = new ArrayList<>();

    private BigDecimal totalAmount;
    private String address;
    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.NEW;

    public Order() {}

    public Long getId() { return id; }
    public Client getClient() { return client; }
    public List<Offer> getOffers() { return offers; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getAddress() { return address; }
    public LocalDate getDeliveryDate() { return deliveryDate; }
    public OrderStatus getStatus() { return status; }

    public void setClient(Client client) { this.client = client; }
    public void setOffers(List<Offer> offers) { this.offers = offers; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public void setAddress(String address) { this.address = address; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }
    public void setStatus(OrderStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "[" + id + "] " + client.getFirstName() + " " + client.getLastName()
                + " | " + address + " | delivery: " + deliveryDate
                + " | status: " + status + " | total: " + totalAmount + " PLN";
    }
}
