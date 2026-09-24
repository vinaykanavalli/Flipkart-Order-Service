package org.shop.flipkartorderservice.order.orderController;


import org.shop.flipkartorderservice.order.orderEntity.OrderEntity;
import org.shop.flipkartorderservice.order.orderRespnse.OrderResponse;
import org.shop.flipkartorderservice.order.orderService.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderEntity orderRequest) {
        return orderService.createAndPublishOrder(orderRequest);
    }
}