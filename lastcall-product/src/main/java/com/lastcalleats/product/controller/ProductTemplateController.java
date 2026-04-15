package com.lastcalleats.product.controller;

import com.lastcalleats.common.response.ApiResponse;
import com.lastcalleats.product.dto.TemplateRequest;
import com.lastcalleats.product.dto.TemplateResponse;
import com.lastcalleats.product.service.ProductTemplateService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

  private final ProductTemplateService productTemplateService;

  public ProductTemplateController(ProductTemplateService productTemplateService) {
    this.productTemplateService = productTemplateService;
  }

  @PostMapping
  public ApiResponse<TemplateResponse> createTemplate(
      @AuthenticationPrincipal Long merchantId,
      @Valid @RequestBody TemplateRequest request) {
    return ApiResponse.success(productTemplateService.createTemplate(merchantId, request));
  }

  @GetMapping
  public ApiResponse<List<TemplateResponse>> getTemplates(
      @AuthenticationPrincipal Long merchantId) {
    return ApiResponse.success(productTemplateService.getTemplates(merchantId));
  }

  @PutMapping("/{id}")
  public ApiResponse<TemplateResponse> updateTemplate(
      @AuthenticationPrincipal Long merchantId,
      @PathVariable("id") Long templateId,
      @Valid @RequestBody TemplateRequest request) {
    return ApiResponse.success(productTemplateService.updateTemplate(merchantId, templateId, request));
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> deleteTemplate(
      @AuthenticationPrincipal Long merchantId,
      @PathVariable("id") Long templateId) {
    productTemplateService.deleteTemplate(merchantId, templateId);
    return ApiResponse.success();
  }
}