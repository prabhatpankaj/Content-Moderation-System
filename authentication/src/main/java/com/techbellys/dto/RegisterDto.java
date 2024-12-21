package com.techbellys.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RegisterDto {
    private String full_name;
    private String username;
    private String email;
    @NotEmpty
    @Size(min = 6, message = "Minimum password length is 6 charactors")
    private String password;
}