package com.mphasis.cqrs.os.command.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.cqrs.os.command.dtos.OrderMessage;
import com.mphasis.cqrs.os.command.entities.Order;
import com.mphasis.cqrs.os.command.repositories.OrderRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.dozer.DozerBeanMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@ProcessingGroup("order-create")
@Slf4j
@RequiredArgsConstructor
public class OrderEventHandler {
    private final OrderRepo orderRepo;

    private final KafkaTemplate<String, Object> kafkaTemplate;




    @EventHandler
    public void on(OrderCreateEvent orderCreateEvent) throws JsonProcessingException {
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        log.debug("Handling {} event: {}", orderCreateEvent.getClass().getSimpleName(), orderCreateEvent.getOrderId());
        Order order = dozerBeanMapper.map(orderCreateEvent, Order.class);
        orderRepo.save(order);
        initiatePaymentEventOnKafka(order);
        log.trace("Done handling {} event: {}", orderCreateEvent.getClass().getSimpleName(), orderCreateEvent.getOrderId());

    }

    @EventHandler
    public void on(OrderUpdateEvent orderUpdateEvent) {
        log.debug("Handling {} event: {}", orderUpdateEvent.getClass().getSimpleName(), orderUpdateEvent.getOrderId());
        Optional<Order> optionalOrder = orderRepo.findByOrderId(orderUpdateEvent.getOrderId());
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(orderUpdateEvent.getStatus());
            order.setUpdatedDate(LocalDateTime.now().toString());
            orderRepo.save(order);
        }
        log.trace("Done handling {} event: {}", orderUpdateEvent.getClass().getSimpleName(), orderUpdateEvent.getOrderId());
    }

    @ExceptionHandler
    public void handle(Exception exception) throws Exception {
        throw exception;

    }

    private void initiatePaymentEventOnKafka(Order order) throws JsonProcessingException {
        String TOPIC = "new_payment_event";
        log.debug("Payment message update on Kafka TOPIC === {} for Order ID {} Order Status {}", TOPIC, order.getOrderId(), order.getStatus());
        log.info("Payment message update on Kafka TOPIC === " + TOPIC + " for Order ID " + order.getOrderId());
        OrderMessage paymentMessage = new OrderMessage();
        paymentMessage.setOrderId(order.getOrderId());
        paymentMessage.setOrderAmount(order.getOrderAmount());
        paymentMessage.setOrderDate(order.getCreatedDate());
        paymentMessage.setStatus(order.getStatus());
        ObjectMapper objectMapper = new ObjectMapper();
        kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(paymentMessage));
        log.trace("Payment message update on Kafka TOPIC === {} for Order ID {}", TOPIC, order.getOrderId());
    }
}
