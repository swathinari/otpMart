package com.otp_mart.otp_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GenerateOtpResponse {
    private String message;
    private long expiresIn;
    private String developmentOtp;

}
