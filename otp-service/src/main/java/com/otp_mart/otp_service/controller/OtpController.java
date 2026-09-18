package com.otp_mart.otp_service.controller;

import com.otp_mart.otp_service.dto.GenerateOtpRequest;
import com.otp_mart.otp_service.dto.GenerateOtpResponse;
import com.otp_mart.otp_service.dto.VerifyOtpRequest;
import com.otp_mart.otp_service.dto.VerifyOtpResponse;
import com.otp_mart.otp_service.service.OtpService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/otp")
public class OtpController {
    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/generate")
    public ResponseEntity<GenerateOtpResponse> generateOtp(
            @Valid @RequestBody GenerateOtpRequest request) {

        GenerateOtpResponse response =
                otpService.generateOtp(
                        request.getMobileNumber());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyOtpResponse> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        VerifyOtpResponse response =
                otpService.verifyOtp(
                        request.getMobileNumber(),
                        request.getOtp());

        return ResponseEntity.ok(response);
    }
}
