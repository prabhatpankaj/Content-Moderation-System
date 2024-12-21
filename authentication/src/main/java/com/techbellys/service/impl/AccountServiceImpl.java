package com.techbellys.service.impl;

import com.techbellys.dto.LoginDto;
import com.techbellys.dto.RegisterDto;
import com.techbellys.entity.AppUser;
import com.techbellys.mapper.UserMapper;
import com.techbellys.repository.AppUserRepository;
import com.techbellys.service.AccountService;
import com.techbellys.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseEntity<Object> getProfile(Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        response.put("Username", auth.getName());
        response.put("Authorities", auth.getAuthorities());
        var appUser = appUserRepository.findById(auth.getName()).orElseThrow(() -> new IllegalArgumentException("Profile Not found"));;
        // Use a DTO to exclude sensitive information like password
        var userResponse = userMapper.toDto(appUser);
        response.put("User", userResponse);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Object> register(RegisterDto registerDto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = getValidationErrors(result);
            return ResponseEntity.badRequest().body(errors);
        }

        AppUser appUser = userMapper.toEntity(registerDto);

        if (appUserRepository.findByUsername(registerDto.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        if (appUserRepository.findByEmail(registerDto.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        appUserRepository.save(appUser);
        String jwtToken = jwtUtil.createJwtToken(appUser);

        // Use a DTO to exclude sensitive information like password
        var userResponse = userMapper.toDto(appUser);

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("user", userResponse);

        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<Object> login(LoginDto loginDto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = getValidationErrors(result);
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );
            AppUser appUser = appUserRepository.findByUsername(loginDto.getUsername());
            String jwtToken = jwtUtil.createJwtToken(appUser);

            // Use a DTO to exclude sensitive information like password
            var userResponse = userMapper.toDto(appUser);

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("user", userResponse);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Wrong Username or Password");
        }
    }

    private Map<String, String> getValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        for (ObjectError error : result.getAllErrors()) {
            FieldError fieldError = (FieldError) error;
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errors;
    }
}