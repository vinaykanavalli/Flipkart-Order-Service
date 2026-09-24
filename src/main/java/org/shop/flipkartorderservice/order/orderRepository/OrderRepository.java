package org.shop.flipkartorderservice.order.orderRepository;


import org.shop.flipkartorderservice.order.orderEntity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
}