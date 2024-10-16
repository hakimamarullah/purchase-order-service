package com.starline.purchase.order.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.starline.purchase.order.dto.request.ItemDto;
import com.starline.purchase.order.dto.response.ApiResponse;
import com.starline.purchase.order.exception.DataNotFoundException;
import com.starline.purchase.order.exception.RestApiException;
import com.starline.purchase.order.model.Item;
import com.starline.purchase.order.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

class ItemServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();
    @InjectMocks
    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        itemService = new ItemService(itemRepository, mapper);
    }

    @Test
    void whenCreateItemThenSuccess() {
        // Prepare parameters
        ItemDto itemDto = new ItemDto();
        itemDto.setCost(1000);
        itemDto.setName("Unit Test");
        itemDto.setDescription("Unit Test");
        itemDto.setPrice(200000);

        // Mock item repo
        Item item = mapper.convertValue(itemDto, Item.class);
        doReturn(item).when(itemRepository).save(any(Item.class));

        // Execute
        ApiResponse<Item> response = itemService.createItem(itemDto);
        Item actualResult = response.getData();

        // Assert
        assertEquals(201, response.getCode());
        assertEquals("Item Created", response.getMessage());
        assertThat(actualResult, samePropertyValuesAs(item));
    }

    @Test
    void whenGetItemByIdThenSuccess() throws DataNotFoundException {
        // Mock item repository
        Item item = new Item();
        item.setCost(1000);
        item.setName("Unit Test");
        item.setDescription("Unit Test");
        item.setPrice(200000);
        doReturn(Optional.of(item)).when(itemRepository).findById(any(Integer.class));

        // Execute
        ApiResponse<Item> response = itemService.getItemById(1);
        Item actualResult = response.getData();

        // Assert
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertThat(actualResult, samePropertyValuesAs(item));

        verify(itemRepository).findById(any(Integer.class));
    }

    @Test
    void whenGetItemByIdAndItemDoesNotExistThenShouldThrowDataNotFoundException() {
        doReturn(Optional.empty()).when(itemRepository).findById(any(Integer.class));

        // Execute
        DataNotFoundException response = assertThrows(DataNotFoundException.class, () -> itemService.getItemById(1));


        // Assert
        assertEquals("item not found", response.getMessage());

        verify(itemRepository).findById(any(Integer.class));
    }

    @Test
    void whenGetItemsThenShouldReturnPagedModel() {
        // Mock item repository
        Item item = new Item();
        item.setCost(1000);
        item.setName("Unit Test");
        item.setDescription("Unit Test");
        item.setPrice(200000);
        doReturn(new PageImpl<>(List.of(item))).when(itemRepository).findAll(any(Pageable.class));

        // Execute
        ApiResponse<PagedModel<Item>> response = itemService.getItems(Pageable.unpaged());
        PagedModel<Item> actualResult = response.getData();


        // Assert
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(1, actualResult.getContent().size());
        assertThat(actualResult.getContent().getFirst(), samePropertyValuesAs(item));

        verify(itemRepository).findAll(any(Pageable.class));
    }

    @Test
    void whenDeleteItemByIdThenSuccess() {
        // Mock item repository
        doReturn(1).when(itemRepository).deleteItemById(any(Integer.class));

        // Execute
        ApiResponse<Object> response = itemService.deleteItemById(1);

        // Assert
        assertEquals(200, response.getCode());
        assertEquals("1 item deleted", response.getMessage());
        assertEquals(1, response.getData());
    }

    @Test
    void whenUpdateItemThenSuccess() throws RestApiException {
        // Prepare parameters
        ItemDto itemDto = new ItemDto();
        itemDto.setCost(1000);
        itemDto.setName("Unit Test");
        itemDto.setDescription("Unit Test");
        itemDto.setPrice(200000);
        itemDto.setId(1);

        // Mock item repo
        Item item = mapper.convertValue(itemDto, Item.class);
        doReturn(item).when(itemRepository).save(any(Item.class));
        doReturn(Optional.of(item)).when(itemRepository).findById(anyInt());

        // Execute
        ApiResponse<Item> response = itemService.updateItem(itemDto);
        Item actualResult = response.getData();

        // Assert
        assertEquals(200, response.getCode());
        assertEquals("Item Updated", response.getMessage());
        assertThat(actualResult, samePropertyValuesAs(item));
    }

    @Test
    void whenUpdateItemAndItemIdIsNotProvidedThenShouldThrowRestApiException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setCost(1000);
        itemDto.setName("Unit Test");
        itemDto.setDescription("Unit Test");
        itemDto.setPrice(200000);

        // Execute
        RestApiException response = assertThrows(RestApiException.class, () -> itemService.updateItem(itemDto));

        // Assert
        assertEquals("item id is required", response.getMessage());
        assertEquals(400, response.getHttpCode());
    }

    @Test
    void whenUpdateItemAndItemDoesNotExistThenShouldThrowDataNotFoundException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setCost(1000);
        itemDto.setName("Unit Test");
        itemDto.setDescription("Unit Test");
        itemDto.setPrice(200000);
        itemDto.setId(1);

        doReturn(Optional.empty()).when(itemRepository).findById(anyInt());

        // Execute
        DataNotFoundException response = assertThrows(DataNotFoundException.class, () -> itemService.updateItem(itemDto));

        // Assert
        assertEquals("item not found", response.getMessage());
    }
}