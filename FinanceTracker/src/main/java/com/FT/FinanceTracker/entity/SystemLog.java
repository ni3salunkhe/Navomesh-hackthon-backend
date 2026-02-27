package com.FT.FinanceTracker.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "system_logs")
public class SystemLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String level;

    @Column(length = 2000)
    private String message;

    private String source;

    public SystemLog() {
    }

    public SystemLog(UUID id, String level, String message, String source) {
        this.id = id;
        this.level = level;
        this.message = message;
        this.source = source;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
