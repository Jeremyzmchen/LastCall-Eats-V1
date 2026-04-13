package com.lastcalleats.product.controller;

import com.lastcalleats.common.response.ApiResponse;
import com.lastcalleats.product.dto.TemplateRequest;
import com.lastcalleats.product.dto.TemplateResponse;
import com.lastcalleats.product.service.ProductTemplateService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for merchant product template APIs.
 *
 * This controller handles:
 * - create template
 * - get all templates
 * - update template
 * - delete template
 */
@RestController
@RequestMapping("/api/merchant/templates")
public class ProductTemplateController {

  /**
   * Service layer for product template business logic.
   */
  private final ProductTemplateService productTemplateService;

  /**
   * Constructor injection.
   *
   * @param productTemplateService template service
   */
  public ProductTemplateController(ProductTemplateService productTemplateService) {
    this.productTemplateService = productTemplateService;
  }

  /**
   * Create a new template.
   *
   * For now, merchantId is temporarily hardcoded to 1L.
   * Later, this should come from Spring Security.
   *
   * @param request template request body
   * @return created template response
   */
  @PostMapping
  public ApiResponse<TemplateResponse> createTemplate(@Valid @RequestBody TemplateRequest request) {
    Long merchantId = 1L;
    TemplateResponse response = productTemplateService.createTemplate(merchantId, request);
    return ApiResponse.success(response);
  }

  /**
   * Get all templates for the current merchant.
   *
   * For now, merchantId is temporarily hardcoded to 1L.
   *
   * @return list of template responses
   */
  @GetMapping
  public ApiResponse<List<TemplateResponse>> getTemplates() {
    Long merchantId = 1L;
    List<TemplateResponse> responses = productTemplateService.getTemplates(merchantId);
    return ApiResponse.success(responses);
  }

  /**
   * Update a template by id.
   *
   * For now, merchantId is temporarily hardcoded to 1L.
   *
   * @param templateId template id from path
   * @param request updated template request body
   * @return updated template response
   */
  @PutMapping("/{id}")
  public ApiResponse<TemplateResponse> updateTemplate(
      @PathVariable("id") Long templateId,
      @Valid @RequestBody TemplateRequest request) {

    Long merchantId = 1L;
    TemplateResponse response = productTemplateService.updateTemplate(merchantId, templateId, request);
    return ApiResponse.success(response);
  }

  /**
   * Soft delete a template by id.
   *
   * For now, merchantId is temporarily hardcoded to 1L.
   *
   * @param templateId template id from path
   * @return success response with no data
   */
  @DeleteMapping("/{id}")
  public ApiResponse<Void> deleteTemplate(@PathVariable("id") Long templateId) {
    Long merchantId = 1L;
    productTemplateService.deleteTemplate(merchantId, templateId);
    return ApiResponse.success();
  }
}