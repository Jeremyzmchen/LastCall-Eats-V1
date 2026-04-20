package com.lastcalleats.merchant.service;

import com.lastcalleats.merchant.dto.MerchantProfileRequest;
import com.lastcalleats.merchant.dto.MerchantProfileResponse;

/** Service interface for merchant-related operations. */
public interface MerchantService {

    MerchantProfileResponse getProfile(Long merchantId);

    MerchantProfileResponse updateProfile(Long merchantId, MerchantProfileRequest request);

    MerchantProfileResponse getPublicProfile(Long merchantId);
}
