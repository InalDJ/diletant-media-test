package com.diletant.diletantmediatest.service;

import com.diletant.diletantmediatest.entity.RefreshToken;
import com.diletant.diletantmediatest.exception.DiletantMediaException;
import com.diletant.diletantmediatest.exception.ResourceNotFoundException;
import com.diletant.diletantmediatest.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }


    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    boolean validateRefreshToken(String token){

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("The Refresh Token hasn't been found"));


        long dateCreated = refreshToken.getCreatedDate().toEpochMilli();

        //it's supposed to remain valid for 5-6 days
        if(Instant.now().toEpochMilli() - dateCreated > 50000 * 10000){
            return false;
        }
        return true;
    }
    @Transactional
    public void deleteRefreshToken(String token){
       refreshTokenRepository.deleteByToken(token);

    }
}
