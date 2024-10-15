package com.starline.purchase.order.model;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/15/2024 9:11 PM
@Last Modified 10/15/2024 9:11 PM
Version 1.0
*/

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;


    @Column(name = "name", length = 500)
    private String name;

    @Column(name = "description", length = 500)
    private String description;


    @Column(name = "price")
    private Integer price;

    @Column(name = "cost")
    private Integer cost;

    @OneToMany(mappedBy = "item")
    private List<PurchaseOrderDetail> purchaseOrderDetails;

}
