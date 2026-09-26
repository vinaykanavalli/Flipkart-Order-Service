package org.shop.flipkartorderservice.order.orderService;


import tools.jackson.databind.ObjectMapper;
import org.shop.flipkartorderservice.order.orderEntity.OrderEntity;
import org.shop.flipkartorderservice.order.orderRepository.OrderRepository;
import org.shop.flipkartorderservice.order.orderRespnse.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    private Random random = new Random();

    public OrderResponse createAndPublishOrder(OrderEntity request) {
        long generatedOrderId = 1001 + random.nextInt(900);
        request.setOrderId(generatedOrderId);
        request.setStatus("CREATED");
        orderRepository.save(request);

            Map<String,Object> eventMap = new HashMap<>();
            eventMap.put("eventId", "EVT-" + System.currentTimeMillis());
            eventMap.put("eventType", "ORDER_CREATED");
            eventMap.put("orderId", generatedOrderId);
            eventMap.put("customerId", request.getCustomerId());
            eventMap.put("amount", request.getAmount());
            eventMap.put("deliveryAddress", request.getDeliveryAddress());

            String eventJson = objectMapper.writeValueAsString(eventMap);
            kafkaTemplate.send("order-created", String.valueOf(generatedOrderId), eventJson);

        return new OrderResponse(generatedOrderId, "CREATED");
    }
}