package com.bootteam.serviceb.domain;

import java.time.LocalDate;

public class Payment {

    private String customerNumber;
    private String cardNumber;
    private LocalDate cardExpiryDate;
    private Double amount;

    public Payment(String customerNumber, String cardNumber, LocalDate cardExpiryDate, Double amount) {
        this.customerNumber = customerNumber;
        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
        this.amount = amount;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(LocalDate cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
