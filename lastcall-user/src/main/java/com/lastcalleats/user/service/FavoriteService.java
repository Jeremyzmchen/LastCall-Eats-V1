package com.lastcalleats.user.service;

import com.lastcalleats.user.dto.FavoriteMerchantResponse;

import java.util.List;

/**
 * 用户收藏服务接口，定义收藏相关的业务操作。
 * 与 UserService 分离，遵循单一职责原则。
 */
public interface FavoriteService {

    void addFavorite(Long userId, Long merchantId);

    void removeFavorite(Long userId, Long merchantId);

    List<FavoriteMerchantResponse> listFavorites(Long userId);

    boolean isFavorite(Long userId, Long merchantId);
}
