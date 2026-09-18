//package com.otp_mart.otp_service.service;
//
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SmsService {
//
//    @Value("${twilio.account-sid}")
//    private String accountSid;
//
//    @Value("${twilio.auth-token}")
//    private String authToken;
//
//    @Value("${twilio.phone-number}")
//    private String fromNumber;
//
//    public void sendOtp(String mobileNumber, String otp) {
//
//        Twilio.init(accountSid, authToken);
//
//        String destinationNumber = "+91" + mobileNumber;
//
//        Message.creator(
//                new PhoneNumber(destinationNumber),
//                new PhoneNumber(fromNumber),
//                "Your OTP is " + otp +
//                        ". It is valid for 5 minutes."
//        ).create();
//    }
//}