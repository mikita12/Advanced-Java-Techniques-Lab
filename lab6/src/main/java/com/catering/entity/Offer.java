package com.catering.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OfferType type;

    private String name;
    private BigDecimal price;

    public Offer() {}

    public Offer(OfferType type, String name, BigDecimal price) {
        this.type = type;
        this.name = name;
        this.price = price;
    }

    public Long getId() { return id; }
    public OfferType getType() { return type; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }

    public void setType(OfferType type) { this.type = type; }
    public void setName(String name) { this.name = name; }
    public void setPrice(BigDecimal price) { this.price = price; }

    @Override
    public String toString() {
        return "[" + id + "] " + type + " - " + name + " | " + price + " PLN";
    }
}
