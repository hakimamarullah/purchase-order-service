package com.starline.purchase.order.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.starline.purchase.order.config.properties.AppProperties;
import com.starline.purchase.order.dto.request.LoginRequest;
import com.starline.purchase.order.dto.request.RegisterRequest;
import com.starline.purchase.order.dto.request.ResetPasswordRequest;
import com.starline.purchase.order.dto.response.ApiResponse;
import com.starline.purchase.order.dto.response.LoginResponse;
import com.starline.purchase.order.dto.response.RegisterResponse;
import com.starline.purchase.order.exception.DataNotFoundException;
import com.starline.purchase.order.model.User;
import com.starline.purchase.order.model.enums.Role;
import com.starline.purchase.order.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class AppUserServiceTest {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationProvider authenticationProvider;
    @Mock
    private JwtEncoder jwtEncoder;
    @Mock
    private AppProperties appProperties;

    @InjectMocks
    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        appUserService = new AppUserService(userRepository, passwordEncoder, authenticationProvider, jwtEncoder, mapper, appProperties);
    }

    @Test
    void whenLoginThenShouldReturnAccessTokenAndStatusCodeIs200() {
        // Prepare parameter
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("unittest@gmail.com");
        loginRequest.setPassword("unittest");

        // Mock authprovider
        Authentication authentication = Mockito.mock(Authentication.class);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.name()));
        doReturn(authorities).when(authentication).getAuthorities();
        doReturn(authentication).when(authenticationProvider).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));

        // Mock principal
        User user = new User();
        user.setId(1);
        user.setEmail("unittest@gmail.com");
        doReturn(user).when(authentication).getPrincipal();

        // Mock AppProperties
        doReturn("unittest").when(appProperties).getJWT_ISSUER();
        doReturn(3600L).when(appProperties).getJWT_TOKEN_AGE();

        // Mock JwtEncoder
        Jwt jwt = Mockito.mock(Jwt.class);
        doReturn("unittest").when(jwt).getTokenValue();
        doReturn(jwt).when(jwtEncoder).encode(Mockito.any());


        // Execute
        ApiResponse<LoginResponse> result = appUserService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals("unittest", result.getData().getAccessToken());
        assertEquals("Login Successful", result.getMessage());
        assertEquals(200, result.getCode());

        verify(authenticationProvider).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
    }


    @Test
    void whenBadCredentialsExceptionOccursThenStatusCodeIs401() {
        // Prepare parameter
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("unittest@gmail.com");
        loginRequest.setPassword("unittest");

        // Mock authprovider
        doThrow(new BadCredentialsException("wrong password")).when(authenticationProvider).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));


        // Execute
        ApiResponse<LoginResponse> result = appUserService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertNull(result.getData());
        assertEquals("wrong password", result.getMessage());
        assertEquals(401, result.getCode());

        verify(authenticationProvider).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void whenDisabledExceptionOccursThenStatusCodeIs401() {
        // Prepare parameter
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("unittest@gmail.com");
        loginRequest.setPassword("unittest");

        // Mock authprovider
        doThrow(new DisabledException("user inactive")).when(authenticationProvider).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));


        // Execute
        ApiResponse<LoginResponse> result = appUserService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertNull(result.getData());
        assertEquals("user inactive", result.getMessage());
        assertEquals(401, result.getCode());

        verify(authenticationProvider).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void whenRegisterUserThenSuccess() {
        // Prepare parameter
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("unittest@gmail.com");
        registerRequest.setPassword("unittest");
        registerRequest.setPhone("081234567890");

        // Mock user repo
        doReturn(Optional.empty()).when(userRepository).findFirstByEmail(anyString());
        doReturn(new User()).when(userRepository).save(any(User.class));


        // Execute
        ApiResponse<RegisterResponse> result = appUserService.registerUser(registerRequest);
        RegisterResponse registerResponse = result.getData();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(201, result.getCode());
        assertEquals("success register user", result.getMessage());
        assertEquals("6281234567890", registerResponse.getPhone());

        verify(userRepository).findFirstByEmail(anyString());
        verify(userRepository).save(any(User.class));


    }

    @Test
    void whenRegisterUserWithUsedEmailThenStatusCodeShouldBe409() {
        // Prepare parameter
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("unittest@gmail.com");
        registerRequest.setPassword("unittest");
        registerRequest.setPhone("081234567890");

        // Mock user repo
        User user = new User();
        user.setEnabled(true);
        user.setEmail("unittest@gmail.com");
        doReturn(Optional.of(user)).when(userRepository).findFirstByEmail(anyString());
        doReturn(new User()).when(userRepository).save(any(User.class));


        // Execute
        ApiResponse<RegisterResponse> result = appUserService.registerUser(registerRequest);


        // Assert
        assertNotNull(result);
        assertNull(result.getData());
        assertEquals(409, result.getCode());
        assertEquals("email already used", result.getMessage());


        verify(userRepository).findFirstByEmail(anyString());

    }

    @Test
    void whenGetUserByIdThenShouldReturnSingleRecord() throws DataNotFoundException {
        // Mock data
        User user = new User();
        user.setId(1);
        user.setEmail("unittest@gmail.com");

        // Mock user repo
        doReturn(Optional.of(user)).when(userRepository).findById(anyInt());

        // Action
        ApiResponse<User> result = appUserService.getUserById(1);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().getId());
        assertEquals("unittest@gmail.com", result.getData().getEmail());

        verify(userRepository).findById(anyInt());
    }

    @Test
    void whenGetUserByIdDoesNotExistThenShouldThrowDataNotFoundException() {
        // Mock user repo
        doReturn(Optional.empty()).when(userRepository).findById(anyInt());

        // Action
        DataNotFoundException result = assertThrows(DataNotFoundException.class, () -> appUserService.getUserById(1));

        // Assert
        assertEquals("user not found", result.getMessage());
        verify(userRepository).findById(anyInt());
    }

    @Test
    void whenGetAllUserThenShouldReturnPaginatedRecords() {
        // Mock data
        User user = new User();
        user.setId(1);
        user.setEmail("unittest@gmail.com");

        // Mock user repo
        doReturn(new PageImpl<>(List.of(user))).when(userRepository).findAll(any(Pageable.class));

        // Action
        ApiResponse<PagedModel<User>> result = appUserService.getUsers(PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("Success", result.getMessage());
        assertEquals(1, result.getData().getContent().size());
        assertEquals(1, result.getData().getContent().getFirst().getId());
        assertEquals("unittest@gmail.com", result.getData().getContent().getFirst().getEmail());
    }

    @Test
    void whenDeleteUserByIdThenSuccess() {
        // Mock user repo
        doReturn(1).when(userRepository).deleteUserById(anyInt());

        // Action
        ApiResponse<Object> result = appUserService.deleteUserById(1);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(1, result.getData());
        assertEquals("1 user deleted", result.getMessage());
    }

    @Test
    void whenResetUserPasswordThenSuccess() throws DataNotFoundException {
        // Prepare parameter
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setPassword("unittest");

        // Mock user repo
        User user = new User();
        user.setId(1);
        user.setEmail("unittest@gmail.com");
        doReturn(Optional.of(user)).when(userRepository).findById(anyInt());
        doReturn(user).when(userRepository).save(any(User.class));

        // Action
        ApiResponse<User> result = appUserService.resetUserPassword(1, resetPasswordRequest);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(1, result.getData().getId());
        assertEquals("password has been reset successfully", result.getMessage());

        verify(userRepository).findById(anyInt());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void whenResetPasswordAndUserDoesNotExistThenShouldThrowDataNotFoundException() {
        // Prepare parameter
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setPassword("unittest");

        // Mock user repo
        doReturn(Optional.empty()).when(userRepository).findById(anyInt());


        // Action
        DataNotFoundException result = assertThrows(DataNotFoundException.class, () -> appUserService.resetUserPassword(1, resetPasswordRequest));

        // Assert
        assertEquals("user not found", result.getMessage());

        verify(userRepository).findById(anyInt());
    }
}