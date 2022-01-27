package com.example.inventorymodule.queue;

import com.example.inventorymodule.entity.ExportHistory;
import com.example.inventorymodule.entity.ImportHistory;
import com.example.inventorymodule.entity.Product;
import com.example.inventorymodule.enums.Status;
import com.example.inventorymodule.repository.ExportHistoryRepository;
import com.example.inventorymodule.repository.ImportHistoryRepository;
import com.example.inventorymodule.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static com.example.inventorymodule.queue.Config.*;

@Log4j2
@Service
public class ConsumerService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ExportHistoryRepository exportHistoryRepository;

    @Autowired
    private ImportHistoryRepository importHistoryRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    public void handleOrder(OrderEvent orderEvent) {
        orderEvent.setQueueName(QUEUE_INVENTORY);
        //Check valid inventory
        if (!orderEvent.validateInventory()) {
            orderEvent.setMessage("Information missing");
            orderEvent.setInventoryStatus(Status.InventoryStatus.PENDING.name());
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderEvent);
            return;
        }
        //check status inventory
        if (orderEvent.getInventoryStatus().equals(Status.InventoryStatus.RETURN.name())) {
            handleReturn(orderEvent);
            return;
        }

        Set<ExportHistory> exportHistorySet = new HashSet<>();
        Set<Product> productSet = new HashSet<>();
        for (OrderDetailEvent orderDetail : orderEvent.getOrderDetailEvents()) {
            Long id = orderDetail.getProductId();
            int quantity = orderDetail.getQuantity();
            Product product = productRepository.findById(id).orElse(null);
            if (product == null) {
                orderEvent.setMessage("Product not found");
                orderEvent.setInventoryStatus(Status.InventoryStatus.PENDING.name());
                System.out.println("loi 2");

                rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderEvent);
                return;
            }
            if (quantity <= 0) {
                orderEvent.setMessage("Product quantity must be > 0");
                orderEvent.setInventoryStatus(Status.InventoryStatus.PENDING.name());
                System.out.println("loi 3");

                rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderEvent);
                return;
            }
            if (quantity > product.getUnitInStock()) {
                orderEvent.setMessage("Product " + id + " unit in stock not enough");
                orderEvent.setInventoryStatus(Status.InventoryStatus.OUT_OF_STOCK.name());
                rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderEvent);
                return;
            }
            product.setUnitInStock(product.getUnitInStock() - quantity);
            ExportHistory exportHistory = ExportHistory.ExportHistoryBuilder.anExportHistory()
                    .withOrderId(orderEvent.getOrderId())
                    .withProductId(id)
                    .withQuantity(quantity)
                    .build();
            exportHistorySet.add(exportHistory);
            productSet.add(product);
        }
        try {
            exportHistoryRepository.saveAll(exportHistorySet);
            productRepository.saveAll(productSet);
            orderEvent.setInventoryStatus(Status.InventoryStatus.DONE.name());
            orderEvent.setMessage("Inventory handling success");
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderEvent);
        } catch (Exception e) {
            orderEvent.setInventoryStatus(Status.InventoryStatus.PENDING.name());
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_INVENTORY, orderEvent);
        }
    }

    @Transactional
    public void handleReturn(OrderEvent orderEvent) {
        System.out.println("oke ban");
        Set<ImportHistory> importHistorySet = new HashSet<>();
        Set<Product> productSet = new HashSet<>();
        for (OrderDetailEvent orderDetail : orderEvent.getOrderDetailEvents()) {
            Long id = orderDetail.getProductId();
            int quantity = orderDetail.getQuantity();
            Product product = productRepository.findById(id).orElse(null);
            if (product == null) {
                orderEvent.setMessage("Product not found");
                orderEvent.setInventoryStatus(Status.InventoryStatus.RETURN.name());
                rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderEvent);
                return;
            }
            if (quantity <= 0) {
                orderEvent.setMessage("Product quantity must be > 0");
                orderEvent.setInventoryStatus(Status.InventoryStatus.RETURN.name());
                rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderEvent);
                return;
            }
            product.setUnitInStock(product.getUnitInStock() + quantity);
            ImportHistory importHistory = ImportHistory.ImportHistoryBuilder.anImportHistory()
                    .withProductId(id)
                    .withQuantity(quantity)
                    .withMessage("Order " + orderEvent.getOrderId() + " return product")
                    .build();
            importHistorySet.add(importHistory);
            productSet.add(product);

            try {
                importHistoryRepository.saveAll(importHistorySet);
                productRepository.saveAll(productSet);
                orderEvent.setInventoryStatus(Status.InventoryStatus.RETURNED.name());
                orderEvent.setMessage("Inventory returned successfully");
                rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderEvent);
            } catch (Exception e) {
                orderEvent.setInventoryStatus(Status.InventoryStatus.PENDING.name());
                rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_INVENTORY, orderEvent);
            }
        }
    }
}

