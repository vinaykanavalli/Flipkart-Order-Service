package org.shop.flipkartorderservice.payment.paymentService;

import org.shop.flipkartorderservice.payment.paymentEntity.PaymentEntity;
import org.shop.flipkartorderservice.payment.paymentRepository.PaymentRepository;
import org.springframework.kafka.annotation.BackOff;
import tools.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    ObjectMapper objectMapper;
    private Random random = new Random();

    @RetryableTopic(attempts = "3", backOff = @BackOff(delay = 2000L))
    @KafkaListener(topics = "order-created", groupId = "payment-group")
    public void processPayment(ConsumerRecord<String, String> record) {
        String orderKey = record.key();
        Long orderId = Long.valueOf(orderKey);

        // Simple Idempotency Check using the main Payment table
        if (paymentRepository.findByOrderId(orderId) != null) {
            System.out.println("Payment already processed for Order ID: " + orderId);
            return;
        }

        int checkRoll = random.nextInt(10);
        if (checkRoll == 0) throw new RuntimeException("Payment Gateway Simulation Error!");

        boolean isSuccess = (checkRoll > 1);
        PaymentEntity payment = new PaymentEntity();
        payment.setOrderId(orderId);
        payment.setAmount(75000);

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("customerId", 101);
            map.put("amount", 75000);

            if (isSuccess) {
                payment.setPaymentStatus("SUCCESS");
                paymentRepository.save(payment);
                map.put("eventType", "PAYMENT_SUCCESS");
                map.put("paymentStatus", "SUCCESS");
                kafkaTemplate.send("payment-success", orderKey, objectMapper.writeValueAsString(map));
            } else {
                payment.setPaymentStatus("FAILED");
                paymentRepository.save(payment);
                map.put("eventType", "PAYMENT_FAILED");
                map.put("paymentStatus", "FAILED");
                kafkaTemplate.send("payment-failed", orderKey, objectMapper.writeValueAsString(map));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, String> record, @Header("kafka_dlt_exception_message") String error) {
        System.err.println("Payment DLT reached for: " + record.value() + " due to " + error);
    }
}