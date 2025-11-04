package com.marciocleydev.Time_bank_for_employees.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.marciocleydev.Time_bank_for_employees.DTO.security.TokenDTO;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
// instrui o Spring a injetar o valor da propriedade security.jwt.token.secret-key, Se a propriedade não existir, usa o fallback "secret".
    private String secretKey = "secret"; //declara o campo e já o inicializa com "secret" como valor padrão.

    @Value("${security.jwt.token.expire-length:3600000}")
    private Long validityInMilliseconds = 3600000L; //1h

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    private String issuer;

    @PostConstruct //method executado uma única vez depois que a bean é criada e as dependências foram injetadas.
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes()); //codifica os bytes da chave em Base64) e reatribui ao campo.
        algorithm = Algorithm.HMAC256(secretKey);//cria a instância do algoritmo HMAC-SHA256 (da biblioteca Auth0 JWT) usando essa chave codificada, para assinar/verificar tokens JWT.

        //O issuerUrl é o identificador da aplicação que gerou o token (o emissor), É uma assinatura de “quem emitiu o token”.
        issuer = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();
    }

    public TokenDTO createAccessToken(String username, List<String> roles) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        String accessToken = getAccessToken(username, roles, now, validity);
        String refreshToken = getRefreshToken(username, roles, now);
        return new TokenDTO(username, true, now, validity, accessToken, refreshToken);
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        Date refreshTokenVality = new Date(now.getTime() + validityInMilliseconds * 3);//( validityInMilliseconds * 3) = 3 horas
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(refreshTokenVality)
                .withSubject(username)
                .sign(algorithm);
    }

    private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)//Define a data/hora de emissão (claim "iat").
                .withExpiresAt(validity)//Define a data/hora de expiração (claim "exp").
                .withSubject(username)//Define o sujeito (claim "sub"), aqui o nome/identificador do usuário.
                .withIssuer(issuer)//Define o emissor (claim "iss") com a URL criada
                .sign(algorithm);//Assina o token com o algoritmo HMAC256 .
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //O method decodifica e verifica a assinatura do JWT recebido e retorna o token decodificado
    private DecodedJWT decodedToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();//Constrói um verificador que valida a assinatura (pode também checar issuer, subject etc. se configurado).
        DecodedJWT decodedJWT = verifier.verify(token);//Verifica o token: valida assinatura e claims padrão (por exemplo exp). Se inválido ou expirado, lança JWTVerificationException.
        return decodedJWT;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        //Bear eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30
        if (StringUtils.hasLength(bearerToken) && bearerToken.startsWith("Bearer ")) { //StringUtils.hasLength = true se não for null e tiver pelo menos 1 caractere (mesmo que espaço)  / StringUtils.hasText(str) → true se não for null, não for vazio e não for só espaço.
            return bearerToken.substring("Bearer ".length()); //remove o "Bearer " do token
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            decodedToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
