package com.starline.purchase.order.controller;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 12:25 AM
@Last Modified 10/16/2024 12:25 AM
Version 1.0
*/

import com.starline.purchase.order.config.constant.RoleConstant;
import com.starline.purchase.order.config.constant.Route;
import com.starline.purchase.order.dto.request.ItemDto;
import com.starline.purchase.order.dto.response.ApiResponse;
import com.starline.purchase.order.exception.DataNotFoundException;
import com.starline.purchase.order.exception.RestApiException;
import com.starline.purchase.order.model.Item;
import com.starline.purchase.order.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
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

@RestController
@RequestMapping(Route.API_V1 + Route.ITEMS)
@SecurityRequirement(name = "bearerJWT")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }



    @PostMapping
    @Operation(summary = "Create new item")
    @RolesAllowed({RoleConstant.ADMIN})
    public ResponseEntity<ApiResponse<Item>> createItem(@Valid @RequestBody ItemDto request) {
        ApiResponse<Item> response = itemService.createItem(request);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
    @GetMapping("/{itemId}")
    @Operation(summary = "Get item by id")
    public ResponseEntity<ApiResponse<Item>> getItemById(@PathVariable Integer itemId) throws DataNotFoundException {
        ApiResponse<Item> response = itemService.getItemById(itemId);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all items", description = "Get all items with pagination")
    public ResponseEntity<ApiResponse<PagedModel<Item>>> getItems(@ParameterObject Pageable pageable) {
        ApiResponse<PagedModel<Item>> response = itemService.getItems(pageable);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "Delete item by id")
    @RolesAllowed({RoleConstant.ADMIN})
    public ResponseEntity<ApiResponse<Object>> deleteItemById(@PathVariable Integer itemId) {
        ApiResponse<Object> response = itemService.deleteItemById(itemId);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @PutMapping
    @Operation(summary = "Update item")
    @RolesAllowed({RoleConstant.ADMIN})
    public ResponseEntity<ApiResponse<Item>> updateItem(@Valid @RequestBody ItemDto request) throws RestApiException {
        ApiResponse<Item> response = itemService.updateItem(request);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
