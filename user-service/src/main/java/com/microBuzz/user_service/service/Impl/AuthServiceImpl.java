package com.microBuzz.user_service.service.Impl;

import com.microBuzz.user_service.client.ConnectionsClient;
import com.microBuzz.user_service.dto.LoginRequestDto;
import com.microBuzz.user_service.dto.SignupRequestDto;
import com.microBuzz.user_service.dto.UserDto;
import com.microBuzz.user_service.entity.User;
import com.microBuzz.user_service.event.UserOnboardingEvent;
import com.microBuzz.user_service.exception.BadRequestException;
import com.microBuzz.user_service.exception.ResourceNotFoundException;
import com.microBuzz.user_service.repository.UserRepository;
import com.microBuzz.user_service.service.AuthService;
import com.microBuzz.user_service.service.JwtService;
import com.microBuzz.user_service.utils.PasswordUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final ConnectionsClient connectionsClient;
    private final KafkaTemplate<Long, UserOnboardingEvent> kafkaTemplate;

    @Override
    @Transactional
    public UserDto signUp(SignupRequestDto signupRequestDto) {

        try {
            boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());
            if (exists) throw new BadRequestException("User already exists, Please login instead!");

            User user = modelMapper.map(signupRequestDto, User.class);
            user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));

            User savedUser = userRepository.save(user);
            signupRequestDto.setUserId(savedUser.getId());
            connectionsClient.createNewUser(signupRequestDto);

            UserOnboardingEvent userOnboardingEvent = UserOnboardingEvent.builder()
                    .userId(savedUser.getId())
                    .email(user.getEmail())
                    .name(user.getName()).build();

            kafkaTemplate.send("user-onboarding-topic", savedUser.getId(), userOnboardingEvent);
            log.info("Kafka topic - User onboarding is produced");
            return modelMapper.map(savedUser, UserDto.class);
        } catch (Exception ex) {
            log.error("Exception occurred in signUp , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in signUp: "+ex.getMessage());
        }
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {

        try {
            User user = userRepository.findByEmail(loginRequestDto.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequestDto.getEmail()));

            boolean isPasswordMatch = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());

            if (!isPasswordMatch) {
                throw new BadRequestException("Incorrect password");
            }

            return jwtService.generateAccessToken(user);
        } catch (Exception ex) {
            log.error("Exception occurred in login , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in login: "+ex.getMessage());
        }
    }
}
