package com.achat.orders.domain;

import com.achat.orders.commands.CancelOrderCommand;
import com.achat.orders.commands.CreateOrderCommand;
import com.achat.orders.events.OrderCancelledEvent;
import com.achat.orders.events.OrderCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class OrderAggregate {
    @AggregateIdentifier
    private String orderId;
    private String status;

    protected OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        if (command.orderId() == null || command.orderId().isBlank()) {
            throw new IllegalArgumentException("Order id is required");
        }
        if (command.productId() == null || command.productId().isBlank()) {
            throw new IllegalArgumentException("Product id is required");
        }
        if (command.quantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        AggregateLifecycle.apply(new OrderCreatedEvent(
                command.orderId(),
                command.productId(),
                command.quantity()
        ));
    }

    @CommandHandler
    public void handle(CancelOrderCommand command) {
        if (!"CREATED".equals(status)) {
            throw new IllegalStateException("Only a created order can be cancelled");
        }
        AggregateLifecycle.apply(new OrderCancelledEvent(command.orderId()));
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        orderId = event.orderId();
        status = "CREATED";
    }

    @EventSourcingHandler
    public void on(OrderCancelledEvent event) {
        status = "CANCELLED";
    }
}
