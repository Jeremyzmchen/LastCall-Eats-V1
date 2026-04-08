package com.lastcalleats.order.repository;

import com.lastcalleats.order.entity.OrderDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<OrderDO, Long> {

    List<OrderDO> findByUserId(Long userId);

    List<OrderDO> findByMerchantId(Long merchantId);

    boolean existsByUserIdAndListingId(Long userId, Long listingId);
}
