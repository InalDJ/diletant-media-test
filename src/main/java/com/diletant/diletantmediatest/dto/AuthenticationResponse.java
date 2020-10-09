package com.diletant.diletantmediatest.dto;

public class AuthenticationResponse {

    private String authenticationToken;
    private String refreshToken;
    private String userName;

    public AuthenticationResponse(String authenticationToken, String refreshToken, String userName) {
        this.authenticationToken = authenticationToken;
        this.refreshToken = refreshToken;
        this.userName = userName;
    }

    public AuthenticationResponse() {
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
