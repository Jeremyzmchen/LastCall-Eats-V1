package com.lastcalleats.product.repository;

import com.lastcalleats.product.entity.ProductListingDO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Repository tests for {@link ProductListingRepo}.
 *
 * <p>This class verifies that custom query methods return correct results.</p>
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ProductListingRepoTest {

  @Autowired
  private ProductListingRepo productListingRepo;

  /**
   * Minimal configuration for JPA test context.
   */
  @Configuration
  @EnableAutoConfiguration
  @EntityScan("com.lastcalleats.product.entity")
  @EnableJpaRepositories("com.lastcalleats.product.repository")
  static class TestConfig {}

  @Test
  @DisplayName("findByMerchantId works correctly")
  void findByMerchantId_returnsCorrectResults() {
    productListingRepo.save(build(1L, true));
    productListingRepo.save(build(1L, false));
    productListingRepo.save(build(2L, true));

    List<ProductListingDO> result = productListingRepo.findByMerchantId(1L);

    assertEquals(2, result.size());
  }

  @Test
  @DisplayName("findByMerchantIdAndIsAvailableTrue works correctly")
  void findByMerchantIdAndIsAvailableTrue_returnsOnlyActive() {
    productListingRepo.save(build(1L, true));
    productListingRepo.save(build(1L, false));
    productListingRepo.save(build(1L, true));

    List<ProductListingDO> result =
        productListingRepo.findByMerchantIdAndIsAvailableTrue(1L);

    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(ProductListingDO::getIsAvailable));
  }

  @Test
  @DisplayName("findAllByOrderByCreatedAtDesc sorts correctly")
  void findAllByOrderByCreatedAtDesc_returnsSorted() {
    productListingRepo.save(buildWithTime(1L, true, LocalDateTime.now().minusHours(2)));
    productListingRepo.save(buildWithTime(1L, true, LocalDateTime.now().minusHours(1)));
    productListingRepo.save(buildWithTime(1L, true, LocalDateTime.now()));

    List<ProductListingDO> result =
        productListingRepo.findAllByOrderByCreatedAtDesc();

    assertEquals(3, result.size());
    assertTrue(result.get(0).getCreatedAt()
        .isAfter(result.get(1).getCreatedAt()));
  }

  // -------- helper methods --------

  private ProductListingDO build(Long merchantId, Boolean available) {
    return buildWithTime(merchantId, available, LocalDateTime.now());
  }

  private ProductListingDO buildWithTime(
      Long merchantId,
      Boolean available,
      LocalDateTime createdAt
  ) {
    return ProductListingDO.builder()
        .merchantId(merchantId)
        .templateId(100L)
        .discountPrice(new BigDecimal("9.99"))
        .quantity(5)
        .remainingQuantity(5)
        .pickupStart(LocalTime.of(18, 0))
        .pickupEnd(LocalTime.of(20, 0))
        .date(LocalDate.now())
        .isAvailable(available)
        .createdAt(createdAt)
        .build();
  }
}