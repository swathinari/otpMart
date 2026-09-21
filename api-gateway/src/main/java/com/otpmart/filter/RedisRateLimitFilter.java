package com.otpmart.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
public class RedisRateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS = 5;
    private static final Duration WINDOW = Duration.ofMinutes(1);

    private final StringRedisTemplate redisTemplate;

    public RedisRateLimitFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Don't rate-limit monitoring/documentation endpoints
        if (path.startsWith("/actuator")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")) {

            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = getClientIp(request);

        String redisKey = "gateway:rate-limit:" + clientIp;

        Long requestCount = redisTemplate.opsForValue().increment(redisKey);

        if (requestCount != null && requestCount == 1) {
            redisTemplate.expire(redisKey, WINDOW);
        }

        if (requestCount != null && requestCount > MAX_REQUESTS) {

            response.setStatus(429);
            response.setContentType("application/json");

            response.getWriter().write("""
                    {
                      "status": 429,
                      "error": "Too Many Requests",
                      "message": "Rate limit exceeded. Try again later."
                    }
                    """);

            return;
        }

        response.setHeader(
                "X-RateLimit-Limit",
                String.valueOf(MAX_REQUESTS)
        );

        response.setHeader(
                "X-RateLimit-Remaining",
                String.valueOf(
                        Math.max(
                                0,
                                MAX_REQUESTS - requestCount
                        )
                )
        );

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {

        String forwardedFor = request.getHeader("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}