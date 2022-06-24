package com.mphasis.cqrs.os.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.cqrs.os.query.controllers.OrderQueryController;
import com.mphasis.cqrs.os.query.dtos.OrderDto;
import com.mphasis.cqrs.os.query.dtos.OrderResult;
import com.mphasis.cqrs.os.query.services.OrderQueryService;
import com.mphasis.cqrs.os.query.utils.SortOrder;
import com.mphasis.cqrs.os.utils.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class OrderQueryControllerTest {
    @MockBean
    public OrderQueryService orderQueryService;

    private OrderQueryController orderQueryController;

    @Before
    public void init() {
        orderQueryController = new OrderQueryController(orderQueryService);
    }

    @Test
    public void retrieveAllOrdersTest() throws ExecutionException, InterruptedException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Integer months = 6;
        Integer page = 1;
        Integer size = 10;
        SortOrder sortOrder = SortOrder.DESC;

        OrderResult orderResult = objectMapper.readValue(FileUtils.readFileAsString("src/test/resources/response.json"), OrderResult.class);
        CompletableFuture<OrderResult> completableFuture = new CompletableFuture<>();
        completableFuture.complete(orderResult);
        when(orderQueryService.retrieveAllOrders(any(Integer.class), any(Integer.class), any(Integer.class), any(SortOrder.class)))
                .thenReturn(completableFuture);
        CompletableFuture<OrderResult> answer = orderQueryController.retrieveAllOrders(months, page, size, sortOrder);
        verify(orderQueryService, times(1)).retrieveAllOrders(any(Integer.class), any(Integer.class), any(Integer.class), any(SortOrder.class));
        verifyNoMoreInteractions(orderQueryService);
        assertNotNull(answer.get());
    }

    @Test
    public void getOrderByIdTest() throws ExecutionException, InterruptedException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        String orderId = "d6fca9af-bf52-467d-9fae-bdd70f7bea87";

        OrderDto orderResult = objectMapper.readValue(FileUtils.readFileAsString("src/test/resources/single_order.json"), OrderDto.class);
        CompletableFuture<Optional<OrderDto>> completableFuture = new CompletableFuture<>();
        completableFuture.complete(Optional.of(orderResult));
        when(orderQueryService.getOrderById(any(String.class))).thenReturn(completableFuture);
        ResponseEntity<?> answer = orderQueryController.getOrderById(orderId);
        verify(orderQueryService, times(1)).getOrderById(any(String.class));
        verifyNoMoreInteractions(orderQueryService);
        assertNotNull(answer);
        assertEquals(answer.getStatusCode(), HttpStatus.OK);
    }

}
