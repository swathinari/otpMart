package com.otp_mart.otp_service.service;

import com.otp_mart.otp_service.dto.GenerateOtpResponse;
import com.otp_mart.otp_service.dto.VerifyOtpResponse;
import com.otp_mart.otp_service.entity.UserEntity;
import com.otp_mart.otp_service.repository.UserRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class OtpService {

    private static final int OTP_EXPIRATION_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 3;

    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;

    private final SecureRandom secureRandom =
            new SecureRandom();

    public OtpService(
            UserRepository userRepository,
            StringRedisTemplate redisTemplate) {

        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    public GenerateOtpResponse generateOtp(
            String mobileNumber) {

        // Check DB.
        // Create user when mobile doesn't exist.
        UserEntity user = userRepository
                .findByMobileNumber(mobileNumber)
                .orElseGet(() -> {

                    UserEntity newUser = new UserEntity();

                    newUser.setMobileNumber(mobileNumber);
                    newUser.setVerified(false);
                    newUser.setCreatedAt(
                            LocalDateTime.now()
                    );

                    return userRepository.save(newUser);
                });

        String resendKey =
                "otp:resend:" + mobileNumber;

        if (Boolean.TRUE.equals(
                redisTemplate.hasKey(resendKey))) {

            throw new IllegalStateException(
                    "Please wait 60 seconds before requesting another OTP"
            );
        }

        String otp = generateSixDigitOtp();

        String otpKey =
                "otp:" + mobileNumber;

        String attemptKey =
                "otp:attempt:" + mobileNumber;

        redisTemplate.opsForValue().set(
                otpKey,
                otp,
                Duration.ofMinutes(
                        OTP_EXPIRATION_MINUTES)
        );

        redisTemplate.opsForValue().set(
                attemptKey,
                "0",
                Duration.ofMinutes(
                        OTP_EXPIRATION_MINUTES)
        );

        redisTemplate.opsForValue().set(
                resendKey,
                "1",
                Duration.ofSeconds(60)
        );

        return new GenerateOtpResponse(
                "OTP generated successfully",
                300,
                otp
        );
    }

    private String generateSixDigitOtp() {

        int number =
                secureRandom.nextInt(900000)
                        + 100000;

        return String.valueOf(number);
    }

    public VerifyOtpResponse verifyOtp(
            String mobileNumber,
            String enteredOtp) {

        UserEntity user = userRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Mobile number not found"
                        ));

        String otpKey =
                "otp:" + mobileNumber;

        String attemptKey =
                "otp:attempt:" + mobileNumber;

        String storedOtp =
                redisTemplate
                        .opsForValue()
                        .get(otpKey);

        // OTP doesn't exist anymore
        if (storedOtp == null) {

            return new VerifyOtpResponse(
                    false,
                    "OTP expired or not found"
            );
        }

        String attemptValue =
                redisTemplate
                        .opsForValue()
                        .get(attemptKey);

        int attempts =
                attemptValue == null
                        ? 0
                        : Integer.parseInt(
                        attemptValue);

        if (attempts >= MAX_ATTEMPTS) {

            redisTemplate.delete(otpKey);
            redisTemplate.delete(attemptKey);

            return new VerifyOtpResponse(
                    false,
                    "Maximum OTP attempts exceeded"
            );
        }

        // Wrong OTP
        if (!storedOtp.equals(enteredOtp)) {

            Long updatedAttempts =
                    redisTemplate
                            .opsForValue()
                            .increment(attemptKey);

            int remaining =
                    MAX_ATTEMPTS
                            - updatedAttempts.intValue();

            if (remaining <= 0) {

                redisTemplate.delete(otpKey);
                redisTemplate.delete(attemptKey);

                return new VerifyOtpResponse(
                        false,
                        "Maximum OTP attempts exceeded"
                );
            }

            return new VerifyOtpResponse(
                    false,
                    "Invalid OTP. Remaining attempts: "
                            + remaining
            );
        }

        // Correct OTP
        user.setVerified(true);
        user.setVerifiedAt(
                LocalDateTime.now());

        userRepository.save(user);

        // OTP is one-time use
        redisTemplate.delete(otpKey);
        redisTemplate.delete(attemptKey);

        return new VerifyOtpResponse(
                true,
                "Mobile number verified successfully"
        );
    }
}