package org.shop.flipkartorderservice.payment.paymentResponse;


public class PaymentResponse {
    private Long orderId;
    private String paymentStatus;

    public PaymentResponse(Long orderId, String paymentStatus) {
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
    }

    public Long getOrderId() { return orderId; }
    public String getPaymentStatus() { return paymentStatus; }
}