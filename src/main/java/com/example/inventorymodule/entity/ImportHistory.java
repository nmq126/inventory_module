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
@Table(name = "import_histories")
public class ImportHistory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private int quantity;

    private String message;


    public static final class ImportHistoryBuilder {
        private LocalDate createdAt;
        private LocalDate updatedAt;
        private LocalDate deletedAt;
        private Long supplierId;
        private Long productId;
        private int quantity;
        private String message;

        private ImportHistoryBuilder() {
        }

        public static ImportHistoryBuilder anImportHistory() {
            return new ImportHistoryBuilder();
        }

        public ImportHistoryBuilder withCreatedAt(LocalDate createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ImportHistoryBuilder withUpdatedAt(LocalDate updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ImportHistoryBuilder withDeletedAt(LocalDate deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        public ImportHistoryBuilder withSupplierId(Long supplierId) {
            this.supplierId = supplierId;
            return this;
        }

        public ImportHistoryBuilder withProductId(Long productId) {
            this.productId = productId;
            return this;
        }

        public ImportHistoryBuilder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public ImportHistoryBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ImportHistory build() {
            ImportHistory importHistory = new ImportHistory();
            importHistory.setCreatedAt(createdAt);
            importHistory.setUpdatedAt(updatedAt);
            importHistory.setDeletedAt(deletedAt);
            importHistory.setSupplierId(supplierId);
            importHistory.setProductId(productId);
            importHistory.setQuantity(quantity);
            importHistory.setMessage(message);
            return importHistory;
        }
    }
}
