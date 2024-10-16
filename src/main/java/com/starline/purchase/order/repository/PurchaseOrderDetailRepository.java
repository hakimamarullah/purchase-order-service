package com.starline.purchase.order.repository;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 7:13 AM
@Last Modified 10/16/2024 7:13 AM
Version 1.0
*/

import com.starline.purchase.order.model.PurchaseOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, Integer> {

    Optional<PurchaseOrderDetail> findByPurchaseOrderHeaderIdAndItemId(Integer pohId, Integer itemId);
}
