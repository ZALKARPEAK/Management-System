package com.example.taskmanagmentsystem.api;

import com.example.taskmanagmentsystem.dto.Authentication.AuthenticationResponse;
import com.example.taskmanagmentsystem.dto.Authentication.SignInRequest;
import com.example.taskmanagmentsystem.dto.Authentication.SignUpRequest;
import com.example.taskmanagmentsystem.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationApi {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public AuthenticationResponse register(@RequestBody SignUpRequest request) {
        /**
         * Регистрация в систему.
         * @param request - данные для регистрации пользователя.
         * @return AuthenticationResponse - возвращает токен, почту и роль.
         */
        return authenticationService.signUp(request);
    }

    @PostMapping("/login")
    public AuthenticationResponse authenticate(@RequestBody SignInRequest request) {
        /**
         * Авторизация в систему.
         * @param request - данные для авторизации пользователя.
         * @return AuthenticationResponse - возвращает токен, почту и роль.
         */
        return authenticationService.signIn(request);
    }
}