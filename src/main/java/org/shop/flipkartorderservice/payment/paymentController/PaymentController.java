package org.shop.flipkartorderservice.payment.paymentController;

import org.shop.flipkartorderservice.payment.paymentEntity.PaymentEntity;
import org.shop.flipkartorderservice.payment.paymentRepository.PaymentRepository;
import org.shop.flipkartorderservice.payment.paymentResponse.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/{orderId}")
    public PaymentResponse getPaymentStatus(@PathVariable Long orderId) {
        PaymentEntity payment = paymentRepository.findAll().iterator().next(); // simplified demo finder
        return new PaymentResponse(payment.getOrderId(), payment.getPaymentStatus());
    }
}