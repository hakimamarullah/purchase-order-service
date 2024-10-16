package com.starline.purchase.order.service;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 7:12 AM
@Last Modified 10/16/2024 7:12 AM
Version 1.0
*/

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starline.purchase.order.config.constant.MessageConstant;
import com.starline.purchase.order.dto.request.AddItemPo;
import com.starline.purchase.order.dto.request.ChangePohDescriptionReq;
import com.starline.purchase.order.dto.request.ChangePohItemQuantityReq;
import com.starline.purchase.order.dto.request.PurchaseOrderRequest;
import com.starline.purchase.order.dto.response.ApiResponse;
import com.starline.purchase.order.dto.response.PurchaseOrderResponse;
import com.starline.purchase.order.exception.DataNotFoundException;
import com.starline.purchase.order.mapper.PurchaseOrderHeaderMapper;
import com.starline.purchase.order.model.Item;
import com.starline.purchase.order.model.PurchaseOrderDetail;
import com.starline.purchase.order.model.PurchaseOrderHeader;
import com.starline.purchase.order.repository.ItemRepository;
import com.starline.purchase.order.repository.PurchaseOrderDetailRepository;
import com.starline.purchase.order.repository.PurchaseOrderHeaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class PurchaseOrderService {

    private final ObjectMapper mapper;

    private final PurchaseOrderHeaderRepository purchaseOrderHeaderRepository;

    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;

    private final ItemRepository itemRepository;


    public PurchaseOrderService(ObjectMapper mapper, PurchaseOrderHeaderRepository purchaseOrderHeaderRepository, PurchaseOrderDetailRepository purchaseOrderDetailRepository, ItemRepository itemRepository) {
        this.mapper = mapper;
        this.purchaseOrderHeaderRepository = purchaseOrderHeaderRepository;
        this.purchaseOrderDetailRepository = purchaseOrderDetailRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public ApiResponse<PurchaseOrderResponse> createPurchaseOrder(PurchaseOrderRequest poRequest) throws JsonProcessingException, DataNotFoundException {
        log.info("START CREATE PURCHASE ORDER {}", mapper.writeValueAsString(poRequest));
        Item item = itemRepository.findById(poRequest.getItemId()).orElseThrow(() -> new DataNotFoundException("Item Does Not Exist"));

        int totalCost = item.getCost() * poRequest.getItemQty();
        int totalPrice = item.getPrice() * poRequest.getItemQty();

        PurchaseOrderHeader poHeader = new PurchaseOrderHeader();
        poHeader.setDateTime(LocalDateTime.now());
        poHeader.setDescription(poRequest.getDescription());
        poHeader.setTotalCost(totalCost);
        poHeader.setTotalPrice(totalPrice);

        purchaseOrderHeaderRepository.save(poHeader);

        PurchaseOrderDetail poDetails = new PurchaseOrderDetail();
        poDetails.setPurchaseOrderHeader(poHeader);
        poDetails.setItem(item);
        poDetails.setItemPrice(item.getPrice());
        poDetails.setItemCost(item.getCost());
        poDetails.setItemQty(poRequest.getItemQty());

        purchaseOrderDetailRepository.save(poDetails);
        List<PurchaseOrderDetail> poDetailsList = new ArrayList<>();
        poDetailsList.add(poDetails);
        poHeader.setPurchaseOrderDetails(poDetailsList);

        PurchaseOrderResponse poResponse = PurchaseOrderHeaderMapper.INSTANCE.toPurchaseOrderResponse(poHeader);

        log.info("END CREATE PURCHASE ORDER {}", mapper.writeValueAsString(poResponse));
        return ApiResponse.<PurchaseOrderResponse>builder()
                .code(201)
                .data(poResponse)
                .message("Purchase Order Created")
                .build();
    }

    public ApiResponse<PagedModel<PurchaseOrderResponse>> getAllPurchaseOrders(Pageable pageable) {
        Page<PurchaseOrderHeader> purchaseOrderHeaders = purchaseOrderHeaderRepository.findAll(pageable);
        Page<PurchaseOrderResponse> purchaseOrderResponses = purchaseOrderHeaders.map(PurchaseOrderHeaderMapper.INSTANCE::toPurchaseOrderResponse);
        return ApiResponse.setSuccess(new PagedModel<>(purchaseOrderResponses), "Success Get All Purchase Order");
    }

    public ApiResponse<PurchaseOrderResponse> getPurchaseOrderById(Integer pohId) throws DataNotFoundException {
        PurchaseOrderHeader poHeader = purchaseOrderHeaderRepository.findById(pohId).orElseThrow(() -> new DataNotFoundException(MessageConstant.PURCHASE_ORDER_DOES_NOT_EXIST));
        return ApiResponse.setSuccess(PurchaseOrderHeaderMapper.INSTANCE.toPurchaseOrderResponse(poHeader), "Success Get Purchase Order");
    }

    @Transactional
    public ApiResponse<PurchaseOrderResponse> deletePurchaseOrderById(Integer pohId) throws DataNotFoundException {
        PurchaseOrderHeader poHeader = purchaseOrderHeaderRepository.findById(pohId).orElseThrow(() -> new DataNotFoundException(MessageConstant.PURCHASE_ORDER_DOES_NOT_EXIST));
        purchaseOrderHeaderRepository.delete(poHeader);
        return ApiResponse.setSuccess(PurchaseOrderHeaderMapper.INSTANCE.toPurchaseOrderResponse(poHeader), "Success Delete Purchase Order");
    }

    @Transactional
    @Modifying
    public ApiResponse<PurchaseOrderResponse> updatePurchaseOrderDescription(ChangePohDescriptionReq changePohDescriptionReq) throws DataNotFoundException {
        PurchaseOrderHeader poHeader = purchaseOrderHeaderRepository.findById(changePohDescriptionReq.getPohId()).orElseThrow(() -> new DataNotFoundException(MessageConstant.PURCHASE_ORDER_DOES_NOT_EXIST));
        poHeader.setDescription(changePohDescriptionReq.getDescription());
        purchaseOrderHeaderRepository.save(poHeader);
        return ApiResponse.setSuccess(PurchaseOrderHeaderMapper.INSTANCE.toPurchaseOrderResponse(poHeader), "Success Update Purchase Order Description");
    }

    @Transactional
    @Modifying
    public ApiResponse<PurchaseOrderResponse> updatePurchaseOrderItemQuantity(ChangePohItemQuantityReq changePohItemQuantityReq) throws DataNotFoundException, JsonProcessingException {
        log.info("START UPDATE PURCHASE ORDER ITEM QUANTITY {}", mapper.writeValueAsString(changePohItemQuantityReq));
        PurchaseOrderDetail poDetail = purchaseOrderDetailRepository.findByPurchaseOrderHeaderIdAndItemId(changePohItemQuantityReq.getPohId(), changePohItemQuantityReq.getItemId()).orElseThrow(() -> new DataNotFoundException("Purchase Order Detail Does Not Exist"));

        if (changePohItemQuantityReq.getNewQty() == 0) {
            purchaseOrderDetailRepository.delete(poDetail);
            PurchaseOrderHeader purchaseOrderHeader = purchaseOrderHeaderRepository.findById(changePohItemQuantityReq.getPohId()).orElseThrow(() -> new DataNotFoundException(MessageConstant.PURCHASE_ORDER_DOES_NOT_EXIST));
            List<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrderHeader.getPurchaseOrderDetails().stream().filter(itemDetails -> !Objects.equals(itemDetails.getId(), poDetail.getId())).toList();
            int totalCost = getTotalCost(purchaseOrderDetails);
            int totalPrice = getTotalPrice(purchaseOrderDetails);
            purchaseOrderHeader.setTotalCost(totalCost);
            purchaseOrderHeader.setTotalPrice(totalPrice);
            purchaseOrderHeaderRepository.save(purchaseOrderHeader);
            return ApiResponse.setSuccess(null, "Success Delete Purchase Order Item Quantity");
        }
        poDetail.setItemQty(changePohItemQuantityReq.getNewQty());
        purchaseOrderDetailRepository.save(poDetail);

        PurchaseOrderHeader purchaseOrderHeader = purchaseOrderHeaderRepository.findById(changePohItemQuantityReq.getPohId()).orElseThrow(() -> new DataNotFoundException(MessageConstant.PURCHASE_ORDER_DOES_NOT_EXIST));

        List<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrderHeader.getPurchaseOrderDetails();


        int totalCost = getTotalCost(purchaseOrderDetails);
        int totalPrice = getTotalPrice(purchaseOrderDetails);
        purchaseOrderHeader.setTotalCost(totalCost);
        purchaseOrderHeader.setTotalPrice(totalPrice);

        purchaseOrderHeaderRepository.save(purchaseOrderHeader);
        PurchaseOrderResponse purchaseOrderResponse = PurchaseOrderHeaderMapper.INSTANCE.toPurchaseOrderResponse(purchaseOrderHeader);

        log.info("UPDATE PURCHASE ORDER ITEM QUANTITY {}", mapper.writeValueAsString(purchaseOrderResponse));
        return ApiResponse.setSuccess(purchaseOrderResponse, "Success Update Purchase Order Item Quantity");
    }

    public ApiResponse<PurchaseOrderResponse> addNewItemToPurchaseOrder(AddItemPo addItemPo) throws JsonProcessingException, DataNotFoundException {
        log.info("START ADD NEW ITEM TO PURCHASE ORDER {}", mapper.writeValueAsString(addItemPo));
        PurchaseOrderHeader poHeader = purchaseOrderHeaderRepository.findById(addItemPo.getPohId()).orElseThrow(() -> new DataNotFoundException(MessageConstant.PURCHASE_ORDER_DOES_NOT_EXIST));
        Item item = itemRepository.findById(addItemPo.getItemId()).orElseThrow(() -> new DataNotFoundException("Item Does Not Exist"));
        Optional<PurchaseOrderDetail> poDetail = purchaseOrderDetailRepository.findByPurchaseOrderHeaderIdAndItemId(addItemPo.getPohId(), addItemPo.getItemId());

        List<PurchaseOrderDetail> purchaseOrderDetails = poHeader.getPurchaseOrderDetails();

        if (poDetail.isPresent()) {
            poDetail.get().setItemQty(poDetail.get().getItemQty() + addItemPo.getQuantity());
            purchaseOrderDetailRepository.save(poDetail.get());
            purchaseOrderDetails.add(poDetail.get());
        } else {
            PurchaseOrderDetail newPoDetail = new PurchaseOrderDetail();
            newPoDetail.setPurchaseOrderHeader(poHeader);
            newPoDetail.setItem(item);
            newPoDetail.setItemPrice(item.getPrice());
            newPoDetail.setItemCost(item.getCost());
            newPoDetail.setItemQty(addItemPo.getQuantity());
            purchaseOrderDetailRepository.save(newPoDetail);
            purchaseOrderDetails.add(newPoDetail);
        }

        poHeader.setPurchaseOrderDetails(purchaseOrderDetails);
        int totalCost = getTotalCost(purchaseOrderDetails);
        int totalPrice = getTotalPrice(purchaseOrderDetails);
        poHeader.setTotalCost(totalCost);
        poHeader.setTotalPrice(totalPrice);
        purchaseOrderHeaderRepository.save(poHeader);
        PurchaseOrderResponse purchaseOrderResponse = PurchaseOrderHeaderMapper.INSTANCE.toPurchaseOrderResponse(poHeader);
        log.info("SUCCESS ADD NEW ITEM TO PURCHASE ORDER {}", mapper.writeValueAsString(purchaseOrderResponse));
        return ApiResponse.setSuccess(purchaseOrderResponse, "Success Add New Item To Purchase Order");

    }

    private int getTotalCost(List<PurchaseOrderDetail> purchaseOrderDetails) {
        return purchaseOrderDetails.stream().map(itemDetails -> itemDetails.getItemCost() * itemDetails.getItemQty()).reduce(0, Integer::sum);
    }

    private int getTotalPrice(List<PurchaseOrderDetail> purchaseOrderDetails) {
        return purchaseOrderDetails.stream().map(itemDetails -> itemDetails.getItemPrice() * itemDetails.getItemQty()).reduce(0, Integer::sum);
    }

}
