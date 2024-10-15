package com.starline.purchase.order.dto.request;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/15/2024 11:43 PM
@Last Modified 10/15/2024 11:43 PM
Version 1.0
*/

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "password should be blank")
    @Size(message = "password should be 6 characters long at minimum", min = 6)
    private String password;


}
