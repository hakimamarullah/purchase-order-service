package com.starline.purchase.order.service;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/16/2024 12:14 AM
@Last Modified 10/16/2024 12:14 AM
Version 1.0
*/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starline.purchase.order.dto.request.ItemDto;
import com.starline.purchase.order.dto.response.ApiResponse;
import com.starline.purchase.order.exception.DataNotFoundException;
import com.starline.purchase.order.exception.RestApiException;
import com.starline.purchase.order.model.Item;
import com.starline.purchase.order.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    private final ObjectMapper mapper;

    public ItemService(ItemRepository itemRepository, ObjectMapper mapper) {
        this.itemRepository = itemRepository;
        this.mapper = mapper;
    }

    public ApiResponse<Item> getItemById(Integer id) throws DataNotFoundException {
        Item user = itemRepository.findById(id).orElseThrow(() -> new DataNotFoundException("item not found"));
        return ApiResponse.<Item>builder().data(user).build();
    }

    public ApiResponse<PagedModel<Item>> getItems(Pageable pageable) {
        Page<Item> users = itemRepository.findAll(pageable);
        return ApiResponse.<PagedModel<Item>>builder()
                .data(new PagedModel<>(users))
                .build();
    }

    public ApiResponse<Object> deleteItemById(Integer userId) {
        int count = itemRepository.deleteItemById(userId);
        return ApiResponse.builder()
                .code(200)
                .message(String.format("%d item deleted", count))
                .build();
    }

    @Transactional
    public ApiResponse<Item> updateItem(ItemDto itemDto) throws RestApiException {
        if (itemDto.getId() == null) {
            throw new RestApiException("item id is required", 400);
        }
        Item item = mapper.convertValue(itemDto, Item.class);
        item = itemRepository.save(item);
        return ApiResponse.<Item>builder()
                .code(200)
                .message("item updated")
                .data(item)
                .build();
    }
}
