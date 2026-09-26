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

        if (paymentRepository.findByOrderId(orderId) != null) {
            System.out.println("Payment already processed for Order ID: " + orderId);
            return ;
        }

        Map<String, Object> userInput = objectMapper.readValue(record.value(), Map.class);//map.class will get raw json to java obj
        Long customerId = Long.valueOf(userInput.get("customerId").toString());
        double amount = Double.parseDouble(userInput.get("amount").toString());

        int checkRoll = random.nextInt(10);
        if (checkRoll == 0) throw new RuntimeException("Payment Gateway Simulation Error!");
        PaymentEntity payment = new PaymentEntity();
        payment.setOrderId(orderId);
        payment.setAmount(amount);

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("customerId", customerId);
        map.put("amount", amount);

        if (checkRoll > 1) {
                payment.setPaymentStatus("SUCCESS");
                paymentRepository.save(payment);
                map.put("eventType", "PAYMENT SUCCESS");
                map.put("paymentStatus", "SUCCESS");
                kafkaTemplate.send("payment-success", orderKey, objectMapper.writeValueAsString(map));
            } else {
                payment.setPaymentStatus("FAILED");
                paymentRepository.save(payment);
                map.put("eventType", "PAYMENT FAILED");
                map.put("paymentStatus", "FAILED");
                kafkaTemplate.send("payment-failed", orderKey, objectMapper.writeValueAsString(map));
            }

    }
    @DltHandler
    public void handleDlt(ConsumerRecord<String, String> record) {
        System.err.println("Payment DLT reached for: " + record.value() + " due to ");
    }
}