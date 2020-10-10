package com.diletant.diletantmediatest.controller;

import com.diletant.diletantmediatest.DiletantMediaTestApplication;
import com.diletant.diletantmediatest.dto.AuthenticationResponse;
import com.diletant.diletantmediatest.dto.LoginRequest;
import com.diletant.diletantmediatest.dto.RefreshTokenRequest;
import com.diletant.diletantmediatest.dto.RegisterRequest;
import com.diletant.diletantmediatest.filter.JwtFilter;
import com.diletant.diletantmediatest.service.AuthService;
import com.diletant.diletantmediatest.service.RefreshTokenService;
import com.diletant.diletantmediatest.service.UserDetailsServiceImpl;
import com.diletant.diletantmediatest.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    MockMvc mvc;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private AuthService authService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthController authController;

    private JacksonTester<LoginRequest> loginRequest;
    private JacksonTester<AuthenticationResponse> authenticationResponse;
    private JacksonTester<RegisterRequest> registerRequest;
    private JacksonTester<RefreshTokenRequest> refreshTokenRequest;

    String loginUri = "/api/auth/login";
    String signupUri = "/api/auth/signup";
    String logoutUri = "/api/auth/logout";
    String refreshTokenUrl = "/api/auth/refresh/token";

    @BeforeEach
    void setUp() throws JsonProcessingException {
    JacksonTester.initFields(this, new ObjectMapper());

    mvc = MockMvcBuilders.standaloneSetup(authController)
            .setControllerAdvice(new DiletantMediaTestApplication())
            .addFilters(new JwtFilter(jwtUtil, userDetailsService))
            .build();
    }

    @Test
    void signup() throws Exception {
        doNothing().when(authService).signup(any());

        MockHttpServletResponse response = mvc.perform(
                post(signupUri).contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest.write(new RegisterRequest("username", "email", "password")).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void login() throws Exception {

        when(authService.login(any())).thenReturn(new AuthenticationResponse(UUID.randomUUID().toString(), UUID.randomUUID().toString(),"username"));

        AuthenticationResponse login = authService.login(new LoginRequest("username", "password"));

        MockHttpServletResponse response = mvc.perform(
                post(loginUri).contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest.write(new LoginRequest("username", "password")).getJson())
        ).andReturn().getResponse();

        assertThat(login).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void refreshTokens() throws Exception {

        when(authService.refreshToken(any())).thenReturn(new AuthenticationResponse(UUID.randomUUID().toString(), UUID.randomUUID().toString(),"username"));

        AuthenticationResponse newLoginInfo = authService.refreshToken(new RefreshTokenRequest(UUID.randomUUID().toString(), "username"));

        MockHttpServletResponse response = mvc.perform(
                post(refreshTokenUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(refreshTokenRequest.write(new RefreshTokenRequest(UUID.randomUUID().toString(), "username")).getJson())
        ).andReturn().getResponse();

        assertThat(newLoginInfo).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void logout() throws Exception {
        doNothing().when(refreshTokenService).deleteRefreshToken(any());

        MockHttpServletResponse response = mvc.perform(
                post(logoutUri).contentType(MediaType.APPLICATION_JSON)
                        .content(refreshTokenRequest.write(new RefreshTokenRequest(UUID.randomUUID().toString(), "username")).getJson())
        ).andReturn().getResponse();

        refreshTokenService.deleteRefreshToken(UUID.randomUUID().toString());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}