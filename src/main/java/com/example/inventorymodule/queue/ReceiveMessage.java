package com.example.inventorymodule.queue;


import com.example.inventorymodule.dto.InventoryDto;
import com.example.inventorymodule.dto.OrderDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.inventorymodule.queue.Config.QUEUE_INVENTORY;


@Log4j2
@Component
public class ReceiveMessage {

    @Autowired
    private ConsumerService consumerService;

    @RabbitListener(queues = {QUEUE_INVENTORY})
    public void getInfoOrder(OrderEvent orderEvent) {
        log.info("Module Inventory nhận thông tin order: " + orderEvent);
        consumerService.handleOrder(orderEvent);
    }

}
