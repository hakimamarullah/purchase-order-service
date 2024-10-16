package com.starline.purchase.order.model;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/15/2024 9:15 PM
@Last Modified 10/15/2024 9:15 PM
Version 1.0
*/

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "po_h")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderHeader extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;


    @Column(name = "datetime")
    private LocalDateTime dateTime;

    @Column(name = "description", length = 500)
    private String description;


    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "total_cost")
    private Integer totalCost;

    @OneToMany(mappedBy = "purchaseOrderHeader", orphanRemoval = true)
    @JsonManagedReference("purchaseOrderHeader-purchaseOrderDetail")
    private List<PurchaseOrderDetail> purchaseOrderDetails;

}
