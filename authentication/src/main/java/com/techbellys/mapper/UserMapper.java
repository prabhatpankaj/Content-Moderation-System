package com.techbellys.mapper;

import com.techbellys.dto.*;
import com.techbellys.entity.AppUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AppUser toEntity(RegisterDto registerDto) {
        var bCryptEncoder = new BCryptPasswordEncoder();

        AppUser appUser = new AppUser();
        appUser.setFull_name(registerDto.getFull_name());
        appUser.setUsername(registerDto.getUsername());
        appUser.setEmail(registerDto.getEmail());
        appUser.setRole("client");
        appUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));
        return appUser;
    }

    public AppUserDto toDto(AppUser appUser) {
        AppUserDto dto = new AppUserDto();
        dto.setId(appUser.getId());
        dto.setUsername(appUser.getUsername());
        dto.setEmail(appUser.getEmail());
        dto.setFull_name(appUser.getFull_name());
        dto.setRole(appUser.getRole());
        return dto;
    }
}
