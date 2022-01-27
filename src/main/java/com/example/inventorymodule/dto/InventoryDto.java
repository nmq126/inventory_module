package com.example.inventorymodule.dto;

import lombok.*;

import javax.persistence.criteria.Order;
import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InventoryDto {

    private Long orderId;
    private String orderStatus;
    private String inventoryStatus;
    private HashMap<Long, Integer> products;
    private String message;
    private String device_token;

    public InventoryDto(OrderDto orderDto){
        this.orderId = orderDto.getOrderId();
        this.orderStatus = orderDto.getOrderStatus();
        this.inventoryStatus = orderDto.getInventoryStatus();
        this.products = orderDto.getProducts();
        this.device_token = orderDto.getDevice_token();
    }

}
