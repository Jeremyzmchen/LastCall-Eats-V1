package com.lastcalleats.review.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单评价实体类，对应数据库中的 review 表。
 * 每笔已完成订单对应一条评价，针对商家和商品模板双向关联。
 */
@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 关联订单 ID，唯一约束保证一单只能评价一次。 */
    @Column(name = "order_id", nullable = false, unique = true)
    private Long orderId;

    /** 评价用户 ID。 */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 被评价的商家 ID。 */
    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    /** 关联商品模板 ID，用于 Discovery 页面展示该商品的历史评分。 */
    @Column(name = "template_id", nullable = false)
    private Long templateId;

    /** 星级评分，1-5。 */
    @Column(nullable = false)
    private Integer rating;

    /** 评价文字内容，可为空。 */
    @Column(columnDefinition = "TEXT")
    private String content;

    /** 评价图片 URL 列表，MinIO 路径，可为空。 */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "image_urls", columnDefinition = "JSON")
    private List<String> imageUrls;

    /** 是否可见，管理员可将违规评价下架（预留）。 */
    @Column(name = "is_visible", nullable = false)
    @Builder.Default
    private Boolean isVisible = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
