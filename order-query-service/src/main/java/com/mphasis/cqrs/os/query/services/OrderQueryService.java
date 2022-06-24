package com.mphasis.cqrs.os.query.services;

import com.mphasis.cqrs.os.query.dtos.OrderDto;
import com.mphasis.cqrs.os.query.dtos.OrderResult;
import com.mphasis.cqrs.os.query.queries.GetOrderQuery;
import com.mphasis.cqrs.os.query.utils.SortOrder;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class OrderQueryService {


    @Autowired
    private QueryGateway queryGateway;

    public CompletableFuture<OrderResult> retrieveAllOrders(Integer months, Integer page, Integer size, SortOrder sortOrder) {
        GetOrderQuery getOrderQuery = GetOrderQuery.builder().months(months).page(page).size(size).sortOrder(sortOrder).build();
        return queryGateway.query(getOrderQuery, ResponseTypes.instanceOf(OrderResult.class));
    }
    public CompletableFuture<Optional<OrderDto>> getOrderById(String orderId){
        return queryGateway.query(orderId, ResponseTypes.optionalInstanceOf(OrderDto.class));
    }
}
