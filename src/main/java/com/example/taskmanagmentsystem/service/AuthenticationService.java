package com.example.taskmanagmentsystem.service;


import com.example.taskmanagmentsystem.dto.Authentication.AuthenticationResponse;
import com.example.taskmanagmentsystem.dto.Authentication.SignInRequest;
import com.example.taskmanagmentsystem.dto.Authentication.SignUpRequest;

public interface AuthenticationService {
    AuthenticationResponse signUp(SignUpRequest request);
    AuthenticationResponse signIn(SignInRequest request);
}