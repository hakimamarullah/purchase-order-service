package com.starline.purchase.order.controller;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/15/2024 10:47 PM
@Last Modified 10/15/2024 10:47 PM
Version 1.0
*/

import com.starline.purchase.order.config.constant.RoleConstant;
import com.starline.purchase.order.config.constant.Route;
import com.starline.purchase.order.dto.request.ResetPasswordRequest;
import com.starline.purchase.order.dto.response.ApiResponse;
import com.starline.purchase.order.exception.DataNotFoundException;
import com.starline.purchase.order.model.User;
import com.starline.purchase.order.service.AppUserService;
import com.starline.purchase.order.utils.AuthUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(Route.API_V1 + Route.USERS)
@SecurityRequirement(name = "bearerJWT")
public class UserController {

    private final AppUserService appUserService;

    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/{userId}")
    @RolesAllowed({RoleConstant.ADMIN})
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Integer userId) throws DataNotFoundException {
        ApiResponse<User> response = appUserService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @RolesAllowed({RoleConstant.ADMIN})
    public ResponseEntity<ApiResponse<PagedModel<User>>> getUsers(@PageableDefault() Pageable pageable) {
        ApiResponse<PagedModel<User>> response = appUserService.getUsers(pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    @RolesAllowed({RoleConstant.ADMIN})
    public ResponseEntity<ApiResponse<Object>> deleteUserById(@PathVariable Integer userId) {
        ApiResponse<Object> response = appUserService.deleteUserById(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.USER})
    public ResponseEntity<ApiResponse<User>> updateUser(@RequestBody ResetPasswordRequest request, Principal principal) throws DataNotFoundException {
        ApiResponse<User> response = appUserService.resetUserPassword(AuthUtils.getCurrentUserId(principal), request);
        return ResponseEntity.ok(response);
    }
}