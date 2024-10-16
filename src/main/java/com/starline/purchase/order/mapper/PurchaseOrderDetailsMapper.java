package com.starline.purchase.order.mapper;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 10:49 AM
@Last Modified 10/16/2024 10:49 AM
Version 1.0
*/

import com.starline.purchase.order.dto.response.PurchaseOrderDetailResponse;
import com.starline.purchase.order.model.PurchaseOrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PurchaseOrderDetailsMapper {
    PurchaseOrderDetailsMapper INSTANCE = Mappers.getMapper(PurchaseOrderDetailsMapper.class);
    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    PurchaseOrderDetailResponse toPurchaseOrderDetailResponse(PurchaseOrderDetail poDetail);
}
