package org.shop.flipkartorderservice.notification.notificationRequest;

public class NotificationRequest {
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private Long customerId;
    private String message;}
