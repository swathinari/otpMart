package com.otp_mart.otp_service.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VerifyOtpResponse {
    private boolean verified;
    private String message;

    public boolean isVerified() {
        return verified;
    }

    public String getMessage() {
        return message;
    }
}
