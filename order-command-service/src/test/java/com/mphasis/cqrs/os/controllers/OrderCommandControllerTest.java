package com.mphasis.cqrs.os.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.cqrs.os.command.commands.CreateOrderCommand;
import com.mphasis.cqrs.os.command.commands.UpdateOrderCommand;
import com.mphasis.cqrs.os.command.controller.OrderCommandController;
import com.mphasis.cqrs.os.command.dtos.OrderDto;
import com.mphasis.cqrs.os.command.exceptions.PreconditionFailed;
import com.mphasis.cqrs.os.command.services.OrderCommandService;
import com.mphasis.cqrs.os.utils.FileUtils;
import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class OrderCommandControllerTest {
    @MockBean
    public OrderCommandService orderCommandService;

    private OrderCommandController orderCommandController;

    private OrderDto orderDto;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        orderCommandController = new OrderCommandController(orderCommandService, dozerBeanMapper);
    }

    @Test
    public void createOrderCommandTest() throws PreconditionFailed, ExecutionException, InterruptedException, IOException {
        orderDto = objectMapper.readValue(FileUtils.readFileAsString(), OrderDto.class);
        assertNotNull(orderCommandService);
        assertNotNull(orderDto);
        when(orderCommandService.createOrder(any(CreateOrderCommand.class))).thenAnswer(ans -> {
            CreateOrderCommand createOrderCommand = ans.getArgument(0, CreateOrderCommand.class);
            CompletableFuture<String> response = new CompletableFuture<>();
            response.complete(createOrderCommand.getOrderId());
            return response;
        });
        ResponseEntity<?> order = orderCommandController.createOrder(orderDto);
        verify(orderCommandService, times(1)).createOrder(any(CreateOrderCommand.class));
        verifyNoMoreInteractions(orderCommandService);
        assertNotNull(order);
        //assertNotEquals(answer.get(), "");
    }

    @Test
    public void updateOrderCommand() throws PreconditionFailed, IOException, ExecutionException, InterruptedException {
        orderDto = objectMapper.readValue(FileUtils.readFileAsString(), OrderDto.class);
        assertNotNull(orderCommandService);
        assertNotNull(orderDto);
        orderDto.setOrderId(UUID.randomUUID().toString());
        when(orderCommandService.updateOrder(any(UpdateOrderCommand.class))).thenAnswer(ans -> {
            UpdateOrderCommand updateOrderCommand = ans.getArgument(0, UpdateOrderCommand.class);
            CompletableFuture<String> response = new CompletableFuture<>();
            response.complete(updateOrderCommand.getOrderId());
            return response;
        });
        ResponseEntity<?> responseEntity = orderCommandController.updateOrder(orderDto);
        verify(orderCommandService, times(1)).updateOrder(any(UpdateOrderCommand.class));
        verifyNoMoreInteractions(orderCommandService);
        assertNotNull(responseEntity);
        // assertEquals(orderDto.getOrderId(), answer.get());
    }


}
