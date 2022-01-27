package com.example.inventorymodule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "export_histories")
public class ExportHistory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private int quantity;

    private String message;


    public static final class ExportHistoryBuilder {
        private LocalDate createdAt;
        private LocalDate updatedAt;
        private LocalDate deletedAt;
        private Long orderId;
        private Long productId;
        private int quantity;
        private String message;

        private ExportHistoryBuilder() {
        }

        public static ExportHistoryBuilder anExportHistory() {
            return new ExportHistoryBuilder();
        }

        public ExportHistoryBuilder withCreatedAt(LocalDate createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ExportHistoryBuilder withUpdatedAt(LocalDate updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ExportHistoryBuilder withDeletedAt(LocalDate deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        public ExportHistoryBuilder withOrderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public ExportHistoryBuilder withProductId(Long productId) {
            this.productId = productId;
            return this;
        }

        public ExportHistoryBuilder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public ExportHistoryBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ExportHistory build() {
            ExportHistory exportHistory = new ExportHistory();
            exportHistory.setCreatedAt(createdAt);
            exportHistory.setUpdatedAt(updatedAt);
            exportHistory.setDeletedAt(deletedAt);
            exportHistory.setOrderId(orderId);
            exportHistory.setProductId(productId);
            exportHistory.setQuantity(quantity);
            exportHistory.setMessage(message);
            return exportHistory;
        }
    }
}

