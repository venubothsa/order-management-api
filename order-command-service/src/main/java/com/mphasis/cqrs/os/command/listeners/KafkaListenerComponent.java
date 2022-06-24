package com.mphasis.cqrs.os.command.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.cqrs.os.command.commands.UpdateOrderCommand;
import com.mphasis.cqrs.os.command.dtos.PaymentMessage;
import com.mphasis.cqrs.os.command.utils.OrderStatus;
import com.mphasis.cqrs.os.command.utils.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListenerComponent {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private CommandGateway commandGateway;


    @KafkaListener(groupId = "payment-events-1", topics = "payment_status_events", containerFactory = "kafkaListenerContainerFactory")
    public void listenFromOrderTopic(String msg) throws JsonProcessingException, InterruptedException {
        executeUpdateCommand(msg);
    }

    private void executeUpdateCommand(String msg) throws JsonProcessingException, InterruptedException {
        PaymentMessage paymentMessage = objectMapper.readValue(msg, PaymentMessage.class);
        log.info("Updating order status ==== " + paymentMessage.getOrderId());
        UpdateOrderCommand updateOrderCommand = new UpdateOrderCommand();
        updateOrderCommand.setOrderId(paymentMessage.getOrderId());
        updateOrderCommand.setTransactionId(paymentMessage.getTransactionId());
        if (PaymentStatus.PAYMENT_SUCCESS.name().equals(paymentMessage.getStatus())) {
            updateOrderCommand.setStatus(OrderStatus.PLACED.name());
        } else {
            updateOrderCommand.setStatus(OrderStatus.CANCELLED.name());
        }
        commandGateway.sendAndWait(updateOrderCommand);

    }


}
