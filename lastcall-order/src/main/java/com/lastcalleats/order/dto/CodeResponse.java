package com.lastcalleats.order.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 商家核销取货码响应体。
 */
@Getter
@Builder
public class CodeResponse {

    private Long orderId;
    private String customerNickname;
    private String productName;
    private Boolean success;
    private String message;
}
