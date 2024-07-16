package com.example.BookStore.services;

import com.example.BookStore.providers.Book;
import com.example.BookStore.providers.Cart;
import com.example.BookStore.providers.Person;
import com.example.BookStore.repositories.BookRepository;
import com.example.BookStore.repositories.CartRepository;
import com.example.BookStore.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final PersonRepository personRepository;
    private final BookRepository bookRepository;
    @Autowired
    public CartService(CartRepository cartRepository, PersonRepository personRepository, BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
    }


    public void saveCart(int person_id, int book_id) {

        Person person = personRepository.findById(person_id).get();
        Book book = bookRepository.findById(book_id).get();

        Cart cartStatus = cartRepository.findByPersonIdAndBookId(person_id, book_id);

        Cart cart = null;

        if (ObjectUtils.isEmpty(cartStatus)) {
            cart = new Cart();
            cart.setBook(book);
            cart.setPerson(person);
            cart.setQuantity(1);
            cart.setTotalPrice(book.getPrice());
        } else {
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(cart.getQuantity() * cart.getBook().getPrice());
        }
        cartRepository.save(cart);
    }

    public List<Cart> getListCarts(Person person){
        List<Cart> carts = cartRepository.findByPerson(person);

        double totalcartPrice = 0.0;
        List<Cart> updateCarts = new ArrayList<>();
        for (Cart c : carts) {
            double totalPrice = (c.getBook().getPrice() * c.getQuantity());
            c.setTotalPrice(totalPrice);
            totalcartPrice += totalPrice;
            c.setTotalOrderPrice(totalcartPrice);

            updateCarts.add(c);
        }

        return updateCarts;
    }

    public Integer getCountCart(int person_id) {
        return cartRepository.countByPersonId(person_id);
    }

    public void updateQuantity(String sy, Integer oId) {

        Cart cart = cartRepository.findById(oId).get();
        int updateQuantity;

        if (sy.equalsIgnoreCase("-")) {
            updateQuantity = cart.getQuantity() - 1;

            if (updateQuantity <= 0) {
                cartRepository.delete(cart);
            } else {
                cart.setQuantity(updateQuantity);
                cartRepository.save(cart);
            }

        } else {
            updateQuantity = cart.getQuantity() + 1;
            cart.setQuantity(updateQuantity);
            cartRepository.save(cart);
        }

    }
    public List<Cart> getAllcarts(String personRole){
        return cartRepository.findAllByPersonRole(personRole);
    }
}
