package com.example.inventorymodule.queue;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailEvent {

    private Long productId;
    private Long orderId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;

}