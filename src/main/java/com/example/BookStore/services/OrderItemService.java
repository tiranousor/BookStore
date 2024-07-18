package com.example.BookStore.services;

import com.example.BookStore.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {
    private final CartRepository orderItemRepository;

    @Autowired
    public OrderItemService(CartRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void deleteAll(int id){
        orderItemRepository.deleteAllByBookId(id);
    }

}
