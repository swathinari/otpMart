package com.otpmart.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RequestValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String method = request.getMethod();
        String path = request.getRequestURI();

        // Allow CORS preflight requests
        if ("OPTIONS".equalsIgnoreCase(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Only validate API requests
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Allow only supported HTTP methods
        if (!isAllowedMethod(method)) {

            response.setStatus(405);
            response.setContentType("application/json");

            response.setHeader(
                    "Allow",
                    "GET, POST, PUT, PATCH, DELETE, OPTIONS"
            );

            response.getWriter().write("""
                    {
                      "status": 405,
                      "error": "Method Not Allowed",
                      "message": "HTTP method is not supported for this API."
                    }
                    """);

            return;
        }

        // POST, PUT and PATCH normally contain JSON request bodies
        if (requiresJsonContentType(method)) {

            String contentType = request.getContentType();

            if (contentType == null ||
                    !contentType.toLowerCase().startsWith("application/json")) {

                response.setStatus(415);
                response.setContentType("application/json");

                response.getWriter().write("""
                        {
                          "status": 415,
                          "error": "Unsupported Media Type",
                          "message": "Content-Type must be application/json."
                        }
                        """);

                return;
            }
        }

        // Basic request-size validation
        long contentLength = request.getContentLengthLong();

        if (contentLength > 1024 * 1024) {

            response.setStatus(413);
            response.setContentType("application/json");

            response.getWriter().write("""
                    {
                      "status": 413,
                      "error": "Payload Too Large",
                      "message": "Request payload must not exceed 1 MB."
                    }
                    """);

            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAllowedMethod(String method) {

        return "GET".equalsIgnoreCase(method)
                || "POST".equalsIgnoreCase(method)
                || "PUT".equalsIgnoreCase(method)
                || "PATCH".equalsIgnoreCase(method)
                || "DELETE".equalsIgnoreCase(method);
    }

    private boolean requiresJsonContentType(String method) {

        return "POST".equalsIgnoreCase(method)
                || "PUT".equalsIgnoreCase(method)
                || "PATCH".equalsIgnoreCase(method);
    }
}