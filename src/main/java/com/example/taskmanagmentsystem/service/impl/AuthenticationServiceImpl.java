package com.example.taskmanagmentsystem.service.impl;

import com.example.taskmanagmentsystem.config.jwt.JwtService;
import com.example.taskmanagmentsystem.dto.Authentication.AuthenticationResponse;
import com.example.taskmanagmentsystem.dto.Authentication.SignInRequest;
import com.example.taskmanagmentsystem.dto.Authentication.SignUpRequest;
import com.example.taskmanagmentsystem.exception.AlreadyExistsException;
import com.example.taskmanagmentsystem.model.UserInfo;
import com.example.taskmanagmentsystem.repository.UserInfoRepository;
import com.example.taskmanagmentsystem.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserInfoRepository userInfoRepository;

    @Override
    public AuthenticationResponse signUp(SignUpRequest request) {
        if (userInfoRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("User already exists");
        }

        UserInfo userAccount = UserInfo.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(null)
                .build();

        userInfoRepository.save(userAccount);

        String jwt = jwtService.generateToken(userAccount);

        return AuthenticationResponse.builder()
                .email(userAccount.getEmail())
                .role(userAccount.getRole())
                .token(jwt)
                .build();
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest request) {
        UserInfo user = userInfoRepository.getUserAccountByEmail(request.getEmail()).orElseThrow(() ->
                new RuntimeException("email or password invalid"));

        String passwordBCrypt = request.getPassword();
        passwordEncoder.encode(passwordBCrypt);

        if (!passwordEncoder.matches(passwordBCrypt, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String jwt = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .token(jwt)
                .build();
    }
}