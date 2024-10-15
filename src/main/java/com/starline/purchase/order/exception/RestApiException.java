package com.starline.purchase.order.exception;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/15/2024 10:52 PM
@Last Modified 10/15/2024 10:52 PM
Version 1.0
*/

import lombok.Getter;

@Getter
public class RestApiException extends Exception {

    private final int httpCode;

    public RestApiException(String message, int httpCode) {
        super(message);
        this.httpCode = httpCode == 0 ? 500 : httpCode;
    }

    public RestApiException(String message) {
        super(message);
        this.httpCode = 500;
    }

}
