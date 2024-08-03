package com.example.taskmanagmentsystem.api;

import com.example.taskmanagmentsystem.dto.Authentication.AuthenticationResponse;
import com.example.taskmanagmentsystem.dto.Authentication.SignInRequest;
import com.example.taskmanagmentsystem.dto.Authentication.SignUpRequest;
import com.example.taskmanagmentsystem.service.AuthenticationService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован."),
            @ApiResponse(responseCode = "400", description = "Ошибки при регистрации, например, пользователь уже существует.")
    })
    @PostMapping("/sign-up")
    public AuthenticationResponse signUp(@RequestBody SignUpRequest request) {
        /**
         * Регистрация в систему на основании предоставленных данных.
         * @param request - объект, содержащий данные нового пользователя.
         * @return AuthenticationResponse - ответ с token для входа в систему.
         */
        return authenticationService.signUp(request);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно вошел в систему."),
            @ApiResponse(responseCode = "401", description = "Неверное имя пользователя или пароль.")
    })
    @PostMapping("/sign-in")
    public AuthenticationResponse signIn(@RequestBody SignInRequest request) {
        /**
         * Авторизация в систему на основании предоставленных данных.
         * @param request - объект, содержащий данные пользователя.
         * @return AuthenticationResponse - ответ с token для входа в систему.
         */
        return authenticationService.signIn(request);
    }
}