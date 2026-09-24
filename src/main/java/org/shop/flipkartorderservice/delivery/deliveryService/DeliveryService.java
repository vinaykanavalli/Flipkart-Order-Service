package org.shop.flipkartorderservice.delivery.deliveryService;

import tools.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.shop.flipkartorderservice.delivery.deliveryEntity.DeliveryEntity;
import org.shop.flipkartorderservice.delivery.deliveryRepository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
@Service
public class DeliveryService {
    @Autowired private DeliveryRepository deliveryRepository;
    @Autowired private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired private ObjectMapper objectMapper;

    @KafkaListener(topics = "payment-success", groupId = "delivery-group")
    public void consumePaymentSuccess(ConsumerRecord<String, String> record) {
        String orderKey = record.key();
        String trackingNo = "TRK-" + (50000 + (int)(Math.random() * 5000));

        DeliveryEntity delivery = new DeliveryEntity();
        delivery.setOrderId(Long.valueOf(orderKey));
        delivery.setTrackingNumber(trackingNo);
        delivery.setDeliveryStatus("CREATED");
        deliveryRepository.save(delivery);

        try {
            // 1. Publish DELIVERY_CREATED
            Map<String, Object> createdMap = new HashMap<>();
            createdMap.put("eventId", "DEL-" + System.currentTimeMillis());
            createdMap.put("eventType", "DELIVERY_CREATED");
            createdMap.put("orderId", Long.valueOf(orderKey));
            createdMap.put("trackingNumber", trackingNo);
            createdMap.put("deliveryStatus", "CREATED");
            kafkaTemplate.send("delivery-created", orderKey, objectMapper.writeValueAsString(createdMap));

            // 2. Simulate OUT_FOR_DELIVERY milestone
            Thread.sleep(5000);
            delivery.setDeliveryStatus("OUT_FOR_DELIVERY");
            deliveryRepository.save(delivery);

            Map<String, Object> outMap = new HashMap<>();
            outMap.put("eventId", "DEL-F-" + System.currentTimeMillis());
            outMap.put("eventType", "DELIVERY_OUT_FOR_DELIVERY");
            outMap.put("orderId", Long.valueOf(orderKey));
            outMap.put("trackingNumber", trackingNo);
            outMap.put("deliveryStatus", "OUT_FOR_DELIVERY");
            kafkaTemplate.send("delivery-events", orderKey, objectMapper.writeValueAsString(outMap));

            // 3. Simulate DELIVERED milestone
            Thread.sleep(5000);
            delivery.setDeliveryStatus("DELIVERED");
            deliveryRepository.save(delivery);

            Map<String, Object> deliveredMap = new HashMap<>();
            deliveredMap.put("eventId", "DEL-D-" + System.currentTimeMillis());
            deliveredMap.put("eventType", "ORDER_DELIVERED");
            deliveredMap.put("orderId", Long.valueOf(orderKey));
            deliveredMap.put("trackingNumber", trackingNo);
            deliveredMap.put("deliveryStatus", "DELIVERED");
            kafkaTemplate.send("delivery-events", orderKey, objectMapper.writeValueAsString(deliveredMap));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}