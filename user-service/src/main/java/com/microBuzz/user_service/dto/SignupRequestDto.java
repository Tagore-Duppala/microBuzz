package com.microBuzz.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SignupRequestDto {

    private String name;
    private String email;
    private String password;
}
