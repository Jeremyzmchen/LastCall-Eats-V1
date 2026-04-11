package com.lastcalleats.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request body for creating an order.
 */
@Data
public class OrderRequest {

    // @Data creates getter, setter, toString, hashCode, and equals methods.

    @NotNull
    private Long listingId;
}
