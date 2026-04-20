package com.lastcalleats.order.provider;

import com.lastcalleats.order.entity.OrderDO;
import com.lastcalleats.order.repository.OrderRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Unit tests for OrderStatsProviderImpl. */
@ExtendWith(MockitoExtension.class)
class OrderStatsProviderImplTest {

    @Mock
    private OrderRepo orderRepo;

    @InjectMocks
    private OrderStatsProviderImpl orderStatsProvider;

    private OrderDO todayPaid;
    private OrderDO todayCompleted;
    private OrderDO todayPending;
    private OrderDO yesterday;

    @BeforeEach
    void setUp() {
        todayPaid = new OrderDO();
        todayPaid.setMerchantId(1L);
        todayPaid.setPrice(new BigDecimal("10.00"));
        todayPaid.setStatus(OrderDO.OrderStatus.PAID);
        todayPaid.setCreatedAt(LocalDateTime.now());

        todayCompleted = new OrderDO();
        todayCompleted.setMerchantId(1L);
        todayCompleted.setPrice(new BigDecimal("20.00"));
        todayCompleted.setStatus(OrderDO.OrderStatus.COMPLETED);
        todayCompleted.setCreatedAt(LocalDateTime.now());

        todayPending = new OrderDO();
        todayPending.setMerchantId(1L);
        todayPending.setPrice(new BigDecimal("15.00"));
        todayPending.setStatus(OrderDO.OrderStatus.PENDING_PAYMENT);
        todayPending.setCreatedAt(LocalDateTime.now());

        yesterday = new OrderDO();
        yesterday.setMerchantId(1L);
        yesterday.setPrice(new BigDecimal("50.00"));
        yesterday.setStatus(OrderDO.OrderStatus.PAID);
        yesterday.setCreatedAt(LocalDate.now().minusDays(1).atStartOfDay());
    }

    @Test
    void getTodayOrderCount_filtersToday() {
        when(orderRepo.findByMerchantId(1L))
                .thenReturn(List.of(todayPaid, todayCompleted, todayPending, yesterday));

        int count = orderStatsProvider.getTodayOrderCount(1L);

        assertEquals(3, count); // yesterday excluded
    }

    @Test
    void getTodayOrderCount_noOrdersToday_returnsZero() {
        when(orderRepo.findByMerchantId(1L)).thenReturn(List.of(yesterday));

        assertEquals(0, orderStatsProvider.getTodayOrderCount(1L));
    }

    @Test
    void getTodayRevenue_filtersByStatusAndDate() {
        when(orderRepo.findByMerchantId(1L))
                .thenReturn(List.of(todayPaid, todayCompleted, todayPending, yesterday));

        BigDecimal revenue = orderStatsProvider.getTodayRevenue(1L);

        // todayPaid(10) + todayCompleted(20); todayPending and yesterday excluded
        assertEquals(new BigDecimal("30.00"), revenue);
    }

    @Test
    void getTodayRevenue_onlyPending_returnsZero() {
        when(orderRepo.findByMerchantId(1L)).thenReturn(List.of(todayPending));

        assertEquals(BigDecimal.ZERO, orderStatsProvider.getTodayRevenue(1L));
    }
}
