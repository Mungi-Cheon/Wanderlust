package com.travel.domain.user.repository;

import com.travel.domain.user.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
