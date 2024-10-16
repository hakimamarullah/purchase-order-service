package com.starline.purchase.order.dto.request;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 8:31 AM
@Last Modified 10/16/2024 8:31 AM
Version 1.0
*/

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ChangePohDescriptionReq {

    @NotNull(message = "purchase order id should not be null")
    @Positive(message = "ID should be positive number")
    private Integer pohId;

    @Length(message = "Description should not be more than 500 characters", max = 500)
    private String description;
}
