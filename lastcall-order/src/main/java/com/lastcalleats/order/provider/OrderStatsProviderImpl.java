package com.lastcalleats.order.provider;

import com.lastcalleats.common.provider.OrderStatsProvider;
import com.lastcalleats.order.entity.OrderDO;
import com.lastcalleats.order.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of OrderStatsProvider in the order module.
 * Give the merchant dashboard today's order count and revenue data.
 */
@Component
@RequiredArgsConstructor
public class OrderStatsProviderImpl implements OrderStatsProvider {

    private final OrderRepo orderRepo;

    /**
     * Count orders placed today for the given merchant.
     *
     * @param merchantId the ID of the merchant
     * @return number of orders created today
     */
    @Override
    public int getTodayOrderCount(Long merchantId) {
        List<OrderDO> orders = orderRepo.findByMerchantId(merchantId);
        LocalDate today = LocalDate.now();
        // filter by today's date only
        return (int) orders.stream()
                .filter(order -> order.getCreatedAt().toLocalDate().equals(today))
                .count();
    }

    /**
     * Calculate total revenue from today's PAID and COMPLETED orders.
     *
     * @param merchantId the ID of the merchant
     * @return sum of prices; returns zero if no qualifying orders today
     */
    @Override
    public BigDecimal getTodayRevenue(Long merchantId) {
        List<OrderDO> orders = orderRepo.findByMerchantId(merchantId);
        LocalDate today = LocalDate.now();
        return orders.stream()
                .filter(order -> order.getCreatedAt().toLocalDate().equals(today))
                // only count PAID and COMPLETED, skip PENDING and CANCELLED
                .filter(order -> order.getStatus() == OrderDO.OrderStatus.PAID
                        || order.getStatus() == OrderDO.OrderStatus.COMPLETED)
                .map(OrderDO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
