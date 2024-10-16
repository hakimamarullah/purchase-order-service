package com.starline.purchase.order.dto.response;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 8:03 AM
@Last Modified 10/16/2024 8:03 AM
Version 1.0
*/

import lombok.Data;

@Data
public class PurchaseOrderDetailResponse {

    private Integer itemId;
    private String name;
    private Integer itemQty;
    private Integer itemCost;
    private Integer itemPrice;

}
