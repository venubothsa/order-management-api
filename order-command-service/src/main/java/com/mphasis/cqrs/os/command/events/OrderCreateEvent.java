package com.mphasis.cqrs.os.command.events;

import com.mphasis.cqrs.os.command.dtos.BillingAddress;
import com.mphasis.cqrs.os.command.dtos.Product;
import com.mphasis.cqrs.os.command.dtos.ShippingAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateEvent {
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
    private String transactionId;
    private ShippingAddress shippingAddress;
    private BillingAddress billingAddress;

}
