package com.starline.purchase.order.dto.request;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 2:42 PM
@Last Modified 10/16/2024 2:42 PM
Version 1.0
*/

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddItemPo {

    @NotNull(message = "purchase order id should not be null")
    @Positive(message = "ID should be positive number")
    private Integer pohId;

    @NotNull(message = "item id should not be null")
    @Positive(message = "ID should be positive number")
    private Integer itemId;

    @NotNull(message = "quantity should not be null")
    @Min(value = 1, message = "quantity minimum 1 item")
    private Integer quantity;
}
