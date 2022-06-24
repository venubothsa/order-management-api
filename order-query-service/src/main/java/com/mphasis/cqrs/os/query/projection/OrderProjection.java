package com.mphasis.cqrs.os.query.projection;

import com.mphasis.cqrs.os.query.dtos.OrderDto;
import com.mphasis.cqrs.os.query.dtos.OrderResult;
import com.mphasis.cqrs.os.query.entities.Order;
import com.mphasis.cqrs.os.query.queries.GetOrderQuery;
import com.mphasis.cqrs.os.query.repositories.OrderRepo;
import com.mphasis.cqrs.os.query.utils.SortOrder;
import org.axonframework.queryhandling.QueryHandler;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderProjection {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @QueryHandler
    public OrderResult handle(GetOrderQuery getOrderQuery) {
        Sort.Direction sort = Sort.Direction.DESC;
        if (SortOrder.ASC.equals(getOrderQuery.getSortOrder())) {
            sort = Sort.Direction.ASC;
        }
        if (Objects.isNull(getOrderQuery.getPage())) {
            getOrderQuery.setPage(1);
        }
        if (Objects.isNull(getOrderQuery.getSize())) {
            getOrderQuery.setSize(10);
        }

        Pageable paging = PageRequest.of(getOrderQuery.getPage() - 1, getOrderQuery.getSize(), Sort.by(sort, "createdDate"));
        long count = orderRepo.count();
        Page<Order> orderPage = orderRepo.findAll(paging);
        OrderResult orderResult = new OrderResult();
        orderResult.setPage(paging.getPageNumber());
        orderResult.setSize(paging.getPageSize());
        orderResult.setTotal(count);
        orderResult.setOrderDtos(orderPage.stream().map(order -> dozerBeanMapper.map(order, OrderDto.class)).collect(Collectors.toList()));
        return orderResult;

    }

    @QueryHandler
    public Optional<OrderDto> handle(String orderId) {
        Optional<Order> optionalOrder = orderRepo.findByOrderId(orderId);
        OrderDto orderDto = null;
        if (optionalOrder.isPresent()) {
            orderDto = dozerBeanMapper.map(optionalOrder.get(), OrderDto.class);
        }
        return Optional.ofNullable(orderDto);
    }
}
