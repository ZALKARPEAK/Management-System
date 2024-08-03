package com.example.taskmanagmentsystem.dto.Authentication;

import com.example.taskmanagmentsystem.util.validator.EmailValidation;
import com.example.taskmanagmentsystem.util.validator.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignUpRequest {
    private String username;
    @EmailValidation
    private String email;
    @ValidPassword
    private String password;
}