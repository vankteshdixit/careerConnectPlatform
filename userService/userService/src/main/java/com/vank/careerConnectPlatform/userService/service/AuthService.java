package com.vank.careerConnectPlatform.userService.service;

import com.vank.careerConnectPlatform.userService.dto.LoginRequestDto;
import com.vank.careerConnectPlatform.userService.dto.SignUpRequestDto;
import com.vank.careerConnectPlatform.userService.dto.UserDto;
import com.vank.careerConnectPlatform.userService.entity.User;
import com.vank.careerConnectPlatform.userService.exception.BadRequestException;
import com.vank.careerConnectPlatform.userService.exception.ResourceNotFoundException;
import com.vank.careerConnectPlatform.userService.repository.UserRepository;
import com.vank.careerConnectPlatform.userService.utils.BCrypt;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserDto signup(SignUpRequestDto signUpRequestDto) {
        log.info("Signup a user with email: {}", signUpRequestDto.getEmail());

        boolean exists = userRepository.existsByEmail(signUpRequestDto.getEmail());
        if(exists){
            throw new BadRequestException("User already exists");
        }

        User user = modelMapper.map(signUpRequestDto, User.class);
        user.setPassword(BCrypt.hash(signUpRequestDto.getPassword()));

        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    public String login(LoginRequestDto loginRequestDto) {
        log.info("Login a user with email: {}", loginRequestDto.getEmail());

        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(()-> new BadRequestException(
                "Incorrect email or password"));


        boolean isPasswordMath = BCrypt.match(loginRequestDto.getPassword(), user.getPassword());
        if (!isPasswordMath){
            throw new BadRequestException("Incorrect email or password");
        }
        return jwtService.generateAccessToken(user);
    }
}
