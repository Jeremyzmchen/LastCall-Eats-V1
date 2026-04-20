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
 * Implementation of {@link com.lastcalleats.common.provider.OrderStatsProvider} in the order module.
 * Supplies today's order count and revenue to the merchant dashboard without creating
 * a direct merchant → order module dependency.
 */
@Component
@RequiredArgsConstructor
public class OrderStatsProviderImpl implements OrderStatsProvider {

    private final OrderRepo orderRepo;

    @Override
    public int getTodayOrderCount(Long merchantId) {
        List<OrderDO> orders = orderRepo.findByMerchantId(merchantId);
        LocalDate today = LocalDate.now();
        return (int) orders.stream()
                .filter(order -> order.getCreatedAt().toLocalDate().equals(today))
                .count();
    }

    @Override
    public BigDecimal getTodayRevenue(Long merchantId) {
        List<OrderDO> orders = orderRepo.findByMerchantId(merchantId);
        LocalDate today = LocalDate.now();
        return orders.stream()
                .filter(order -> order.getCreatedAt().toLocalDate().equals(today))
                .filter(order -> order.getStatus() == OrderDO.OrderStatus.PAID
                        || order.getStatus() == OrderDO.OrderStatus.COMPLETED)
                .map(OrderDO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
