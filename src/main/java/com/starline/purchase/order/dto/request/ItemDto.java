package com.starline.purchase.order.dto.request;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 12:13 AM
@Last Modified 10/16/2024 12:13 AM
Version 1.0
*/

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ItemDto {

    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;


    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive value")
    private Integer price;

    @NotNull(message = "Cost is required")
    @Positive(message = "Cost must be a positive value")
    private Integer cost;
}