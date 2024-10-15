package com.starline.purchase.order.repository;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 12:16 AM
@Last Modified 10/16/2024 12:16 AM
Version 1.0
*/

import com.starline.purchase.order.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    int deleteItemById(Integer id);
}
