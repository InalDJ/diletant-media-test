package com.diletant.diletantmediatest.service;

import com.diletant.diletantmediatest.dto.AuthenticationResponse;
import com.diletant.diletantmediatest.dto.LoginRequest;
import com.diletant.diletantmediatest.dto.RefreshTokenRequest;
import com.diletant.diletantmediatest.dto.RegisterRequest;
import com.diletant.diletantmediatest.entity.NotificationEmail;
import com.diletant.diletantmediatest.entity.User;
import com.diletant.diletantmediatest.entity.VerificationToken;
import com.diletant.diletantmediatest.exception.DiletantMediaException;
import com.diletant.diletantmediatest.repository.UserRepository;
import com.diletant.diletantmediatest.repository.VerificationTokenRepository;
import com.diletant.diletantmediatest.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private VerificationTokenRepository verificationTokenRepository;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RefreshTokenService refreshTokenService;
    private MailService mailService;
    private MailContentBuilder mailContentBuilder;

    @Autowired
    public AuthService(VerificationTokenRepository verificationTokenRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserRepository userRepository, RefreshTokenService refreshTokenService, MailService mailService, MailContentBuilder mailContentBuilder) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.mailService = mailService;
        this.mailContentBuilder = mailContentBuilder;
    }



    public AuthenticationResponse login(LoginRequest loginRequest) throws DiletantMediaException {

        User userFound = userRepository.findByUserName(loginRequest.getUserName()).orElseThrow(() -> new DiletantMediaException("User not found"));

        if(userFound.isEnabled() == false){
            throw new DiletantMediaException("Your account has not been activated");
        }

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        } catch (Exception e){
            throw new DiletantMediaException("invalid username/password");
        }

        var authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setUserName(loginRequest.getUserName());
        authenticationResponse.setRefreshToken(refreshTokenService.generateRefreshToken().getToken());
        authenticationResponse.setAuthenticationToken(jwtUtil.generateToken(loginRequest.getUserName()));
        authenticationResponse.setUserName(loginRequest.getUserName());

        return authenticationResponse;
    }

    @Transactional
    public void signup(RegisterRequest registerRequest) throws DiletantMediaException {

        User existingUser = userRepository.findByEmail(registerRequest.getEmail());

        if(existingUser == null){
            User user = new User();
            user.setUserName(registerRequest.getUserName());
            user.setEmail(registerRequest.getEmail());
            user.setEnabled(false);

            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

            userRepository.save(user);

            //generates random token
            String token = generateVerificationToken(user);

            mailService.sendMail(new NotificationEmail("Благодарим за регистрацию! "
                    , user.getEmail(), " Пожалуйста, активируйте Ваш аккаунт перейдя по ссылке: http://diletant-media-test.tk:8080/api/auth/accountVerification/" + token));
        } else {
            throw new DiletantMediaException("User with the same email already exists");
        }

    }

    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new DiletantMediaException("Invalid Token"));

        fetchUserAndEnable(verificationToken);
    }

    public void fetchUserAndEnable(VerificationToken verificationToken){
        String username = verificationToken.getUser().getUserName();
        User user = userRepository.findByUserName(username).orElseThrow(()-> new DiletantMediaException("User Not Found"));

        user.setEnabled(true);
        userRepository.save(user);
    }


    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws DiletantMediaException {

        var authenticationResponse = new AuthenticationResponse();

        if(refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken())){
            String token = jwtUtil.generateToken(refreshTokenRequest.getUserName());

            authenticationResponse.setAuthenticationToken(token);
            authenticationResponse.setRefreshToken(refreshTokenRequest.getRefreshToken());
            authenticationResponse.setUserName(refreshTokenRequest.getUserName());
        } else {
            throw new DiletantMediaException("Refresh Token has expired");
        }

        return authenticationResponse;
    }




}
