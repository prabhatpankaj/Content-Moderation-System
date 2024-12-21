package com.techbellys.dto;

import lombok.Data;

@Data
public class AppUserDto {
    private String id;
    private String username;
    private String email;
    private String full_name;
    private String role;
}