package com.marciocleydev.Time_bank_for_employees.controllers;

import com.marciocleydev.Time_bank_for_employees.DTO.security.AccountCredentialsDTO;
import com.marciocleydev.Time_bank_for_employees.controllers.docs.AuthControllerDocs;
import com.marciocleydev.Time_bank_for_employees.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Authentication endpoint")
@RestController
@RequestMapping(value = "/auth")
public class AuthController implements AuthControllerDocs {

    private AuthService authService;

    public AuthController(AuthService userService){
        this.authService = userService;
    }

    @Override
    @PostMapping(value = "/signin")
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsDTO credentials){
        if (credentialsIsInvalid(credentials)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var token = authService.signin(credentials);
        if (token == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        return token;
    }

    @Override
    @PutMapping("/refresh/{username}")
    public ResponseEntity<?> refreshToken(
            @PathVariable("username") String username,
            @RequestHeader("Authorization") String refreshToken
    ){
        if (parameterAreInvalid(username, refreshToken)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var token = authService.refreshToken(username, refreshToken);
        if (token == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        return token;
    }

    private boolean credentialsIsInvalid(AccountCredentialsDTO credentials) {
        return credentials == null ||
                !StringUtils.hasText(credentials.getUsername()) ||
                !StringUtils.hasText(credentials.getPassword());
    }

    private boolean parameterAreInvalid(String username, String refreshToken) {
        return !StringUtils.hasText(username) || !StringUtils.hasText(refreshToken);
    }
}
