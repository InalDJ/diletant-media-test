package com.diletant.diletantmediatest.repository;

import com.diletant.diletantmediatest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String username);

    @Nullable
    User findByEmail (String email);
}
