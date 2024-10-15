package com.starline.purchase.order.config;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 5/28/2024 10:09 PM
@Last Modified 5/28/2024 10:09 PM
Version 1.0
*/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starline.purchase.order.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.objectMapper = mapper;
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ApiResponse<String> error = new ApiResponse<>();
        error.setMessage("You are not authenticated, please login to get access token");
        error.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        error.setData("Unauthorized");
        OutputStream out = response.getOutputStream();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        objectMapper.writeValue(out, error);
        out.flush();
    }
}
