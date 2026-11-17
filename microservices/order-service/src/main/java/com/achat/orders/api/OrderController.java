package com.achat.orders.api;

import com.achat.orders.commands.CancelOrderCommand;
import com.achat.orders.commands.CreateOrderCommand;
import com.achat.orders.queries.FindAllOrdersQuery;
import com.achat.orders.readmodel.OrderView;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public OrderController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<CreateOrderResponse>> create(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        String orderId = UUID.randomUUID().toString();
        CreateOrderCommand command = new CreateOrderCommand(
                orderId,
                request.productId(),
                request.quantity()
        );

        return commandGateway.<Object>send(command)
                .thenApply(ignored -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(new CreateOrderResponse(orderId, "CREATED")));
    }

    @PostMapping("/{orderId}/cancel")
    public CompletableFuture<ResponseEntity<Void>> cancel(@PathVariable String orderId) {
        return commandGateway.<Object>send(new CancelOrderCommand(orderId))
                .thenApply(ignored -> ResponseEntity.noContent().build());
    }

    @GetMapping
    public CompletableFuture<List<OrderView>> findAll() {
        return queryGateway.query(
                new FindAllOrdersQuery(),
                ResponseTypes.multipleInstancesOf(OrderView.class)
        );
    }
}
