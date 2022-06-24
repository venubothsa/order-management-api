package com.mphasis.cqrs.os.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.cqrs.os.command.commands.CreateOrderCommand;
import com.mphasis.cqrs.os.command.commands.UpdateOrderCommand;
import com.mphasis.cqrs.os.command.services.OrderCommandService;
import com.mphasis.cqrs.os.command.utils.OrderStatus;
import com.mphasis.cqrs.os.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@Slf4j
public class OrderCommandServiceTest {


    private ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private CommandGateway commandGateway;

    private OrderCommandService orderCommandService;


    @Before
    public void init() {
        orderCommandService = new OrderCommandService(commandGateway);
    }

    @Test
    public void createOrderCommandTest() throws IOException, ExecutionException, InterruptedException {
        CreateOrderCommand createOrderCommand = objectMapper.readValue(FileUtils.readFileAsString(), CreateOrderCommand.class);
        createOrderCommand.setOrderId(UUID.randomUUID().toString());
        when(commandGateway.send(any())).thenAnswer(ans -> {
            CreateOrderCommand orderCommand = ans.getArgument(0, CreateOrderCommand.class);
            assertEquals(createOrderCommand.getOrderId(), orderCommand.getOrderId());
            CompletableFuture<String> response = new CompletableFuture<>();
            response.complete(orderCommand.getOrderId());
            return response;
        });

        CompletableFuture<String> order = orderCommandService.createOrder(createOrderCommand);
        verify(commandGateway, times(1)).send(any());
        verifyNoMoreInteractions(commandGateway);
        assertEquals(createOrderCommand.getOrderId(), order.get());

    }

    @Test
    public void updateOrderCommandTest() throws IOException, ExecutionException, InterruptedException {
        UpdateOrderCommand updateOrderCommand = new UpdateOrderCommand();
        updateOrderCommand.setOrderId(UUID.randomUUID().toString());
        updateOrderCommand.setStatus(OrderStatus.PLACED.name());
        updateOrderCommand.setTransactionId(UUID.randomUUID().toString());
        when(commandGateway.send(any())).thenAnswer(ans -> {
            UpdateOrderCommand orderCommand = ans.getArgument(0, UpdateOrderCommand.class);
            assertEquals(updateOrderCommand.getOrderId(), orderCommand.getOrderId());
            CompletableFuture<String> response = new CompletableFuture<>();
            response.complete(orderCommand.getOrderId());
            return response;
        });

        CompletableFuture<String> order = orderCommandService.updateOrder(updateOrderCommand);
        verify(commandGateway, times(1)).send(any());
        verifyNoMoreInteractions(commandGateway);
        assertEquals(updateOrderCommand.getOrderId(), order.get());

    }

}
