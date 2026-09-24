package org.shop.flipkartorderservice.notification.notificationRepository;

import org.shop.flipkartorderservice.notification.notificationEntity.NotificationEntity;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<NotificationEntity, Long> {
}
