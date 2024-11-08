package com.starline.purchase.order.controller;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/15/2024 10:10 PM
@Last Modified 10/15/2024 10:10 PM
Version 1.0
*/

import com.starline.purchase.order.config.constant.Route;
import com.starline.purchase.order.dto.request.LoginRequest;
import com.starline.purchase.order.dto.request.RegisterRequest;
import com.starline.purchase.order.dto.response.ApiResponse;
import com.starline.purchase.order.dto.response.LoginResponse;
import com.starline.purchase.order.dto.response.RegisterResponse;
import com.starline.purchase.order.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Route.API_V1 + Route.AUTH)
public class AuthController {

    private final AppUserService appUserService;

    @Autowired
    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping(Route.LOGIN)
    @Operation(summary = "Login user", description = "Login user with email and password")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        ApiResponse<LoginResponse> response = appUserService.login(request);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @PostMapping(Route.REGISTER)
    @Operation(summary = "Register user", description = "Register user with email and password and additional information")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request) {
        ApiResponse<RegisterResponse> response = appUserService.registerUser(request);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

}

