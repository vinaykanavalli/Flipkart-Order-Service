package org.shop.flipkartorderservice.order.orderRespnse;

public class OrderResponse {
    private Long orderId;
    private String status;

    public OrderResponse(Long orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}