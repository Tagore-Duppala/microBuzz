package com.microBuzz.user_service.service.Impl;

import com.microBuzz.user_service.dto.LoginRequestDto;
import com.microBuzz.user_service.dto.SignupRequestDto;
import com.microBuzz.user_service.dto.UserDto;
import com.microBuzz.user_service.entity.User;
import com.microBuzz.user_service.exception.BadRequestException;
import com.microBuzz.user_service.exception.ResourceNotFoundException;
import com.microBuzz.user_service.repository.UserRepository;
import com.microBuzz.user_service.service.AuthService;
import com.microBuzz.user_service.service.JwtService;
import com.microBuzz.user_service.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    @Override
    public UserDto signUp(SignupRequestDto signupRequestDto) {
        boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());
        if(exists) throw new BadRequestException("User already exists, Please login instead!");

        User user = modelMapper.map(signupRequestDto, User.class);
        user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: "+loginRequestDto.getEmail()));

        boolean isPasswordMatch = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());

        if(!isPasswordMatch) {
            throw new BadRequestException("Incorrect password");
        }

        return jwtService.generateAccessToken(user);
    }
}
