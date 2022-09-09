package com.application.library.history;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HistoryRepo extends JpaRepository<HistoryModel, Long> {
    List<HistoryModel> findByCreatedBy(String name);
    List<HistoryModel> findByCreatedByAndStatus(String name, String status);
    HistoryModel findFirstByBookIdAndStatus(Long id, String status);
    List<HistoryModel> findByCreatedAtGreaterThan(LocalDateTime createdAt);
}
