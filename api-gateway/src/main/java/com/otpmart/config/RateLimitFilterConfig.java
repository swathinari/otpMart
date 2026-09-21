package com.otpmart.config;

import com.otpmart.filter.CorrelationIdFilter;
import com.otpmart.filter.RedisRateLimitFilter;
import com.otpmart.filter.RequestValidationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RateLimitFilterConfig {

    @Bean
    public FilterRegistrationBean<CorrelationIdFilter>
    correlationIdFilter() {

        FilterRegistrationBean<CorrelationIdFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(new CorrelationIdFilter());
        registration.addUrlPatterns("/api/*");

        // First filter
        registration.setOrder(Integer.MIN_VALUE);

        return registration;
    }

    @Bean
    public FilterRegistrationBean<RequestValidationFilter>
    requestValidationFilter() {

        FilterRegistrationBean<RequestValidationFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(new RequestValidationFilter());
        registration.addUrlPatterns("/api/*");

        // Second filter
        registration.setOrder(Integer.MIN_VALUE + 1);

        return registration;
    }

    @Bean
    public FilterRegistrationBean<RedisRateLimitFilter>
    rateLimitFilter(StringRedisTemplate redisTemplate) {

        RedisRateLimitFilter filter =
                new RedisRateLimitFilter(redisTemplate);

        FilterRegistrationBean<RedisRateLimitFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(filter);
        registration.addUrlPatterns("/api/*");

        // Third filter
        registration.setOrder(Integer.MIN_VALUE + 2);

        return registration;
    }
}