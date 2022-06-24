package com.mphasis.cqrs.os.command.dtos;

import lombok.Data;

@Data
public class PaymentMessage {
    private String transactionId;
    private String orderId;
    private Double orderAmount;
    private String orderDate;
    private String status;
}
