package org.shop.flipkartorderservice.delivery.deliveryController;


import org.shop.flipkartorderservice.delivery.deliveryEntity.DeliveryEntity;
import org.shop.flipkartorderservice.delivery.deliveryRepository.DeliveryRepository;
import org.shop.flipkartorderservice.delivery.deliveryResponse.DeliveryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {
    @Autowired
    private DeliveryRepository deliveryRepository;

    @GetMapping("/{orderId}")
    public DeliveryResponse getDeliveryStatus(@PathVariable Long orderId) {
        DeliveryEntity d = deliveryRepository.findByOrderId(orderId);
        return new DeliveryResponse(d.getOrderId(), d.getTrackingNumber(), d.getDeliveryStatus());
    }
}