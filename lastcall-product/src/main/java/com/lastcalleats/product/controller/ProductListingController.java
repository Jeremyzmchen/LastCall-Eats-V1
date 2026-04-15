package com.lastcalleats.product.controller;

import com.lastcalleats.common.response.ApiResponse;
import com.lastcalleats.product.dto.ListingRequest;
import com.lastcalleats.product.dto.ListingResponse;
import com.lastcalleats.product.dto.UserBrowseResponse;
import com.lastcalleats.product.service.ProductListingService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for merchant product listing APIs.
 *
 * This controller handles:
 * - create listing
 * - get all listings for the current merchant
 * - deactivate listing
 * - browse available listings for users
 */
@RestController
public class ProductListingController {

  private final ProductListingService productListingService;

  public ProductListingController(ProductListingService productListingService) {
    this.productListingService = productListingService;
  }

  /**
   * Browse all available listings (no auth required).
   *
   * @return list of available listings with merchant info
   */
  @GetMapping("/api/products/browse")
  public ApiResponse<List<UserBrowseResponse>> browseListings() {
    return ApiResponse.success(productListingService.browseAvailableListings());
  }

  /**
   * Create a new listing.
   *
   * @param merchantId current merchant from JWT
   * @param request listing request body
   * @return created listing response
   */
  @PostMapping("/api/merchant/listings")
  public ApiResponse<ListingResponse> createListing(
      @AuthenticationPrincipal Long merchantId,
      @Valid @RequestBody ListingRequest request) {
    return ApiResponse.success(productListingService.createListing(merchantId, request));
  }

  /**
   * Get all listings for the current merchant.
   *
   * @param merchantId current merchant from JWT
   * @return list of listing responses
   */
  @GetMapping("/api/merchant/listings")
  public ApiResponse<List<ListingResponse>> getMerchantListings(
      @AuthenticationPrincipal Long merchantId) {
    return ApiResponse.success(productListingService.getMerchantListings(merchantId));
  }

  /**
   * Deactivate a listing by id.
   *
   * @param merchantId current merchant from JWT
   * @param listingId listing id from path
   * @return success response with no data
   */
  @DeleteMapping("/api/merchant/listings/{id}")
  public ApiResponse<Void> deactivateListing(
      @AuthenticationPrincipal Long merchantId,
      @PathVariable("id") Long listingId) {
    productListingService.deactivateListing(merchantId, listingId);
    return ApiResponse.success();
  }
}