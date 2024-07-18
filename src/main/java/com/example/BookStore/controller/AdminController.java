package com.example.BookStore.controller;

import com.example.BookStore.providers.Book;
import com.example.BookStore.providers.Order;
import com.example.BookStore.providers.Person;
import com.example.BookStore.services.*;
import com.example.BookStore.util.BookValidator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class AdminController {
    private final BookValidator bookValidator;
    private final BookService bookService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final PersonService personService;
    private final CartService cartService;


    public AdminController(BookValidator bookValidator, BookService bookService, OrderService orderService, OrderItemService orderItemService, PersonService personService, CartService cartService) {
        this.bookValidator = bookValidator;
        this.bookService = bookService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.personService = personService;
        this.cartService = cartService;
    }

    @GetMapping("/add-book")
    public String addBook(@ModelAttribute("book") Book book){
        return "addBook";
    }

    @PostMapping("/add-book")
    public String createBook(Model model, @Valid @ModelAttribute("book") Book book, BindingResult bindingResult){
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "addBook";
        model.addAttribute("postCorrect", true);
        model.addAttribute("book", new Book());

        bookService.save(book);
        return "addBook";
    }
    @Transactional
    @PostMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        cartService.deleteAll(id);
        orderItemService.deleteAll(id);
        bookService.delete(id);
        return "redirect:/";
    }
    @GetMapping("{id}/edit")
    public String editBook(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOne(id));
        return "edit";
    }
    @PostMapping("/{id}/edit")
    public String updateBook(Model model, @PathVariable("id") int id, @Valid Book book, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "edit";
        bookService.update(id,book);
        return "redirect:/bookDetails";
    }

    @GetMapping("/bookDetails")
    public String bookDetails(Model model) {
        model.addAttribute("bookList", bookService.findAll());
        return "bookDetails";
    }

    @GetMapping("/users")
    public String getAllOrders(Authentication authentication, Model model) {
        List<Order> orders = orderService.showAllOrders();
        Map<Person, List<Order>> personOrdersMap = orders.stream().collect(Collectors.groupingBy(Order::getPerson));
        model.addAttribute("personOrdersMap", personOrdersMap);
        return "users";
    }
    @PostMapping("/updateOrderStatus")
    public String updateOrderStatus(@RequestParam("orderId") int orderId, @RequestParam("status") String status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/users";
    }
}
