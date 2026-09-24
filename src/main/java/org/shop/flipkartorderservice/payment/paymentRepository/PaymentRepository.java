package org.shop.flipkartorderservice.payment.paymentRepository;

import org.shop.flipkartorderservice.payment.paymentEntity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
    PaymentEntity findByOrderId(Long orderId);
}