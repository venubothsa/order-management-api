package com.mphasis.cqrs.os.query.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderResult {
    @JsonProperty("orders")
    private List<OrderDto> orderDtos;
    private Integer page;
    private Integer size;
    private Long total;
}
