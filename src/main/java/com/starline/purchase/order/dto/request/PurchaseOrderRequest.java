package com.starline.purchase.order.dto.request;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 7:56 AM
@Last Modified 10/16/2024 7:56 AM
Version 1.0
*/

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PurchaseOrderRequest {

    @NotNull(message = "Item ID should not be null")
    @Positive(message = "Item ID should be positive number")
    private Integer itemId;


    @NotNull(message = "Item quantity should not be null")
    @Min(value = 1, message = "Item quantity minimum 1 item")
    private Integer itemQty;

    private String description;
}
