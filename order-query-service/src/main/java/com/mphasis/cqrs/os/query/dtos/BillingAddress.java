package com.mphasis.cqrs.os.query.dtos;

import lombok.Data;

@Data
public class BillingAddress {
    private String billingFirstName;
    private String billingLastName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private long zipCode;
    private String country;

}
