package com.starline.purchase.order.dto.response;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 8:00 AM
@Last Modified 10/16/2024 8:00 AM
Version 1.0
*/

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOrderResponse {

    private Integer pohId;
    private List<PurchaseOrderDetailResponse> purchaseOrderDetails;
    private Integer totalCost;
    private Integer totalPrice;
    private String description;
    private LocalDateTime dateTime;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime updatedDatetime;
    private LocalDateTime createdDatetime;

    public void setId(Integer id) {
        this.pohId = id;
    }


}
