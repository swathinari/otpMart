package com.otp_mart.otp_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerifyOtpRequest {
    @NotBlank
    @Pattern(regexp = "^[6-9][0-9]{9}$")
    private String mobileNumber;

    @NotBlank
    @Pattern(
            regexp = "^[0-9]{6}$",
            message = "OTP must contain 6 digits"
    )
    private String otp;
}
