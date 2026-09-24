package org.shop.flipkartorderservice.payment.paymentEntity;

import jakarta.persistence.*;

    @Entity
    @Table(name = "payments")
    public class PaymentEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private Long orderId;
        private double amount;
        private String paymentStatus;

        public PaymentEntity() {}
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public String getPaymentStatus() { return paymentStatus; }
        public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    }

