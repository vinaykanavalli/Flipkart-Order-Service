package org.shop.flipkartorderservice.notification.notificationService;



import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.shop.flipkartorderservice.notification.notificationRepository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @KafkaListener(topics = {"order-created", "payment-success", "payment-failed", "delivery-created", "delivery-events"}, groupId = "notification-group")
    public void handleNotifications(ConsumerRecord<String, String> record) {
        String topic = record.topic();
        String payload = record.value();

        System.out.println("\n--------------------------------------------------");
        System.out.println(" E-Mail has been sent  (Topic: " + topic + ")");
        System.out.println("Order: " + record.key());

        if (payload.contains("DELIVERY_OUT_FOR_DELIVERY")) {
            System.out.println("Your item is out for delivery today!");
        } else if (payload.contains("ORDER_DELIVERED")) {
            System.out.println("Your order has been successfully delivered!");
        } else {
            System.out.println("Event processed: " + payload);
        }
        System.out.println("--------------------------------------------------\n");
    }}