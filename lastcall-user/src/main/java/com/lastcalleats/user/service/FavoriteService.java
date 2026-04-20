package com.lastcalleats.user.service;

import com.lastcalleats.user.dto.FavoriteListingResponse;

import java.util.List;

/** Service interface for managing a user's favourite listings (separate from UserService per SRP). */
public interface FavoriteService {

    void addFavorite(Long userId, Long listingId);

    void removeFavorite(Long userId, Long listingId);

    List<FavoriteListingResponse> listFavorites(Long userId);

    boolean isFavorite(Long userId, Long listingId);
}
