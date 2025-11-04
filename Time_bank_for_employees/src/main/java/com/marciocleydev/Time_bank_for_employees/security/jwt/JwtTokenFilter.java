package com.marciocleydev.Time_bank_for_employees.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException {
        var token = jwtTokenProvider.resolveToken(request);//extrai o token da requisição from header Authorization: Bearer ...).
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            //converte o token em um objeto Authentication (usuário + authorities/roles).
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            if (authentication != null) {
                //se houver authentication válida, armazena-a no SecurityContext (ThreadLocal) para que Spring Security autorize/considere o usuário nas próximas etapas.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filter.doFilter(request, response);//prossegue com a cadeia de filtros/servlet (sempre deve ser chamado para que a requisição seja processada).
    }
}
