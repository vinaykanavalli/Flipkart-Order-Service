package org.shop.flipkartorderservice.delivery.deliveryRepository;

import org.shop.flipkartorderservice.delivery.deliveryEntity.DeliveryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends CrudRepository<DeliveryEntity, Long> {
}