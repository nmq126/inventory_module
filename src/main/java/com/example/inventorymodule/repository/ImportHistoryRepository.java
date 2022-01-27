package com.example.inventorymodule.repository;

import com.example.inventorymodule.entity.ImportHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportHistoryRepository extends JpaRepository<ImportHistory, Long> {
}
