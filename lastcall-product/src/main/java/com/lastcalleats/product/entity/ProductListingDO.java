package com.lastcalleats.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 限时商品 Listing —— 商家基于模板每天发布的具体商品。
 * 包含折扣价、库存、取餐时间窗口等每次发布时才确定的信息。
 */
@Entity
@Table(name = "product_listing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListingDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    // 关联的商品模板
    @Column(name = "template_id", nullable = false)
    private Long templateId;

    // 本次发布的折扣售价
    @Column(name = "discount_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountPrice;

    // 本次发布的总库存
    @Column(nullable = false)
    private Integer quantity;

    // 剩余库存（下单时递减）
    @Column(name = "remaining_quantity", nullable = false)
    private Integer remainingQuantity;

    // 取餐时间窗口
    @Column(name = "pickup_start", nullable = false)
    private LocalTime pickupStart;

    @Column(name = "pickup_end", nullable = false)
    private LocalTime pickupEnd;

    // 发布日期
    @Column(nullable = false)
    private LocalDate date;

    // 是否仍然对用户可见/可购买
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
