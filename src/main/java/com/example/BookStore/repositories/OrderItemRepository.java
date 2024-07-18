package com.example.BookStore.repositories;

import com.example.BookStore.providers.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Repeatable;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    void deleteAllByBookId(int id);

}
