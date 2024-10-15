package com.starline.purchase.order.dto.request;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/21/2024 9:34 PM
@Last Modified 6/21/2024 9:34 PM
Version 1.0
*/

import com.starline.purchase.order.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RegisterRequest {

    @NotBlank(message = "firstName should not be blank")
    private String firstName;

    @NotBlank(message = "lastName should not be blank")
    private String lastName;

    @NotBlank(message = "email should not be blank")
    @Email(message = "email should be using valid format")
    private String email;

    @NotBlank(message = "phone should not be blank")
    @Pattern(message = "phone should be numerical characters", regexp = "^\\d+$")
    private String phone;

    @NotBlank(message = "password should be blank")
    @Size(message = "password should be 6 characters long at minimum", min = 6)
    private String password;

    @NotNull(message = "role should not be null or blank")
    private Role role;
}

