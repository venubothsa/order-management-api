package com.mphasis.cqrs.os.command.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateEvent {
    private String orderId;
    private String status;
    private String transactionId;
}
