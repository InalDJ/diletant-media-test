package com.diletant.diletantmediatest.controller;

import com.diletant.diletantmediatest.dto.AuthenticationResponse;
import com.diletant.diletantmediatest.dto.LoginRequest;
import com.diletant.diletantmediatest.dto.RefreshTokenRequest;
import com.diletant.diletantmediatest.dto.RegisterRequest;
import com.diletant.diletantmediatest.exception.DiletantMediaException;
import com.diletant.diletantmediatest.service.AuthService;
import com.diletant.diletantmediatest.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    private RefreshTokenService refreshTokenService;


    @Autowired
    public AuthController(AuthService authService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) throws DiletantMediaException {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token, HttpServletResponse response) throws IOException {
        authService.verifyAccount(token);
        response.sendRedirect("http://diletant-media-test.tk/message");
        return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) throws DiletantMediaException {
        AuthenticationResponse login = authService.login(loginRequest);
        System.out.println("Refresh token generated during login: "+login.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body(login);
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<AuthenticationResponse> refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) throws DiletantMediaException {
        return ResponseEntity.status(HttpStatus.OK).body(authService.refreshToken(refreshTokenRequest));
    }

    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully!!");
    }
}
