package com.hotel.model;

public class Payment {
    private String paymentId;
    private double amount;
    private String method; // "Credit Card", "Cash"

    public Payment(String paymentId, double amount, String method) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.method = method;
    }

    public void processPayment() {
        System.out.println("Processing payment of $" + amount + " via " + method);
        System.out.println("Payment Successful!");
    }

    // Getters
    public String getPaymentId() { return paymentId; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
}
