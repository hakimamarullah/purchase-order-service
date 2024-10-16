package com.starline.purchase.order.service;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/21/2024 10:44 PM
@Last Modified 6/21/2024 10:44 PM
Version 1.0
*/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starline.purchase.order.config.properties.AppProperties;
import com.starline.purchase.order.dto.request.LoginRequest;
import com.starline.purchase.order.dto.request.RegisterRequest;
import com.starline.purchase.order.dto.request.ResetPasswordRequest;
import com.starline.purchase.order.dto.response.ApiResponse;
import com.starline.purchase.order.dto.response.LoginResponse;
import com.starline.purchase.order.dto.response.RegisterResponse;
import com.starline.purchase.order.exception.DataNotFoundException;
import com.starline.purchase.order.model.User;
import com.starline.purchase.order.repository.UserRepository;
import com.starline.purchase.order.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.web.PagedModel;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final AuthenticationProvider authenticationProvider;

    private final JwtEncoder jwtEncoder;


    private final ObjectMapper mapper;

    private final AppProperties appProperties;


    @Autowired
    public AppUserService(UserRepository userRepository,
                          BCryptPasswordEncoder passwordEncoder,
                          AuthenticationProvider authenticationProvider,
                          JwtEncoder jwtEncoder,
                          ObjectMapper mapper,
                          AppProperties appProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.jwtEncoder = jwtEncoder;
        this.mapper = mapper;
        this.appProperties = appProperties;
    }


    public ApiResponse<LoginResponse> login(LoginRequest loginRequest) {
        Authentication authentication;

        try {
            authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (BadCredentialsException | DisabledException ex) {
            return ApiResponse.<LoginResponse>builder()
                    .code(401)
                    .message(ex.getMessage())
                    .build();
        }

        User user = (User) authentication.getPrincipal();

        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claim = JwtClaimsSet.builder()
                .issuer(appProperties.getJWT_ISSUER())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(appProperties.getJWT_TOKEN_AGE()))
                .subject(String.format("%s,%s", user.getId(), user.getUsername()))
                .claim("roles", scope)
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claim)).getTokenValue();

        LoginResponse loginResponse = mapper.convertValue(user, LoginResponse.class);
        loginResponse.setAccessToken(token);
        return ApiResponse.setSuccess(loginResponse, "Login Successful");
    }

    @Transactional
    public ApiResponse<RegisterResponse> registerUser(RegisterRequest request) {
        request.setPhone(CommonUtils.formatPhoneNumber(request.getPhone()));

        Optional<User> userDB = userRepository.findFirstByEmail(request.getEmail());
        ApiResponse<RegisterResponse> response = ApiResponse.setSuccess();

        if (userDB.isPresent() && userDB.get().isEnabled()) {
            response.setCode(409);
            response.setMessage("email already used");
            return response;
        }

        User newUser = mapper.convertValue(request, User.class);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(newUser);


        RegisterResponse registerResponse = mapper.convertValue(newUser, RegisterResponse.class);
        response.setMessage("success register user");
        response.setCode(201);
        response.setData(registerResponse);
        return response;
    }

    public ApiResponse<User> getUserById(Integer userId) throws DataNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("user not found"));
        return ApiResponse.setSuccess(user);
    }

    public ApiResponse<PagedModel<User>> getUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return ApiResponse.setSuccess(new PagedModel<>(users));
    }

    @Transactional
    @Modifying
    public ApiResponse<Object> deleteUserById(Integer userId) {
        int count = userRepository.deleteUserById(userId);
        return ApiResponse.setSuccess(count, String.format("%d user deleted", count));
    }

    @Transactional
    @Modifying
    public ApiResponse<User> resetUserPassword(Integer id, ResetPasswordRequest request) throws DataNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("user not found"));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return ApiResponse.setSuccess(user, "password has been reset successfully");
    }

}
