package com.example.BookStore.repositories;

import com.example.BookStore.providers.Order;
import com.example.BookStore.providers.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByPerson(Person person);
    List<Order> findAllByPersonOrderByOrderDateDesc(Person person);
//
//    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
//    List<Order> findAllOrdersOrderByOrderDate();
}
