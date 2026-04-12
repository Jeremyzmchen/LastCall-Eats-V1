package com.lastcalleats.merchant.service;

import com.lastcalleats.merchant.dto.MerchantProfileRequest;
import com.lastcalleats.merchant.dto.MerchantProfileResponse;

/**
 * 商家服务接口，定义商家模块的所有业务操作。
 * 包括商家资料的查看、更新和公开主页。
 */
public interface MerchantService {

    MerchantProfileResponse getProfile(Long merchantId);

    MerchantProfileResponse updateProfile(Long merchantId, MerchantProfileRequest request);

    MerchantProfileResponse getPublicProfile(Long merchantId);
}
