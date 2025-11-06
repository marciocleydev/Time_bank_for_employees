package com.marciocleydev.Time_bank_for_employees.services;

import com.marciocleydev.Time_bank_for_employees.DTO.security.AccountCredentialsDTO;
import com.marciocleydev.Time_bank_for_employees.DTO.security.TokenDTO;
import com.marciocleydev.Time_bank_for_employees.repositories.UserRepository;
import com.marciocleydev.Time_bank_for_employees.security.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    public ResponseEntity<TokenDTO> signin(AccountCredentialsDTO credentials){
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword()
                    )
            );

        var user = userRepository.findByUsername(credentials.getUsername());
        if(user == null){
            throw new UsernameNotFoundException("User: " + credentials.getUsername() + " not found!");
        }

        var token = tokenProvider.createAccessToken(
                credentials.getUsername(),
                user.getRoles()
        );
        return ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken){
        var user = userRepository.findByUsername(username);
        TokenDTO token;
        if(user != null){
            token = tokenProvider.refreshToken(username, refreshToken);
        }else{
            throw new UsernameNotFoundException("User: " + username + " not found!");
        }
        return ResponseEntity.ok(token);
    }
}
