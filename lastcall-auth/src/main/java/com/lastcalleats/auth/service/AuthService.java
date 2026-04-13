package com.lastcalleats.auth.service;

import com.lastcalleats.auth.dto.AuthResponse;
import com.lastcalleats.auth.dto.LoginRequest;
import com.lastcalleats.auth.dto.MerchantRegisterRequest;
import com.lastcalleats.auth.dto.UserRegisterRequest;

public interface AuthService {

  AuthResponse registerUser(UserRegisterRequest request);

  AuthResponse registerMerchant(MerchantRegisterRequest request);

  AuthResponse login(LoginRequest request, String role);
}