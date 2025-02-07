package com.microBuzz.connections_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NewUserDto {

    private Long userId;
    private String email;
    private String name;
    private String bio;

}
