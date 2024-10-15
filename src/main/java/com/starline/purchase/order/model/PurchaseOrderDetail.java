package com.starline.purchase.order.model;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/15/2024 9:18 PM
@Last Modified 10/15/2024 9:18 PM
Version 1.0
*/

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "po_d")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderDetail extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "poh_id")
    private PurchaseOrderHeader purchaseOrderHeader;

    @Column(name = "item_qty")
    private Integer itemQty;

    @Column(name = "item_cost")
    private Integer itemCost;

    @Column(name = "item_price")
    private Integer itemPrice;


}