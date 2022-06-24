package com.mphasis.cqrs.os.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.cqrs.os.command.entities.Order;
import com.mphasis.cqrs.os.command.events.OrderCreateEvent;
import com.mphasis.cqrs.os.command.events.OrderEventHandler;
import com.mphasis.cqrs.os.command.events.OrderUpdateEvent;
import com.mphasis.cqrs.os.command.repositories.OrderRepo;
import com.mphasis.cqrs.os.command.utils.OrderStatus;
import com.mphasis.cqrs.os.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class OrderEventHandlerTest {
    @Mock
    private OrderRepo orderRepo;
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;
    private String id;
    @Mock
    private OrderEventHandler orderEventHandler;
    private ObjectMapper objectMapper;
    private DozerBeanMapper dozerBeanMapper;

    @Before
    public void init() {
        objectMapper = new ObjectMapper();
        dozerBeanMapper = new DozerBeanMapper();
        id = UUID.randomUUID().toString();
        orderEventHandler = new OrderEventHandler(orderRepo, kafkaTemplate);
    }

    @Test
    public void testOrderCreateEventHandler() throws IOException {

        OrderCreateEvent orderCreateEvent = objectMapper.readValue(FileUtils.readFileAsString(), OrderCreateEvent.class);
        orderCreateEvent.setOrderId(id);
        Order order = dozerBeanMapper.map(orderCreateEvent, Order.class);
        when(orderRepo.save(any(Order.class))).thenReturn(order);
        orderEventHandler.on(orderCreateEvent);
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo, times(1)).save(orderArgumentCaptor.capture());
        verifyNoMoreInteractions(orderRepo);
        assertEquals(orderCreateEvent.getOrderId(), order.getOrderId());

    }

    @Test
    public void testOrderUpdateEventHandler() throws IOException {

        OrderUpdateEvent orderUpdateEvent = new OrderUpdateEvent();
        orderUpdateEvent.setOrderId(id);
        orderUpdateEvent.setStatus(OrderStatus.PLACED.name());
        orderUpdateEvent.setTransactionId(UUID.randomUUID().toString());
        Order order = dozerBeanMapper.map(orderUpdateEvent, Order.class);
        when(orderRepo.findByOrderId(order.getOrderId())).thenReturn(Optional.of(order));
        when(orderRepo.save(any(Order.class))).thenReturn(order);
        orderEventHandler.on(orderUpdateEvent);
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo,times(1)).findByOrderId(order.getOrderId());
        verify(orderRepo, times(1)).save(orderArgumentCaptor.capture());
        verifyNoMoreInteractions(orderRepo);
        assertEquals(orderUpdateEvent.getOrderId(), order.getOrderId());

    }

}
