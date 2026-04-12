package com.lastcalleats.user.service.impl;

import com.lastcalleats.common.exception.BusinessException;
import com.lastcalleats.common.exception.ErrorCode;
import com.lastcalleats.merchant.entity.MerchantDO;
import com.lastcalleats.merchant.repository.MerchantRepo;
import com.lastcalleats.user.dto.FavoriteMerchantResponse;
import com.lastcalleats.user.entity.UserFavoriteDO;
import com.lastcalleats.user.repository.UserFavoriteRepo;
import com.lastcalleats.user.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏服务实现类，处理用户收藏商家的业务逻辑。
 * 依赖 UserFavoriteRepo 操作收藏表，依赖 MerchantRepo 查询商家信息。
 */
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final UserFavoriteRepo userFavoriteRepo;
    private final MerchantRepo merchantRepo;

    @Override
    public void addFavorite(Long userId, Long merchantId) {
        // 检查商家是否存在
        merchantRepo.findById(merchantId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MERCHANT_NOT_FOUND));

        // 检查是否已经收藏
        if (userFavoriteRepo.existsByUserIdAndMerchantId(userId, merchantId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "已经收藏过该商家");
        }

        UserFavoriteDO favorite = UserFavoriteDO.builder()
                .userId(userId)
                .merchantId(merchantId)
                .build();
        userFavoriteRepo.save(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long merchantId) {
        if (!userFavoriteRepo.existsByUserIdAndMerchantId(userId, merchantId)) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "未收藏该商家");
        }
        userFavoriteRepo.deleteByUserIdAndMerchantId(userId, merchantId);
    }

    @Override
    public List<FavoriteMerchantResponse> listFavorites(Long userId) {
        List<UserFavoriteDO> favorites = userFavoriteRepo.findByUserId(userId);

        return favorites.stream()
                .map(fav -> {
                    MerchantDO merchant = merchantRepo.findById(fav.getMerchantId())
                            .orElse(null);
                    if (merchant == null) {
                        return null;
                    }
                    return FavoriteMerchantResponse.builder()
                            .merchantId(merchant.getId())
                            .merchantName(merchant.getName())
                            .merchantAddress(merchant.getAddress())
                            .favoritedAt(fav.getCreatedAt())
                            .build();
                })
                .filter(resp -> resp != null)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFavorite(Long userId, Long merchantId) {
        return userFavoriteRepo.existsByUserIdAndMerchantId(userId, merchantId);
    }
}
