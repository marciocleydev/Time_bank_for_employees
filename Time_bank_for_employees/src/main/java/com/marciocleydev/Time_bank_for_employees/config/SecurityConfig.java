package com.marciocleydev.Time_bank_for_employees.config;

import com.marciocleydev.Time_bank_for_employees.security.jwt.JwtTokenFilter;
import com.marciocleydev.Time_bank_for_employees.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    JwtTokenProvider tokenProvider;

    public SecurityConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // esse method cria o PasswordEncoder que o Spring Security usará para gerar e verificar senhas armazenadas
    //senhas serão criptografadas usando PBKDF2.
    @Bean
    PasswordEncoder passwordEncoder() {
        //define como a senha será criptografada.
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
                "", 8, 185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        //Cria um map para registrar esse encoder com o nome "pbkdf2"
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);

        //Quando ele encontrar uma senha salva no banco com prefixo {pbkdf2}, ele sabe qual encoder usar.
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

        //“Se a senha não tiver prefixo, use PBKDF2 por padrão.”
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder;
    }

    //Isso expõe o AuthenticationManager como um bean no contexto do Spring.
    //Ele é necessário para que você consiga autenticar usuários manualmente, por exemplo no endpoint /auth/signin.
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    //instancia meu filtro que vai validar o JWT antes da requisição chegar ao controller.
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenFilter filter = new JwtTokenFilter(tokenProvider);
        return http
                .httpBasic(AbstractHttpConfigurer::disable)//Desativa autenticação HTTP básica (usuário/senha via popup).
                .csrf(AbstractHttpConfigurer::disable)//Desativa proteção CSRF, pois APIs REST stateless não precisam disso.
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)//Isso coloca o filtro antes do filtro padrão, toda request vai passar pelo filtro primeiro
                .sessionManagement(//Define que nenhuma sessão será criada, Cada requisição deve enviar o JWT — não existe login persistido no servidor.
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(
                   //Esses endpoints podem ser acessados sem autenticação.
                                        "/auth/signin",
                                        "/auth/refresh",
                                        "/auth/createUser",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**")
                                .permitAll()
                                .requestMatchers("/**").authenticated()//todoh o resto precisa de autenticação
                                .requestMatchers("/users").denyAll()//está totalmente bloqueado, mesmo se autenticado.
                                .anyRequest().authenticated()
                )
                .cors(cors -> {})//permite que outra config de CORS seja aplicada
                .build(); //constroi o SecurityFilterChain que será usado pelo Spring Security.
    }
}
