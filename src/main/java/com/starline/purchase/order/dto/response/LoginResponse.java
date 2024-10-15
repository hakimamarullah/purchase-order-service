package com.starline.purchase.order.dto.response;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/21/2024 9:47 PM
@Last Modified 6/21/2024 9:47 PM
Version 1.0
*/

import com.starline.purchase.order.model.enums.Role;
import lombok.Data;


@Data
public class LoginResponse {

    private String username;
    private String accessToken;
    private Role role;
    private Boolean enabled;
}
