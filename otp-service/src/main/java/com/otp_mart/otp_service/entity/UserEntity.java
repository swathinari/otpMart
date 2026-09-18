package com.otp_mart.otp_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table  (name = "users",
uniqueConstraints = {
@UniqueConstraint(columnNames = "mobile_number")
    }
)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "mobile_number",
            nullable = false,
            unique = true
    )
    private String mobileNumber;

    @Column(nullable = false)
    private boolean verified = false;

    private LocalDateTime createdAt;

    private LocalDateTime verifiedAt;


    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
