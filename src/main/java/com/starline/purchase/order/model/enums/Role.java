package com.starline.purchase.order.model.enums;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/15/2024 8:28 PM
@Last Modified 10/15/2024 8:28 PM
Version 1.0
*/

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    USER, ADMIN;
    @JsonCreator
    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.name().equals(value)) {
                return role;
            }
        }
        return null;
    }

}
