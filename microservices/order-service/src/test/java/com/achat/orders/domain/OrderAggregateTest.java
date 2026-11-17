package com.achat.orders.domain;

import com.achat.orders.commands.CancelOrderCommand;
import com.achat.orders.commands.CreateOrderCommand;
import com.achat.orders.events.OrderCancelledEvent;
import com.achat.orders.events.OrderCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderAggregateTest {
    private AggregateTestFixture<OrderAggregate> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(OrderAggregate.class);
    }

    @Test
    void createsAnOrder() {
        fixture.givenNoPriorActivity()
                .when(new CreateOrderCommand("order-1", "product-1", 2))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new OrderCreatedEvent("order-1", "product-1", 2));
    }

    @Test
    void cancelsAnExistingOrder() {
        fixture.given(new OrderCreatedEvent("order-1", "product-1", 2))
                .when(new CancelOrderCommand("order-1"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new OrderCancelledEvent("order-1"));
    }

    @Test
    void rejectsOrdersWithNonPositiveQuantity() {
        fixture.givenNoPriorActivity()
                .when(new CreateOrderCommand("order-1", "product-1", 0))
                .expectException(IllegalArgumentException.class);
    }
}
