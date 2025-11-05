package com.marciocleydev.Time_bank_for_employees.controllers;

import com.marciocleydev.Time_bank_for_employees.DTO.security.AccountCredentialsDTO;
import com.marciocleydev.Time_bank_for_employees.services.AuthService;
import com.marciocleydev.Time_bank_for_employees.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Authentication endpoint")
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService userService){
        this.authService = userService;
    }

    @Operation(summary = "Authenticate a user and generate a token")
    @PostMapping(value = "/signin")
    public ResponseEntity<?> singnin(@RequestBody AccountCredentialsDTO credentials){
        if (credentialsIsInvalid(credentials)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var token = authService.signin(credentials);
    }

    private boolean credentialsIsInvalid(AccountCredentialsDTO credentials) {
        return credentials == null ||
                !StringUtils.hasText(credentials.getUsername()) ||
                !StringUtils.hasText(credentials.getPassword());
    }
}
