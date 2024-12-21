package com.techbellys.controller;

import com.techbellys.constants.ApiEndpoints;
import com.techbellys.dto.LoginDto;
import com.techbellys.dto.RegisterDto;
import com.techbellys.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiEndpoints.ACCOUNT)
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping(ApiEndpoints.PROFILE)
    public ResponseEntity<Object> profile(Authentication auth) {
        return accountService.getProfile(auth);
    }

    @PostMapping(ApiEndpoints.REGISTER)
    public ResponseEntity<Object> register(
            @Valid @RequestBody RegisterDto registerDto, BindingResult result) {
        return accountService.register(registerDto, result);
    }

    @PostMapping(ApiEndpoints.LOGIN)
    public ResponseEntity<Object> login(
            @Valid @RequestBody LoginDto loginDto, BindingResult result) {
        return accountService.login(loginDto, result);
    }
}

