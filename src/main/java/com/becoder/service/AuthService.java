package com.becoder.service;

import com.becoder.dto.AuthResponse;
import com.becoder.dto.LoginRequest;
import com.becoder.dto.RegisterRequest;

public interface AuthService {

	AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
	
}
