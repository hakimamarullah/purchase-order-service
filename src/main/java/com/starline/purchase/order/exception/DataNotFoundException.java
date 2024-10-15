package com.starline.purchase.order.exception;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/15/2024 10:51 PM
@Last Modified 10/15/2024 10:51 PM
Version 1.0
*/

public class DataNotFoundException extends RestApiException {

    public DataNotFoundException(String message) {
        super(message, 404);
    }
}
