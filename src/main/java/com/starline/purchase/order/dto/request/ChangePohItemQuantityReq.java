package com.starline.purchase.order.dto.request;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 8:34 AM
@Last Modified 10/16/2024 8:34 AM
Version 1.0
*/

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ChangePohItemQuantityReq {

    @NotNull(message = "purchase order id should not be null")
    @Positive(message = "ID should be positive number")
    private Integer pohId;

    @NotNull(message = "item id should not be null")
    @Positive(message = "ID should be positive number")
    private Integer itemId;

    @Min(value = 0, message = "new quantity should not be less than 0")
    private Integer newQty;
}
