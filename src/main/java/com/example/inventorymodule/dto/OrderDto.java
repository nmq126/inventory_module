package com.example.inventorymodule.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {
    private Long orderId;
    private Long userId;
    private String orderStatus;
    private String paymentStatus;
    private String inventoryStatus;
    private BigDecimal totalPrice;
    private HashMap<Long, Integer> products;

    private String device_token;

}
