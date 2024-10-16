package com.starline.purchase.order.dto.response;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/21/2024 8:30 PM
@Last Modified 6/21/2024 8:30 PM
Version 1.0
*/

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private Integer code;
    private String message;
    private T data;



    public static <U> ApiResponse<U> setSuccess(U data, String message) {
        return ApiResponse.<U>builder()
                .code(200)
                .data(data)
                .message(message)
                .build();
    }

    public static ApiResponse<Void> setSuccess(String message) {
        return setSuccess(null, message);
    }

    public static <U> ApiResponse<U> setSuccess() {
        return setSuccess(null, "Success");
    }

    public static <U> ApiResponse<U> setSuccess(U data) {
        return setSuccess(data, "Success");
    }




    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(this.code);
    }
}
