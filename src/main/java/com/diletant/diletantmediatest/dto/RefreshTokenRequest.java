package com.diletant.diletantmediatest.dto;

import javax.validation.constraints.NotBlank;

public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;
    private String userName;

    public RefreshTokenRequest(@NotBlank String refreshToken, String userName) {
        this.refreshToken = refreshToken;
        this.userName = userName;
    }

    public RefreshTokenRequest() {
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
