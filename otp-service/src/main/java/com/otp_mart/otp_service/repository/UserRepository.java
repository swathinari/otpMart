package com.otp_mart.otp_service.repository;

import com.otp_mart.otp_service.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByMobileNumber(String mobileNumber);

//    boolean existsByMobileNumber(String mobileNumber);
}
