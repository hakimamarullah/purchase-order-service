package com.starline.purchase.order.mapper;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 10:47 AM
@Last Modified 10/16/2024 10:47 AM
Version 1.0
*/

import com.starline.purchase.order.dto.response.PurchaseOrderDetailResponse;
import com.starline.purchase.order.dto.response.PurchaseOrderResponse;
import com.starline.purchase.order.model.PurchaseOrderDetail;
import com.starline.purchase.order.model.PurchaseOrderHeader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PurchaseOrderHeaderMapper {

    PurchaseOrderHeaderMapper INSTANCE = Mappers.getMapper(PurchaseOrderHeaderMapper.class);

    @Mapping(target = "pohId", source = "id")
    PurchaseOrderResponse toPurchaseOrderResponse(PurchaseOrderHeader poHeader);

    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    PurchaseOrderDetailResponse toPurchaseOrderDetailResponse(PurchaseOrderDetail detail);

    List<PurchaseOrderDetailResponse> toPurchaseOrderDetailResponseList(List<PurchaseOrderDetail> details);


}
