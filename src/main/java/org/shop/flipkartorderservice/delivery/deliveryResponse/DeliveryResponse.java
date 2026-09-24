package org.shop.flipkartorderservice.delivery.deliveryResponse;

public class DeliveryResponse {
    public DeliveryResponse(Long orderId, String trackingNumber, String deliveryStatus) {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private Long orderId;
    private String trackingNumber;
    private String status;
}
