package com.catering.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ActivityType type;

    @ManyToOne
    private Order order;

    private LocalDate date;
    private BigDecimal amount;

    public Activity() {}

    public Long getId() { return id; }
    public ActivityType getType() { return type; }
    public Order getOrder() { return order; }
    public LocalDate getDate() { return date; }
    public BigDecimal getAmount() { return amount; }

    public void setType(ActivityType type) { this.type = type; }
    public void setOrder(Order order) { this.order = order; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    @Override
    public String toString() {
        return "[" + id + "] " + type + " | order #" + order.getId()
                + " | date: " + date + " | amount: " + amount + " PLN";
    }
}
