package com.starline.purchase.order.controller;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 8:47 AM
@Last Modified 10/16/2024 8:47 AM
Version 1.0
*/

import com.fasterxml.jackson.core.JsonProcessingException;
import com.starline.purchase.order.config.constant.Route;
import com.starline.purchase.order.dto.request.AddItemPo;
import com.starline.purchase.order.dto.request.ChangePohDescriptionReq;
import com.starline.purchase.order.dto.request.ChangePohItemQuantityReq;
import com.starline.purchase.order.dto.request.PurchaseOrderRequest;
import com.starline.purchase.order.dto.response.ApiResponse;
import com.starline.purchase.order.dto.response.PurchaseOrderResponse;
import com.starline.purchase.order.exception.DataNotFoundException;
import com.starline.purchase.order.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(Route.API_V1 + Route.PURCHASE_ORDERS)
@SecurityRequirement(name = "bearerJWT")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    @Operation(summary = "Create purchase order")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> createPurchaseOrder(@Valid @RequestBody PurchaseOrderRequest purchaseOrderRequest) throws DataNotFoundException, JsonProcessingException {
        ApiResponse<PurchaseOrderResponse> apiResponse = purchaseOrderService.createPurchaseOrder(purchaseOrderRequest);
        return ResponseEntity.status(apiResponse.getHttpStatus()).body(apiResponse);
    }

    @GetMapping
    @Operation(summary = "Get all purchase orders", description = "Get all purchase orders with pagination")
    public ResponseEntity<ApiResponse<PagedModel<PurchaseOrderResponse>>> getAllPurchaseOrders(@ParameterObject Pageable pageable) throws JsonProcessingException {
        ApiResponse<PagedModel<PurchaseOrderResponse>> apiResponse = purchaseOrderService.getAllPurchaseOrders(pageable);
        return ResponseEntity.status(apiResponse.getHttpStatus()).body(apiResponse);
    }

    @GetMapping("/{pohId}")
    @Operation(summary = "Get purchase order by id")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> getPurchaseOrderById(@PathVariable Integer pohId) throws DataNotFoundException {
        ApiResponse<PurchaseOrderResponse> apiResponse = purchaseOrderService.getPurchaseOrderById(pohId);
        return ResponseEntity.status(apiResponse.getHttpStatus()).body(apiResponse);
    }

    @PutMapping
    @Operation(summary = "Update purchase order description")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> changePurchaseOrderDescription(@Valid @RequestBody ChangePohDescriptionReq changePohDescriptionReq) throws DataNotFoundException, JsonProcessingException {
        ApiResponse<PurchaseOrderResponse> apiResponse = purchaseOrderService.updatePurchaseOrderDescription(changePohDescriptionReq);
        return ResponseEntity.status(apiResponse.getHttpStatus()).body(apiResponse);
    }

    @DeleteMapping("/{pohId}")
    @Operation(summary = "Delete purchase order by id")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> deletePurchaseOrderById(@PathVariable Integer pohId) throws DataNotFoundException {
        ApiResponse<PurchaseOrderResponse> apiResponse = purchaseOrderService.deletePurchaseOrderById(pohId);
        return ResponseEntity.status(apiResponse.getHttpStatus()).body(apiResponse);
    }

    @PostMapping("/item/quantity")
    @Operation(summary = "Update purchase order item quantity", description = "If set to 0 then the po_d will be deleted")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> updatePurchaseOrderItemQty(@Valid @RequestBody ChangePohItemQuantityReq changePohItemQuantityReq) throws DataNotFoundException, JsonProcessingException {
        ApiResponse<PurchaseOrderResponse> apiResponse = purchaseOrderService.updatePurchaseOrderItemQuantity(changePohItemQuantityReq);
        return ResponseEntity.status(apiResponse.getHttpStatus()).body(apiResponse);
    }

    @PostMapping("/item/add")
    @Operation(summary = "Add item to purchase order", description = "If item already exist in po_d then the quantity will be increased")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> addItemToPurchaseOrder(@Valid @RequestBody AddItemPo addItemPo) throws DataNotFoundException, JsonProcessingException, ExecutionException, InterruptedException {
        ApiResponse<PurchaseOrderResponse> apiResponse = purchaseOrderService.addNewItemToPurchaseOrder(addItemPo);
        return ResponseEntity.status(apiResponse.getHttpStatus()).body(apiResponse);
    }
}
