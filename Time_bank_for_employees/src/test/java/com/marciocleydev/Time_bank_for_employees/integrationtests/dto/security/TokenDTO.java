package com.marciocleydev.Time_bank_for_employees.integrationtests.dto.security;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@XmlRootElement(name = "TokenDTO")
public class TokenDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private Boolean authenticated;
    private Date createDate;
    private Date expirationDate;
    private String accessToken;
    private String refreshToken;

    public TokenDTO() {
    }

    public TokenDTO(String username, Boolean authenticated, Date createDate, Date expirationDate, String accessToken, String refreshToken) {
        this.username = username;
        this.authenticated = authenticated;
        this.createDate = createDate;
        this.expirationDate = expirationDate;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TokenDTO tokenDTO = (TokenDTO) o;
        return Objects.equals(username, tokenDTO.username) && Objects.equals(authenticated, tokenDTO.authenticated) && Objects.equals(createDate, tokenDTO.createDate) && Objects.equals(expirationDate, tokenDTO.expirationDate) && Objects.equals(accessToken, tokenDTO.accessToken) && Objects.equals(refreshToken, tokenDTO.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, authenticated, createDate, expirationDate, accessToken, refreshToken);
    }
}
