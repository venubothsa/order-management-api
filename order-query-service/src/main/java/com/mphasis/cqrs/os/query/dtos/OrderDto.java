package com.mphasis.cqrs.os.query.dtos;

import lombok.Data;

@Data
public class OrderDto {
    private String orderId;
    private String customerId;
    private String createdDate;
    private String updatedDate;
    private double orderAmount;
    private String status;
    private String invoiceNumber;
    private String createdBy;
    private String updatedBy;
    private Product product;
    private ShippingAddress shippingAddress;
    private BillingAddress billingAddress;
    private String transactionId;
}
