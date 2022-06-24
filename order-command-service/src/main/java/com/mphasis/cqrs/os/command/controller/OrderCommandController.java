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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDto orderDto) throws PreconditionFailed, ExecutionException, InterruptedException {
        if (Objects.nonNull(orderDto.getOrderId())) {
            throw new PreconditionFailed("Invalid payload....! ");
        }
        CreateOrderCommand createOrderCommand = dozerBeanMapper.map(orderDto, CreateOrderCommand.class);
        log.debug("Processing CreateOrderCommand: {}", createOrderCommand);
        createOrderCommand.setOrderId(UUID.randomUUID().toString());
        CompletableFuture<String> order = orderCommandService.createOrder(createOrderCommand);
        Map<String, String> map = new HashMap<>();
        if (Objects.nonNull(order)) {
            map.put("orderId", order.get());
            map.put("message", "Order created successfully...!");
            return ResponseEntity.ok(map);
        } else
            map.put("message", "Order is not created...!");
        return ResponseEntity.internalServerError().body(map);
    }

    @PatchMapping
    @LogExecutionTime
    public ResponseEntity<?> updateOrder(@RequestBody OrderDto orderDto) throws PreconditionFailed, ExecutionException, InterruptedException {
        if (Objects.isNull(orderDto.getOrderId())) {
            throw new PreconditionFailed("Invalid payload....! ");
        }
        UpdateOrderCommand updateOrderCommand = dozerBeanMapper.map(orderDto, UpdateOrderCommand.class);
        CompletableFuture<String> order = orderCommandService.updateOrder(updateOrderCommand);
        Map<String, String> map = new HashMap<>();
        if (Objects.nonNull(order)) {
            map.put("orderId", order.get());
            map.put("message", "Order created successfully...!");
            return ResponseEntity.ok(map);
        } else
            map.put("message", "Order is not created...!");
        return ResponseEntity.internalServerError().body(map);
    }


}
