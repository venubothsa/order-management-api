package com.mphasis.cqrs.os.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.cqrs.os.query.dtos.OrderResult;
import com.mphasis.cqrs.os.query.queries.GetOrderQuery;
import com.mphasis.cqrs.os.query.services.OrderQueryService;
import com.mphasis.cqrs.os.query.utils.SortOrder;
import com.mphasis.cqrs.os.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@Slf4j
public class OrderQueryServiceTest {
    private OrderQueryService orderQueryService;
    @MockBean
    private QueryGateway queryGateway;

    @Before
    public void init() {
        orderQueryService = new OrderQueryService(queryGateway);
    }

    @Test
    public void retrieveAllOrdersTest() throws IOException, ExecutionException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderResult orderResult = objectMapper.readValue(FileUtils.readFileAsString("src/test/resources/response.json"), OrderResult.class);
        Integer months = 6;
        Integer page = 1;
        Integer size = 10;
        SortOrder sortOrder = SortOrder.DESC;
        GetOrderQuery getOrderQuery = GetOrderQuery.builder().size(size).sortOrder(sortOrder).page(page).months(months).build();
        CompletableFuture<OrderResult> completableFuture = new CompletableFuture<>();
        completableFuture.complete(orderResult);
        when(queryGateway.query(getOrderQuery, ResponseTypes.instanceOf(OrderResult.class))).thenReturn(completableFuture);
        CompletableFuture<OrderResult> answer = orderQueryService.retrieveAllOrders(months, page, size, sortOrder);
        verify(queryGateway, times(1)).query(getOrderQuery, ResponseTypes.instanceOf(OrderResult.class));
        verifyNoMoreInteractions(queryGateway);
        assertNotNull(answer.get());
    }
}
