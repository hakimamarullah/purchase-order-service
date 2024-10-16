package com.starline.purchase.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.starline.purchase.order.dto.request.PurchaseOrderRequest;
import com.starline.purchase.order.dto.response.ApiResponse;
import com.starline.purchase.order.dto.response.PurchaseOrderResponse;
import com.starline.purchase.order.exception.DataNotFoundException;
import com.starline.purchase.order.model.Item;
import com.starline.purchase.order.model.PurchaseOrderDetail;
import com.starline.purchase.order.model.PurchaseOrderHeader;
import com.starline.purchase.order.repository.ItemRepository;
import com.starline.purchase.order.repository.PurchaseOrderDetailRepository;
import com.starline.purchase.order.repository.PurchaseOrderHeaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

class PurchaseOrderServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private PurchaseOrderHeaderRepository purchaseOrderHeaderRepository;

    @Mock
    private PurchaseOrderDetailRepository purchaseOrderDetailRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.findAndRegisterModules();

        purchaseOrderService = new PurchaseOrderService(mapper, purchaseOrderHeaderRepository, purchaseOrderDetailRepository, itemRepository);
    }

    @Test
    void whenCreatePurchaseOrderThenSuccess() throws DataNotFoundException, JsonProcessingException {
        // Prepare parameter
        PurchaseOrderRequest request = new PurchaseOrderRequest();
        request.setItemQty(1);
        request.setItemId(2);
        request.setDescription("unit-test");

        // Mock item repo
        Item item = new Item();
        item.setId(2);
        item.setName("unit-test");
        item.setPrice(1000);
        item.setCost(2000);

        doReturn(Optional.of(item)).when(itemRepository).findById(anyInt());

        // Mock purchase order repo
        doReturn(new PurchaseOrderHeader()).when(purchaseOrderHeaderRepository).save(any());
        doReturn(new PurchaseOrderDetail()).when(purchaseOrderDetailRepository).save(any());

        // Act
        ApiResponse<PurchaseOrderResponse> response = purchaseOrderService.createPurchaseOrder(request);
        PurchaseOrderResponse actualResult = response.getData();

        // Assert
        assertNotNull(response);
        assertNotNull(actualResult);
        assertEquals("Purchase Order Created", response.getMessage());
        assertEquals(201, response.getCode());
        assertEquals(1000, actualResult.getTotalPrice());
        assertEquals(2000, actualResult.getTotalCost());
        assertEquals("unit-test", actualResult.getDescription());

        verify(itemRepository).findById(anyInt());
        verify(purchaseOrderHeaderRepository).save(any());
        verify(purchaseOrderDetailRepository).save(any());

    }

    @Test
    void getAllPurchaseOrders() {
    }

    @Test
    void getPurchaseOrderById() {
    }

    @Test
    void deletePurchaseOrderById() {
    }

    @Test
    void updatePurchaseOrderDescription() {
    }

    @Test
    void updatePurchaseOrderItemQuantity() {
    }

    @Test
    void addNewItemToPurchaseOrder() {
    }
}