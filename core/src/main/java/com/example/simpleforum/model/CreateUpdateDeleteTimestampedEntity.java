package com.example.simpleforum.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.repository.Modifying;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@MappedSuperclass
@Data
public abstract class CreateUpdateDeleteTimestampedEntity {
    public static final String SOFT_DELETED_CLAUSE = "deleted_at IS NULL";

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    @PreUpdate
    @Modifying
    public void setTimestamps() {
        updatedAt = Timestamp.from(Instant.now());
        if (Objects.isNull(createdAt)) {
            createdAt = Timestamp.from(Instant.now());
        }
    }
}
