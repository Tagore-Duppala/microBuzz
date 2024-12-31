package com.microBuzz.user_service.service;

import com.microBuzz.user_service.dto.LoginRequestDto;
import com.microBuzz.user_service.dto.SignupRequestDto;
import com.microBuzz.user_service.dto.UserDto;

public interface AuthService {

    UserDto signUp(SignupRequestDto signupRequestDto);

    String login(LoginRequestDto loginRequestDto);
}
