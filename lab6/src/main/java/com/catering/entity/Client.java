package com.catering.entity;

import jakarta.persistence.*;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String clientNumber;

    public Client() {}

    public Client(String firstName, String lastName, String clientNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientNumber = clientNumber;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getClientNumber() { return clientNumber; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setClientNumber(String clientNumber) { this.clientNumber = clientNumber; }

    @Override
    public String toString() {
        return "[" + id + "] " + firstName + " " + lastName + " (nr: " + clientNumber + ")";
    }
}
