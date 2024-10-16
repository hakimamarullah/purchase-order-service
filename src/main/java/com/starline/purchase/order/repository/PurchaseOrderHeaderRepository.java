package com.starline.purchase.order.repository;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 7:12 AM
@Last Modified 10/16/2024 7:12 AM
Version 1.0
*/

import com.starline.purchase.order.model.PurchaseOrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderHeaderRepository extends JpaRepository<PurchaseOrderHeader, Integer> {
}
