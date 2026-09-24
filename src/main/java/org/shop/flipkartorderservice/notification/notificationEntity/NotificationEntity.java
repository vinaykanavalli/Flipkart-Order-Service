package org.shop.flipkartorderservice.notification.notificationEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private String message;

    public NotificationEntity() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
