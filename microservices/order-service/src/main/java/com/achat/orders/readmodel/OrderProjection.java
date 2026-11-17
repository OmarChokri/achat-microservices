package com.achat.orders.readmodel;

import com.achat.orders.events.OrderCancelledEvent;
import com.achat.orders.events.OrderCreatedEvent;
import com.achat.orders.queries.FindAllOrdersQuery;
import jakarta.persistence.EntityNotFoundException;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
public class OrderProjection {
    private final OrderRepository repository;

    public OrderProjection(OrderRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    @Transactional
    public void on(OrderCreatedEvent event) {
        repository.save(new OrderEntity(
                event.orderId(),
                event.productId(),
                event.quantity(),
                "CREATED",
                Instant.now()
        ));
    }

    @EventHandler
    @Transactional
    public void on(OrderCancelledEvent event) {
        OrderEntity order = repository.findById(event.orderId())
                .orElseThrow(() -> new EntityNotFoundException("Order projection not found: " + event.orderId()));
        order.setStatus("CANCELLED");
        repository.save(order);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public List<OrderView> handle(FindAllOrdersQuery query) {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(OrderView::from)
                .toList();
    }
}
