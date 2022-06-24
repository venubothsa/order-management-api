package com.mphasis.cqrs.os.query.controllers;

import com.mphasis.cqrs.os.query.aspects.LogExecutionTime;
import com.mphasis.cqrs.os.query.dtos.OrderDto;
import com.mphasis.cqrs.os.query.dtos.OrderResult;
import com.mphasis.cqrs.os.query.services.OrderQueryService;
import com.mphasis.cqrs.os.query.utils.SortOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderQueryController {
    private final OrderQueryService orderQueryService;

    @LogExecutionTime
    @GetMapping
    public CompletableFuture<OrderResult> retrieveAllOrders(@RequestParam(required = false, name = "months") Integer months, @RequestParam(required = false, name = "page") Integer page, @RequestParam(required = false, name = "size") Integer size, @RequestParam(required = false, name = "sortOrder") SortOrder sortOrder) {
        return orderQueryService.retrieveAllOrders(months, page, size, sortOrder);
    }

    @GetMapping("/{orderId}")
    @LogExecutionTime
    public ResponseEntity<?> getOrderById(@PathVariable("orderId") String orderId) throws ExecutionException, InterruptedException {
        CompletableFuture<Optional<OrderDto>> byId = orderQueryService.getOrderById(orderId);
        if (byId.get().isPresent()) {
            return ResponseEntity.ok(byId.get().get());
        }
        return ResponseEntity.ok("Order not found...!");
    }
}
