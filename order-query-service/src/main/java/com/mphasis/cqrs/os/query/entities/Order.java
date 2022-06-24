package com.mphasis.cqrs.os.query.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.mphasis.cqrs.os.query.dtos.BillingAddress;
import com.mphasis.cqrs.os.query.dtos.Product;
import com.mphasis.cqrs.os.query.dtos.ShippingAddress;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//Mongo database annotation.
@Document(collection = "orders")
@Data
public class Order {
    @Id
    @JsonProperty("orderId")
    private String orderId;
    private String customerId;
    private String createdDate;
    private String updatedDate;
    private double orderAmount;
    private String status;
    private String transactionId;
    private String invoiceNumber;
    private String createdBy;
    private String updatedBy;
    private Product product;
    private ShippingAddress shippingAddress;
    private BillingAddress billingAddress;


}
