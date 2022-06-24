package com.mphasis.cqrs.os.command.controller;

import com.mphasis.cqrs.os.command.aspects.LogExecutionTime;
import com.mphasis.cqrs.os.command.commands.CreateOrderCommand;
import com.mphasis.cqrs.os.command.commands.UpdateOrderCommand;
import com.mphasis.cqrs.os.command.dtos.OrderDto;
import com.mphasis.cqrs.os.command.exceptions.PreconditionFailed;
import com.mphasis.cqrs.os.command.services.OrderCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/order")
@Slf4j
@RequiredArgsConstructor
public class OrderCommandController {
    private final OrderCommandService orderCommandService;
    private final DozerBeanMapper dozerBeanMapper;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @LogExecutionTime
    public CompletableFuture<String> createOrder(@Valid @RequestBody OrderDto orderDto) throws PreconditionFailed {
        if (Objects.nonNull(orderDto.getOrderId())) {
            throw new PreconditionFailed("Invalid payload....! ");
        }
        CreateOrderCommand createOrderCommand = dozerBeanMapper.map(orderDto, CreateOrderCommand.class);
        log.debug("Processing CreateOrderCommand: {}", createOrderCommand);
        createOrderCommand.setOrderId(UUID.randomUUID().toString());
        return orderCommandService.createOrder(createOrderCommand);
    }

    @PatchMapping
    @LogExecutionTime
    public CompletableFuture<String> updateOrder(@RequestBody OrderDto orderDto) throws PreconditionFailed {
        if (Objects.isNull(orderDto.getOrderId())) {
            throw new PreconditionFailed("Invalid payload....! ");
        }
        UpdateOrderCommand updateOrderCommand = dozerBeanMapper.map(orderDto, UpdateOrderCommand.class);
        return orderCommandService.updateOrder(updateOrderCommand);
    }


}
