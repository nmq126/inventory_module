package com.example.inventorymodule.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDto {

    private Long orderId;
    private Long userId;
    private BigDecimal totalPrice;
    private String PaymentStatus;
    private String message;
    private String device_token;
}
