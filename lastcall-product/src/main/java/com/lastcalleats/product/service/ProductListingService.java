package com.lastcalleats.product.service;

import com.lastcalleats.product.dto.ListingRequest;
import com.lastcalleats.product.dto.ListingResponse;

import java.util.List;

/**
 * Service interface for product listing related business logic.
 *
 * A listing is the actual daily item a merchant publishes for sale.
 */
public interface ProductListingService {

  /**
   * Create a new listing for the current merchant.
   *
   * @param merchantId current merchant id
   * @param request listing request body
   * @return created listing response
   */
  ListingResponse createListing(Long merchantId, ListingRequest request);

  /**
   * Get all listings belonging to the current merchant.
   *
   * @param merchantId current merchant id
   * @return list of listing responses
   */
  List<ListingResponse> getMerchantListings(Long merchantId);

  /**
   * Soft deactivate a listing.
   *
   * @param merchantId current merchant id
   * @param listingId listing id
   */
  void deactivateListing(Long merchantId, Long listingId);

  /**
   * Browse all available listings (for users).
   *
   * @return list of available listing responses
   */
  List<ListingResponse> browseAvailableListings();
}