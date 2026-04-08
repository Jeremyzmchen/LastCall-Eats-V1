package com.lastcalleats.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CodeResponse {

    private Long orderId;
    private String customerNickname;
    private String productName;
    private Boolean success;
    private String message;
}
