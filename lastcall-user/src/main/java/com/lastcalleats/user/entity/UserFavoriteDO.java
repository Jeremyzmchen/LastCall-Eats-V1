package com.lastcalleats.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 用户收藏实体类，对应数据库中的 user_favorite 表。
 * 记录用户与限时商品之间的收藏关系，一个用户可以收藏多个 listing。
 */
@Entity
@Table(name = "user_favorite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFavoriteDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "listing_id", nullable = false)
    private Long listingId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
