package com.microBuzz.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String name;
    private String bio;

}
