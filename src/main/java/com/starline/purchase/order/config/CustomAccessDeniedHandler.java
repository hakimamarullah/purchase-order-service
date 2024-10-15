package com.starline.purchase.order.config;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 5/28/2024 10:10 PM
@Last Modified 5/28/2024 10:10 PM
Version 1.0
*/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starline.purchase.order.dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(ObjectMapper mapper) {
        this.objectMapper  = mapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ApiResponse<String> error = new ApiResponse<>();
        error.setMessage("You are not allowed to access this resource");
        error.setCode(HttpServletResponse.SC_FORBIDDEN);
        error.setData("Forbidden");
        OutputStream out = response.getOutputStream();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        objectMapper.writeValue(out, error);
        out.flush();
    }
}
