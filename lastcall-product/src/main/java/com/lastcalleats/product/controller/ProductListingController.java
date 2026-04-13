package com.lastcalleats.product.controller;

import com.lastcalleats.common.response.ApiResponse;
import com.lastcalleats.product.dto.ListingRequest;
import com.lastcalleats.product.dto.ListingResponse;
import com.lastcalleats.product.service.ProductListingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for merchant product listing APIs.
 *
 * This controller handles:
 * - create listing
 * - get all listings for the current merchant
 * - deactivate listing
 */
@RestController
@RequestMapping("/api/merchant/listings")
public class ProductListingController {

  /**
   * Service layer for listing business logic.
   */
  private final ProductListingService productListingService;

  /**
   * Constructor injection.
   *
   * @param productListingService listing service
   */
  public ProductListingController(ProductListingService productListingService) {
    this.productListingService = productListingService;
  }

  /**
   * Create a new listing.
   *
   * For now, merchantId is temporarily hardcoded to 1L.
   * Later, this should come from Spring Security.
   *
   * @param request listing request body
   * @return created listing response
   */
  @PostMapping
  public ApiResponse<ListingResponse> createListing(@Valid @RequestBody ListingRequest request) {
    Long merchantId = 1L;
    ListingResponse response = productListingService.createListing(merchantId, request);
    return ApiResponse.success(response);
  }

  /**
   * Get all listings for the current merchant.
   *
   * For now, merchantId is temporarily hardcoded to 1L.
   *
   * @return list of listing responses
   */
  @GetMapping
  public ApiResponse<List<ListingResponse>> getMerchantListings() {
    Long merchantId = 1L;
    List<ListingResponse> responses = productListingService.getMerchantListings(merchantId);
    return ApiResponse.success(responses);
  }

  /**
   * Deactivate a listing by id.
   *
   * For now, merchantId is temporarily hardcoded to 1L.
   *
   * @param listingId listing id from path
   * @return success response with no data
   */
  @DeleteMapping("/{id}")
  public ApiResponse<Void> deactivateListing(@PathVariable("id") Long listingId) {
    Long merchantId = 1L;
    productListingService.deactivateListing(merchantId, listingId);
    return ApiResponse.success();
  }
}