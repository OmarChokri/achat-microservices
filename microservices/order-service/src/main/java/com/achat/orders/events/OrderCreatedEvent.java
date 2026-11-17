package com.achat.orders.events;

public record OrderCreatedEvent(String orderId, String productId, int quantity) {
}
