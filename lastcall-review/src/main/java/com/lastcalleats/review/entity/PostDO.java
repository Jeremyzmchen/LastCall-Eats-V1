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
 * 社区帖子实体类，对应数据库中的 post 表。
 * 用户可自由发布帖子，并可选择关联某家商家，支持点赞和评论。
 */
@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 发帖用户 ID，关联 user 表。 */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 关联商家 ID，可为空（帖子不强制绑定商家）。 */
    @Column(name = "merchant_id")
    private Long merchantId;

    /** 帖子正文，不允许为空。 */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /** 帖子图片 URL 列表，存储为 JSON 数组，MinIO 路径。 */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "image_urls", columnDefinition = "JSON")
    private List<String> imageUrls;

    /** 点赞数冗余字段，由 ReactionService 同步维护，避免每次 COUNT 查询。 */
    @Column(name = "like_count", nullable = false)
    @Builder.Default
    private Integer likeCount = 0;

    /** 评论数冗余字段，由 CommentService 同步维护。 */
    @Column(name = "comment_count", nullable = false)
    @Builder.Default
    private Integer commentCount = 0;

    /** 帖子是否可见，管理员可将违规帖子下架（预留）。 */
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
