package com.mphasis.cqrs.os.command.services;

import com.mphasis.cqrs.os.command.commands.CreateOrderCommand;
import com.mphasis.cqrs.os.command.commands.UpdateOrderCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderCommandService {
    private final CommandGateway commandGateway;

    public CompletableFuture<String> createOrder(CreateOrderCommand createOrderCommand) {
        log.debug("Processing CreateOrderCommand: {}", createOrderCommand);
        createOrderCommand.setOrderId(UUID.randomUUID().toString());
        log.info("Create ==== Order Id ==== {} ", createOrderCommand.getOrderId());
        return commandGateway.send(createOrderCommand);

    }

    public CompletableFuture<String> updateOrder(UpdateOrderCommand updateOrderCommand) {
        log.debug("Processing UpdateOrderCommand: {}", updateOrderCommand);
        log.info("Update ==== Order Id ==== {} ", updateOrderCommand.getOrderId());
        return commandGateway.send(updateOrderCommand);
    }
}
