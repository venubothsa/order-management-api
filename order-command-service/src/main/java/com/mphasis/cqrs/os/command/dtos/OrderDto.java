package com.mphasis.cqrs.os.command.dtos;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderDto {

    private String orderId;
    @NotBlank
    @NotNull
    private String customerId;
    private String createdDate;
    private String updatedDate;
    @Min(1)
    private double orderAmount;
    private String status;
    private String invoiceNumber;
    private String transactionId;
    private String createdBy;
    private String updatedBy;
    @NotNull
    private Product product;
    @NotNull
    private ShippingAddress shippingAddress;
    @NotNull
    private BillingAddress billingAddress;
}
