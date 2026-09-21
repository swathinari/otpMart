package com.otpmart.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(
            String userId,
            String username,
            String role) {

        try {

            Instant now = Instant.now();

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(username)
                    .claim("userId", userId)
                    .claim("role", role)
                    .issueTime(java.util.Date.from(now))
                    .expirationTime(
                            java.util.Date.from(
                                    now.plus(1, ChronoUnit.HOURS)
                            )
                    )
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claims
            );

            byte[] secret =
                    jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8);

            signedJWT.sign(new MACSigner(secret));

            return signedJWT.serialize();

        } catch (JOSEException e) {
            throw new RuntimeException(
                    "Unable to generate JWT token",
                    e
            );
        }
    }
}