package com.FT.FinanceTracker.dto;

import com.FT.FinanceTracker.entity.User;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserAdminDto {
    private UUID id;
    private String email;
    private String fullName;
    private User.Role role;
    private User.Status status;
    private LocalDateTime createdAt;

    public UserAdminDto() {}

    public UserAdminDto(UUID id, String email, String fullName, User.Role role, User.Status status, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public User.Role getRole() { return role; }
    public void setRole(User.Role role) { this.role = role; }

    public User.Status getStatus() { return status; }
    public void setStatus(User.Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
