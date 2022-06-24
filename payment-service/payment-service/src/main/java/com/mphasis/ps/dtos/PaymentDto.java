package com.mphasis.ps.dtos;

import lombok.Data;

@Data
public class PaymentDto {
    private String orderId;
    private Double amount;
    private String orderDate;
    private String createdDate;
    private String transactionId;
}
