package com.example.myapp.model;

import com.example.myapp.repository.ManagerRepository;
import com.example.myapp.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.bytebuddy.implementation.bind.annotation.Default;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import javax.persistence.Transient;

// import org.apache.catalina.Manager;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    @JsonProperty("mob_num")
    private String mobileNumber;

    @NotBlank(message = "PAN number is required")
    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN number")
    @JsonProperty("pan_num")
    private String panNumber;

    @JsonProperty("manager_id")
    private Long managerId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("is_active")
    private boolean isActive;

    @Transient
    private ManagerRepository managerRepository;


    public void setManagerRepository(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @PrePersist
    @PreUpdate
    protected void onPersistOrUpdate() {
        normalizeFields();
        updatedAt = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // @PrePersist
    // @PreUpdate
    protected void normalizeFields() {
        if (mobileNumber != null) {
            // Remove valid prefixes
            if (mobileNumber.startsWith("+91")) {
                mobileNumber = mobileNumber.substring(3);
            } else if (mobileNumber.startsWith("0")) {
                mobileNumber = mobileNumber.substring(1);
            }
        }

        if (panNumber != null) {
            panNumber = panNumber.toUpperCase();
        }

        if (managerId != null) {
            Optional<Manager> optionalManager = managerRepository.findById(managerId);
            if (optionalManager.isPresent()) {
                Manager manager = optionalManager.get();
                if (!manager.isActive()) {
                    throw new IllegalArgumentException("Inactive manager_id");
                }
            } else {
                throw new IllegalArgumentException("Invalid manager_id");
            }
        }
    }

    // @PrePersist
    // protected void onCreate() {
    //     createdAt = LocalDateTime.now();
    //     updatedAt = LocalDateTime.now();
    // }

    // @PreUpdate
    // protected void onUpdate() {
    //     updatedAt = LocalDateTime.now();
    // }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
