package com.lastcalleats.product.service.impl;

import com.lastcalleats.product.dto.ListingRequest;
import com.lastcalleats.product.dto.ListingResponse;
import com.lastcalleats.product.entity.ProductListingDO;
import com.lastcalleats.product.entity.ProductTemplateDO;
import com.lastcalleats.product.repository.ProductListingRepo;
import com.lastcalleats.product.repository.ProductTemplateRepo;
import com.lastcalleats.product.service.ProductListingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for product listing related business logic.
 *
 * <p>A listing is the actual daily item a merchant publishes for sale.
 * This class handles creating listings, retrieving a merchant's listings,
 * deactivating listings, and browsing available listings for users.</p>
 */
@Service
@Transactional
public class ProductListingServiceImpl implements ProductListingService {

  /**
   * Repository for product listing database operations.
   */
  private final ProductListingRepo productListingRepo;

  /**
   * Repository for product template database operations.
   * A listing must be created from an existing template.
   */
  private final ProductTemplateRepo productTemplateRepo;

  /**
   * Constructs the listing service with required repositories.
   *
   * @param productListingRepo repository for listing data
   * @param productTemplateRepo repository for template data
   */
  public ProductListingServiceImpl(ProductListingRepo productListingRepo,
      ProductTemplateRepo productTemplateRepo) {
    this.productListingRepo = productListingRepo;
    this.productTemplateRepo = productTemplateRepo;
  }

  /**
   * Create a new listing for the current merchant.
   *
   * <p>This method first verifies that the template exists and belongs to
   * the current merchant. It then creates a listing where
   * {@code remainingQuantity} starts equal to {@code quantity}, and
   * {@code isAvailable} is set to true by default.</p>
   *
   * @param merchantId current merchant id
   * @param request listing request data
   * @return the created listing response
   */
  @Override
  public ListingResponse createListing(Long merchantId, ListingRequest request) {
    // Ensure the template exists and belongs to this merchant
    ProductTemplateDO template = getOwnedTemplateOrThrow(merchantId, request.getTemplateId());

    // Build a new listing entity from request data
    ProductListingDO listing = ProductListingDO.builder()
        .merchantId(merchantId)
        .templateId(request.getTemplateId())
        .discountPrice(request.getDiscountPrice())
        .quantity(request.getQuantity())
        .remainingQuantity(request.getQuantity()) // stock starts full
        .pickupStart(request.getPickupStart())
        .pickupEnd(request.getPickupEnd())
        .date(request.getDate())
        .isAvailable(true)
        .build();

    // Save the new listing and convert it to response format
    ProductListingDO savedListing = productListingRepo.save(listing);
    return toResponse(savedListing, template);
  }

  /**
   * Get all listings belonging to the current merchant.
   *
   * @param merchantId current merchant id
   * @return a list of the merchant's listing responses
   */
  @Override
  @Transactional(readOnly = true)
  public List<ListingResponse> getMerchantListings(Long merchantId) {
    // Load all listings for this merchant
    List<ProductListingDO> listings = productListingRepo.findByMerchantId(merchantId);

    // Convert each listing entity to response DTO
    return listings.stream()
        .map(this::toResponseWithTemplateLookup)
        .collect(Collectors.toList());
  }

  /**
   * Soft deactivate a listing.
   *
   * <p>This method does not delete the row from the database.
   * It only marks the listing as unavailable.</p>
   *
   * @param merchantId current merchant id
   * @param listingId listing id to deactivate
   */
  @Override
  public void deactivateListing(Long merchantId, Long listingId) {
    // Ensure the listing exists and belongs to this merchant
    ProductListingDO listing = getOwnedListingOrThrow(merchantId, listingId);

    // Soft deactivate the listing
    listing.setIsAvailable(false);

    // Save the updated listing
    productListingRepo.save(listing);
  }

  /**
   * Browse all currently available listings for users.
   *
   * <p>This method is used by the user-facing browse page. It loads all listings
   * from the database, keeps only listings that are still available and have
   * remaining stock, and then converts them into response DTOs.</p>
   *
   * @return a list of available listing responses
   */
  @Override
  @Transactional(readOnly = true)
  public List<ListingResponse> browseAvailableListings() {
    // Load all listing records from the database
    List<ProductListingDO> listings = productListingRepo.findAll();

    return listings.stream()
        // Keep only listings that are marked as available
        .filter(listing -> Boolean.TRUE.equals(listing.getIsAvailable()))
        // Keep only listings that still have stock left
        .filter(listing -> listing.getRemainingQuantity() > 0)
        // Convert each listing entity into a response DTO
        .map(this::toResponseWithTemplateLookup)
        // Collect all converted results into a list
        .collect(Collectors.toList());
  }

  /**
   * Find a template by id and verify it belongs to the current merchant.
   *
   * @param merchantId current merchant id
   * @param templateId template id
   * @return the owned template entity
   * @throws RuntimeException if the template does not exist or belongs to another merchant
   */
  private ProductTemplateDO getOwnedTemplateOrThrow(Long merchantId, Long templateId) {
    ProductTemplateDO template = productTemplateRepo.findById(templateId)
        .orElseThrow(() -> new RuntimeException("Template not found."));

    // Prevent a merchant from using another merchant's template
    if (!template.getMerchantId().equals(merchantId)) {
      throw new RuntimeException("You cannot use another merchant's template.");
    }

    return template;
  }

  /**
   * Find a listing by id and verify it belongs to the current merchant.
   *
   * @param merchantId current merchant id
   * @param listingId listing id
   * @return the owned listing entity
   * @throws RuntimeException if the listing does not exist or belongs to another merchant
   */
  private ProductListingDO getOwnedListingOrThrow(Long merchantId, Long listingId) {
    ProductListingDO listing = productListingRepo.findById(listingId)
        .orElseThrow(() -> new RuntimeException("Listing not found."));

    // Prevent a merchant from operating on another merchant's listing
    if (!listing.getMerchantId().equals(merchantId)) {
      throw new RuntimeException("You cannot operate on another merchant's listing.");
    }

    return listing;
  }

  /**
   * Convert a listing entity and its template entity into a listing response DTO.
   *
   * @param listing listing entity
   * @param template related template entity
   * @return listing response DTO
   */
  private ListingResponse toResponse(ProductListingDO listing, ProductTemplateDO template) {
    return ListingResponse.builder()
        .id(listing.getId())
        .merchantId(listing.getMerchantId())
        .templateId(listing.getTemplateId())
        .templateName(template.getName())
        .originalPrice(template.getOriginalPrice())
        .discountPrice(listing.getDiscountPrice())
        .quantity(listing.getQuantity())
        .remainingQuantity(listing.getRemainingQuantity())
        .pickupStart(listing.getPickupStart())
        .pickupEnd(listing.getPickupEnd())
        .date(listing.getDate())
        .isAvailable(listing.getIsAvailable())
        .build();
  }

  /**
   * Convert a listing entity into a response DTO by first loading its template.
   *
   * @param listing listing entity
   * @return listing response DTO
   */
  private ListingResponse toResponseWithTemplateLookup(ProductListingDO listing) {
    // Load the related template so response can include template info
    ProductTemplateDO template = productTemplateRepo.findById(listing.getTemplateId())
        .orElseThrow(() -> new RuntimeException("Template not found for listing."));

    return toResponse(listing, template);
  }
}