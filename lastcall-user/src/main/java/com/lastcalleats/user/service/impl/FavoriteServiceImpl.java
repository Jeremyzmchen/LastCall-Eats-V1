package com.lastcalleats.user.service.impl;

import com.lastcalleats.common.exception.BusinessException;
import com.lastcalleats.common.exception.ErrorCode;
import com.lastcalleats.merchant.entity.MerchantDO;
import com.lastcalleats.merchant.repository.MerchantRepo;
import com.lastcalleats.product.entity.ProductListingDO;
import com.lastcalleats.product.entity.ProductTemplateDO;
import com.lastcalleats.product.repository.ProductListingRepo;
import com.lastcalleats.product.repository.ProductTemplateRepo;
import com.lastcalleats.user.dto.FavoriteListingResponse;
import com.lastcalleats.user.entity.UserFavoriteDO;
import com.lastcalleats.user.repository.UserFavoriteRepo;
import com.lastcalleats.user.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏服务实现类，处理用户收藏 listing 的业务逻辑。
 */
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final UserFavoriteRepo userFavoriteRepo;
    private final ProductListingRepo productListingRepo;
    private final ProductTemplateRepo productTemplateRepo;
    private final MerchantRepo merchantRepo;

    @Override
    public void addFavorite(Long userId, Long listingId) {
        productListingRepo.findById(listingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Listing not found"));

        if (userFavoriteRepo.existsByUserIdAndListingId(userId, listingId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Already favourited");
        }

        userFavoriteRepo.save(UserFavoriteDO.builder()
                .userId(userId)
                .listingId(listingId)
                .build());
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long listingId) {
        if (!userFavoriteRepo.existsByUserIdAndListingId(userId, listingId)) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Favourite not found");
        }
        userFavoriteRepo.deleteByUserIdAndListingId(userId, listingId);
    }

    @Override
    public List<FavoriteListingResponse> listFavorites(Long userId) {
        return userFavoriteRepo.findByUserId(userId).stream()
                .map(fav -> {
                    ProductListingDO listing = productListingRepo.findById(fav.getListingId()).orElse(null);
                    if (listing == null) return null;
                    ProductTemplateDO template = productTemplateRepo.findById(listing.getTemplateId()).orElse(null);
                    if (template == null) return null;
                    MerchantDO merchant = merchantRepo.findById(listing.getMerchantId()).orElse(null);
                    if (merchant == null) return null;
                    return FavoriteListingResponse.builder()
                            .listingId(listing.getId())
                            .merchantId(merchant.getId())
                            .merchantName(merchant.getName())
                            .productName(template.getName())
                            .description(template.getDescription())
                            .originalPrice(template.getOriginalPrice())
                            .discountPrice(listing.getDiscountPrice())
                            .remainingQuantity(listing.getRemainingQuantity())
                            .pickupStart(listing.getPickupStart())
                            .pickupEnd(listing.getPickupEnd())
                            .date(listing.getDate())
                            .isAvailable(listing.getIsAvailable())
                            .favoritedAt(fav.getCreatedAt())
                            .build();
                })
                .filter(r -> r != null)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFavorite(Long userId, Long listingId) {
        return userFavoriteRepo.existsByUserIdAndListingId(userId, listingId);
    }
}