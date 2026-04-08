package com.lastcalleats.merchant.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// 注释掉的字段为 V2 预留，暂不开发
@Entity
@Table(name = "merchant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 255)
    private String name;

    //@Column(length = 20)
    //private String phone;

    //@Column(name = "cover_url", length = 500)
    //private String coverUrl;

    //@Column(columnDefinition = "TEXT")
    //private String description;

    @Column(nullable = false, length = 500)
    private String address;

    //@Column(precision = 10, scale = 7)
    //private BigDecimal latitude;

    //@Column(precision = 10, scale = 7)
    //private BigDecimal longitude;

    @Column(name = "business_hours", length = 255)
    private String businessHours;

    //@Column(nullable = false, precision = 2, scale = 1)
    //private BigDecimal rating = BigDecimal.ZERO;

    //@Column(name = "review_count", nullable = false)
    //private Integer reviewCount = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
