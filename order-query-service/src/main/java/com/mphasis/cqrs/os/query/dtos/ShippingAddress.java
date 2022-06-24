package com.mphasis.cqrs.os.query.dtos;

import lombok.Data;

@Data
public class ShippingAddress {

    private String shippingFirstName;
    private String shippingLastName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private long zipCode;
    private String country;


}
