package com.diletant.diletantmediatest.service;

import com.diletant.diletantmediatest.DiletantMediaTestApplication;
import com.diletant.diletantmediatest.dto.LoginRequest;
import com.diletant.diletantmediatest.dto.RegisterRequest;
import com.diletant.diletantmediatest.entity.User;
import com.diletant.diletantmediatest.exception.DiletantMediaException;
import com.diletant.diletantmediatest.exception.ResourceNotFoundException;
import com.diletant.diletantmediatest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    RegisterRequest userJuniorSignup;

    LoginRequest userJuniorLogin;

    User userJunior;


    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);

        //given data for signup
        userJuniorSignup = new RegisterRequest();
        userJuniorSignup.setUserName("Junior Dos Santos");
        userJuniorSignup.setEmail("jds@gmail.com");
        userJuniorSignup.setPassword("cigano");

        //given data for login
        userJuniorLogin = new LoginRequest();
        userJuniorLogin.setUserName("Junior Dos Santos");
        userJuniorLogin.setPassword("cigano");

        //given user
        userJunior = new User();
        userJunior.setEnabled(false);
        userJunior.setUserName("Junior Dos Santos");
        userJunior.setEmail("jds@gmail.com");
        userJunior.setPassword("cigano");
    }


    @Test
    void signupSuccessful() {
        //given
        when(userRepository.findByEmail(userJuniorSignup.getEmail())).thenReturn(null);
        when(userRepository.save(any())).thenReturn(userJunior);

        User emptyUser = userRepository.findByEmail(userJuniorSignup.getEmail());

        User userJuniorRegister = new User();
        userJuniorRegister.setUserName("Junior Dos Santos");
        userJuniorRegister.setEmail("jds@gmail.com");
        userJuniorRegister.setPassword("cigano");
        userJunior.setEnabled(false);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        userRepository.save(userJuniorRegister);

        verify(userRepository).save(captor.capture());

        User capturedUser = captor.getValue();

        assertThat(emptyUser).isNull();
        assertThat(capturedUser.getUserName()).isEqualTo(userJuniorRegister.getUserName());
        assertThat(capturedUser.getEmail()).isEqualTo(userJuniorRegister.getEmail());
        assertThat(capturedUser.getPassword()).isEqualTo(userJuniorRegister.getPassword());
        assertThat(capturedUser.isEnabled()).isEqualTo(userJuniorRegister.isEnabled());
    }

    @Test
    void loginSuccessful() {
        when(userRepository.findByUserName(any())).thenReturn(Optional.of(userJunior));

        //mock authentification
        User userFound = userRepository.findByUserName(userJuniorLogin.getUserName()).orElseThrow(() -> new DiletantMediaException("User not found"));
        userFound.setEnabled(true);

        assertThat(userFound.getUserName()).isEqualTo(userJunior.getUserName());
        assertThat(userFound.getPassword()).isEqualTo(userJunior.getPassword());

        assertThat(userFound.isEnabled()).isEqualTo(true);

    }

    @Test
    void loginFailedUserNotFound() throws ResourceNotFoundException {
        when(userRepository.findByUserName(any())).thenThrow(new ResourceNotFoundException("User Not Found"));

        assertThrows(ResourceNotFoundException.class, ()-> {
            userRepository.findByUserName(userJuniorLogin.getUserName());
        });
    }

    @Test
    void loginFailedUserisNotActivated() throws DiletantMediaException {
        when(userRepository.findByUserName(any())).thenReturn(Optional.of(userJunior));

        User userFound = userRepository.findByUserName(userJuniorLogin.getUserName()).orElseThrow(() -> new DiletantMediaException("User not found"));
        userFound.setEnabled(false);

        assertThat(userFound.getUserName()).isEqualTo(userJunior.getUserName());
        assertThat(userFound.getPassword()).isEqualTo(userJunior.getPassword());

        assertThat(userFound.isEnabled()).isEqualTo(false);
    }


    @Test
    void verifyAccount() {
    }

    @Test
    void refreshToken() {
    }
}