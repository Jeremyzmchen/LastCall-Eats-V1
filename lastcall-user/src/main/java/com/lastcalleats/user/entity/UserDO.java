package com.lastcalleats.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

// 注释掉的字段为 V2 预留，暂不开发
@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 100)
    private String nickname;

    //@Column(name = "avatar_url", length = 500)
    //private String avatarUrl;

    //@Enumerated(EnumType.STRING)
    //@Column(name = "auth_provider", nullable = false, length = 20)
    //private AuthProvider authProvider = AuthProvider.LOCAL;

    //@Column(name = "auth_provider_id", length = 255)
    //private String authProviderId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    //public enum AuthProvider {
    //    LOCAL, GOOGLE, FACEBOOK
    //}
}
