package com.example.BookStore.services;

import com.example.BookStore.repositories.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {
    private CartRepository orderItemRepository;

    public void deleteAll(int id){
        orderItemRepository.deleteAllByBookId(id);
    }

}
