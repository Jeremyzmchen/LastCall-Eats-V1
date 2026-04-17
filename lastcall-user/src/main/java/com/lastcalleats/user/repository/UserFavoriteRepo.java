package com.lastcalleats.user.repository;

import com.lastcalleats.user.entity.UserFavoriteDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户收藏数据访问接口，对应 user_favorite 表。
 * 提供收藏关系的增删查操作，Spring 会根据方法名自动生成 SQL。
 */
@Repository
public interface UserFavoriteRepo extends JpaRepository<UserFavoriteDO, Long> {

    List<UserFavoriteDO> findByUserId(Long userId);

    Optional<UserFavoriteDO> findByUserIdAndListingId(Long userId, Long listingId);

    boolean existsByUserIdAndListingId(Long userId, Long listingId);

    void deleteByUserIdAndListingId(Long userId, Long listingId);
}
