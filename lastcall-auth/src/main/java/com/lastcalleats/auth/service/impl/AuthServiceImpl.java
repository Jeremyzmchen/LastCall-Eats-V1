package com.lastcalleats.auth.service.impl;

import com.lastcalleats.auth.dto.AuthResponse;
import com.lastcalleats.auth.dto.LoginRequest;
import com.lastcalleats.auth.dto.MerchantRegisterRequest;
import com.lastcalleats.auth.dto.UserRegisterRequest;
import com.lastcalleats.auth.security.JwtUtil;
import com.lastcalleats.auth.service.AuthService;
import com.lastcalleats.common.exception.BusinessException;
import com.lastcalleats.common.exception.ErrorCode;
import com.lastcalleats.merchant.entity.MerchantDO;
import com.lastcalleats.merchant.repository.MerchantRepo;
import com.lastcalleats.user.entity.UserDO;
import com.lastcalleats.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepo userRepo;
  private final MerchantRepo merchantRepo;
  private final JwtUtil jwtUtil;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.jwt.expiration-ms:86400000}")
  private Long expirationMs;

  @Override
  public AuthResponse registerUser(UserRegisterRequest request) {
    if (userRepo.existsByEmail(request.getEmail())) {
      throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    UserDO user = new UserDO();
    user.setEmail(request.getEmail());
    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    user.setNickname(request.getNickname());

    user = userRepo.save(user);

    String token = jwtUtil.generateToken(user.getId(), "ROLE_USER");
    return buildResponse(token, user.getId(), user.getEmail(), "ROLE_USER");
  }

  @Override
  public AuthResponse registerMerchant(MerchantRegisterRequest request) {
    if (merchantRepo.existsByEmail(request.getEmail())) {
      throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    MerchantDO merchant = new MerchantDO();
    merchant.setEmail(request.getEmail());
    merchant.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    merchant.setName(request.getName());
    merchant.setAddress(request.getAddress());

    merchant = merchantRepo.save(merchant);

    String token = jwtUtil.generateToken(merchant.getId(), "ROLE_MERCHANT");
    return buildResponse(token, merchant.getId(), merchant.getEmail(), "ROLE_MERCHANT");
  }

  @Override
  public AuthResponse login(LoginRequest request, String role) {
    if ("USER".equalsIgnoreCase(role)) {
      UserDO user = userRepo.findByEmail(request.getEmail())
          .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

      if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
        throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
      }

      String token = jwtUtil.generateToken(user.getId(), "ROLE_USER");
      return buildResponse(token, user.getId(), user.getEmail(), "ROLE_USER");
    }

    if ("MERCHANT".equalsIgnoreCase(role)) {
      MerchantDO merchant = merchantRepo.findByEmail(request.getEmail())
          .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

      if (!passwordEncoder.matches(request.getPassword(), merchant.getPasswordHash())) {
        throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
      }

      String token = jwtUtil.generateToken(merchant.getId(), "ROLE_MERCHANT");
      return buildResponse(token, merchant.getId(), merchant.getEmail(), "ROLE_MERCHANT");
    }

    throw new BusinessException(ErrorCode.BAD_REQUEST, "Invalid role");
  }

  private AuthResponse buildResponse(String token, Long userId, String email, String role) {
    AuthResponse response = new AuthResponse();
    response.setAccessToken(token);
    response.setTokenType("Bearer");
    response.setExpiresIn(expirationMs);
    response.setUserId(userId);
    response.setEmail(email);
    response.setRole(role);
    return response;
  }
}