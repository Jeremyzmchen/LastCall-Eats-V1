package com.lastcalleats.merchant.service.impl;

import com.lastcalleats.common.exception.BusinessException;
import com.lastcalleats.common.exception.ErrorCode;
import com.lastcalleats.merchant.dto.MerchantProfileRequest;
import com.lastcalleats.merchant.dto.MerchantProfileResponse;
import com.lastcalleats.merchant.entity.MerchantDO;
import com.lastcalleats.merchant.repository.MerchantRepo;
import com.lastcalleats.merchant.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 商家服务实现类，处理商家模块的核心业务逻辑。
 * 包括商家资料的查看、更新和公开主页。
 */
@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepo merchantRepo;

    @Override
    public MerchantProfileResponse getProfile(Long merchantId) {
        MerchantDO merchant = findMerchantById(merchantId);
        return toResponse(merchant);
    }

    @Override
    public MerchantProfileResponse updateProfile(Long merchantId, MerchantProfileRequest request) {
        MerchantDO merchant = findMerchantById(merchantId);
        merchant.setName(request.getName());
        merchant.setAddress(request.getAddress());
        merchant.setBusinessHours(request.getBusinessHours());
        merchantRepo.save(merchant);
        return toResponse(merchant);
    }

    @Override
    public MerchantProfileResponse getPublicProfile(Long merchantId) {
        MerchantDO merchant = findMerchantById(merchantId);
        return toResponse(merchant);
    }

    /**
     * 根据 ID 查找商家，不存在则抛出异常。
     */
    private MerchantDO findMerchantById(Long merchantId) {
        return merchantRepo.findById(merchantId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MERCHANT_NOT_FOUND));
    }

    /**
     * 将 Entity 转换为 DTO，避免重复代码。
     */
    private MerchantProfileResponse toResponse(MerchantDO merchant) {
        return MerchantProfileResponse.builder()
                .id(merchant.getId())
                .email(merchant.getEmail())
                .name(merchant.getName())
                .address(merchant.getAddress())
                .businessHours(merchant.getBusinessHours())
                .isActive(merchant.getIsActive())
                .build();
    }
}
