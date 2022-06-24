package com.mphasis.cqrs.os.command.commands;

import com.mphasis.cqrs.os.command.dtos.BillingAddress;
import com.mphasis.cqrs.os.command.dtos.Product;
import com.mphasis.cqrs.os.command.dtos.ShippingAddress;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
public class CreateOrderCommand {
    @TargetAggregateIdentifier
    private String orderId;
    private String customerId;
    private String createdDate;
    private String updatedDate;
    private Double orderAmount;
    private String status;
    private String invoiceNumber;
    private String transactionId;
    private String createdBy;
    private String updatedBy;
    private Product product;
    private ShippingAddress shippingAddress;
    private BillingAddress billingAddress;
}
