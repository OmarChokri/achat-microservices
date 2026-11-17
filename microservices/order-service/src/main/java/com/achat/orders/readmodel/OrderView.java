package com.achat.orders.readmodel;

import java.time.Instant;

public record OrderView(
        String orderId,
        String productId,
        int quantity,
        String status,
        Instant createdAt
) {
    public static OrderView from(OrderEntity entity) {
        return new OrderView(
                entity.getOrderId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
