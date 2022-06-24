package com.mphasis.cqrs.os.command.dtos;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BillingAddress {
    @NotNull
    @NotBlank
    private String billingFirstName;
    @NotNull
    @NotBlank
    private String billingLastName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    @Min(100000)
    private long zipCode;
    @NotBlank
    @NotNull
    private String country;
}
