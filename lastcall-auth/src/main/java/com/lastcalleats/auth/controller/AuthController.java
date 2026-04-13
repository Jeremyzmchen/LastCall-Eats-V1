package com.lastcalleats.auth.controller;

import com.lastcalleats.auth.dto.AuthResponse;
import com.lastcalleats.auth.dto.LoginRequest;
import com.lastcalleats.auth.dto.MerchantRegisterRequest;
import com.lastcalleats.auth.dto.UserRegisterRequest;
import com.lastcalleats.auth.service.AuthService;
import com.lastcalleats.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register/user")
  public ResponseEntity<ApiResponse<AuthResponse>> registerUser(
      @Valid @RequestBody UserRegisterRequest request) {
    AuthResponse response = authService.registerUser(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/register/merchant")
  public ResponseEntity<ApiResponse<AuthResponse>> registerMerchant(
      @Valid @RequestBody MerchantRegisterRequest request) {
    AuthResponse response = authService.registerMerchant(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/login/user")
  public ResponseEntity<ApiResponse<AuthResponse>> loginUser(
      @Valid @RequestBody LoginRequest request) {
    AuthResponse response = authService.login(request, "USER");
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/login/merchant")
  public ResponseEntity<ApiResponse<AuthResponse>> loginMerchant(
      @Valid @RequestBody LoginRequest request) {
    AuthResponse response = authService.login(request, "MERCHANT");
    return ResponseEntity.ok(ApiResponse.success(response));
  }
}