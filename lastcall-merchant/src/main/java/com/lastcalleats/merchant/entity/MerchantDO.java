package com.lastcalleats.merchant.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 目前加了注释的字段先保留，不作为V1版本内容
 */

@Entity
@Table(name = "merchant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantDO {
    // Basic fields
    // auto_increment id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Email
    @Column(nullable = false, unique = true, length = 255)
    private String email;
    // Password
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
    // Name
    @Column(nullable = false, length = 255)
    private String name;
    // Phone
    //@Column(length = 20)
    //private String phone;
    // Avatar
    //@Column(name = "cover_url", length = 500)
    //private String coverUrl;
    // Description
    //@Column(columnDefinition = "TEXT")
    //private String description;
    // Address
    @Column(nullable = false, length = 500)
    private String address;
    //@Column(precision = 10, scale = 7)
    //private BigDecimal latitude;
    //@Column(precision = 10, scale = 7)
    //private BigDecimal longitude;
    // Created_at
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    // Updated_at
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    // Supplementary fields
    //
    @Column(name = "business_hours", length = 255)
    private String businessHours;

    //@Column(nullable = false, precision = 2, scale = 1)
    //private BigDecimal rating = BigDecimal.ZERO;

    //@Column(name = "review_count", nullable = false)
    //private Integer reviewCount = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;


}
