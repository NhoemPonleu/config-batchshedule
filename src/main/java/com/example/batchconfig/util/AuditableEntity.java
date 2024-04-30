package com.example.batchconfig.util;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@MappedSuperclass
public class AuditableEntity {
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "time_local")
    private LocalTime localTime;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    private static final ZoneId CAMBODIA_ZONE = ZoneId.of("Asia/Phnom_Penh");

    public AuditableEntity() {
        this.createdAt = LocalDateTime.now(CAMBODIA_ZONE);
        this.updatedAt = LocalDateTime.now(CAMBODIA_ZONE);
        this.localTime = LocalTime.now(CAMBODIA_ZONE);
        this.transactionDate = LocalDate.now(CAMBODIA_ZONE);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    // Additional convenience methods
    public LocalDate getCreatedDate() {
        return createdAt != null ? createdAt.toLocalDate() : null;
    }

    public LocalTime getCreatedTime() {
        return createdAt != null ? createdAt.toLocalTime() : null;
    }

    public LocalDate getUpdatedDate() {
        return updatedAt != null ? updatedAt.toLocalDate() : null;
    }

    public LocalTime getUpdatedTime() {
        return updatedAt != null ? updatedAt.toLocalTime() : null;
    }
}