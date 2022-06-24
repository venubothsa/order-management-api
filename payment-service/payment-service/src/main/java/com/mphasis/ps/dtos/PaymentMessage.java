package com.mphasis.ps.dtos;

import lombok.Data;

@Data
public class PaymentMessage {
    private String orderId;
    private Double orderAmount;
    private String orderDate;
    private String status;
    private String transactionId;
}
