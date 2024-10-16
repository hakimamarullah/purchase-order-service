package com.starline.purchase.order.utils;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/21/2024 11:55 PM
@Last Modified 6/21/2024 11:55 PM
Version 1.0
*/

import java.security.Principal;
import java.util.Optional;

public class AuthUtils {

    private AuthUtils() {}

    /**
     * Return -1 if the principal is null or the string principal.getName() does not match
     * this pattern "userId,username".
     * @param principal {@link Principal}
     * @return Long
     */
    public static Integer getCurrentUserId(Principal principal) {
        String userDetails = Optional.ofNullable(principal).map(Principal::getName).orElse("-1,");
        return getCurrentUserId(userDetails);
    }

    public static Integer getCurrentUserId(String subject) {
        return Integer.parseInt(subject.split(",")[0]);
    }

}
