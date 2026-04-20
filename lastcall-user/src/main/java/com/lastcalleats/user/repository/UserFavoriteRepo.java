package com.lastcalleats.user.repository;

import com.lastcalleats.user.entity.UserFavoriteDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** Spring Data JPA repository for UserFavoriteDO. */
@Repository
public interface UserFavoriteRepo extends JpaRepository<UserFavoriteDO, Long> {

    List<UserFavoriteDO> findByUserId(Long userId);

    Optional<UserFavoriteDO> findByUserIdAndListingId(Long userId, Long listingId);

    boolean existsByUserIdAndListingId(Long userId, Long listingId);

    void deleteByUserIdAndListingId(Long userId, Long listingId);
}
