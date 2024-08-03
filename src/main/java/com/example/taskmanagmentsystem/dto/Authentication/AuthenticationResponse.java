package com.example.taskmanagmentsystem.dto.Authentication;

import com.example.taskmanagmentsystem.util.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
    private String email;
    private Role role;
}