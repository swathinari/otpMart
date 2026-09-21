package com.otpmart.service;

import com.otpmart.dto.RegisterRequest;
import com.otpmart.entity.User;
import com.otpmart.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByMobile(request.getMobile())) {
            throw new RuntimeException("Mobile number already registered");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setMobile(request.getMobile());
        user.setRole(
                request.getRole() == null || request.getRole().isBlank()
                        ? "USER"
                        : request.getRole()
        );

        return userRepository.save(user);
    }
}
