package com.techbellys.service;

import com.techbellys.dto.LoginDto;
import com.techbellys.dto.RegisterDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;

public interface AccountService {
    ResponseEntity<Object> getProfile(Authentication auth);
    ResponseEntity<Object> register(RegisterDto registerDto, BindingResult result);
    ResponseEntity<Object> login(LoginDto loginDto, BindingResult result);
}
