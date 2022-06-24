package com.mphasis.cqrs.os.aggregators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.cqrs.os.command.aggregaters.OrderAggregate;
import com.mphasis.cqrs.os.command.commands.CreateOrderCommand;
import com.mphasis.cqrs.os.command.commands.UpdateOrderCommand;
import com.mphasis.cqrs.os.command.events.OrderCreateEvent;
import com.mphasis.cqrs.os.command.events.OrderUpdateEvent;
import com.mphasis.cqrs.os.command.utils.OrderStatus;
import com.mphasis.cqrs.os.utils.FileUtils;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

public class OrderAggregateTest {
    private FixtureConfiguration<OrderAggregate> fixture;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String id;


    @Before
    public void init() {
        fixture = new AggregateTestFixture<>(OrderAggregate.class);

        id = UUID.randomUUID().toString();

    }

   /* @Test
    public void testCreateOrderCommandPass() throws IOException {
        CreateOrderCommand createOrderCommand = objectMapper.readValue(FileUtils.readFileAsString(), CreateOrderCommand.class);
        createOrderCommand.setOrderId(id);
        OrderCreateEvent orderCreateEvent = objectMapper.readValue(FileUtils.readFileAsString(), OrderCreateEvent.class);
        orderCreateEvent.setOrderId(id);
        fixture.given().when(createOrderCommand).expectSuccessfulHandlerExecution().expectEvents(orderCreateEvent);

    }*/

   /* @Test
    public void testUpdateOrderCommandPass() throws IOException {
        UpdateOrderCommand updateOrderCommand = new UpdateOrderCommand();
        updateOrderCommand.setOrderId(id);
        updateOrderCommand.setStatus(OrderStatus.PLACED.name());
        updateOrderCommand.setTransactionId(UUID.randomUUID().toString());

        OrderUpdateEvent orderUpdateEvent = new OrderUpdateEvent();
        orderUpdateEvent.setOrderId(id);
        orderUpdateEvent.setStatus(updateOrderCommand.getStatus());
        orderUpdateEvent.setTransactionId(updateOrderCommand.getTransactionId());
        fixture.given().when(updateOrderCommand).expectSuccessfulHandlerExecution().expectEvents(orderUpdateEvent);
    }*/

  /*  @Test
    public void testCreateOrderCommandFail() throws IOException {
        CreateOrderCommand createOrderCommand = objectMapper.readValue(FileUtils.readFileAsString(), CreateOrderCommand.class);
        createOrderCommand.setOrderId(null);
        fixture.given().when(createOrderCommand).expectException(IllegalArgumentException.class).;
        createOrderCommand.setOrderId("");
        fixture.given().when(createOrderCommand).expectException(IllegalArgumentException.class);

    }

    @Test
    public void testUpdateOrderCommandFail() {
        UpdateOrderCommand updateOrderCommand = new UpdateOrderCommand();
        updateOrderCommand.setOrderId(null);
        fixture.given().when(updateOrderCommand).expectSuccessfulHandlerExecution().expectException(IllegalArgumentException.class);
        updateOrderCommand.setOrderId("");
        fixture.given().when(updateOrderCommand).expectException(IllegalArgumentException.class);

    }
*/
}
