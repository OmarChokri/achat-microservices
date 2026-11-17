package com.achat.orders.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CreateOrderCommand(
        @TargetAggregateIdentifier String orderId,
        String productId,
        int quantity
) {
}
