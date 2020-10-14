package com.example.mylibrarymanagement;

import com.example.mylibrarymanagement.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MylibrarymanagementApplication implements CommandLineRunner {
    @Autowired
    BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(MylibrarymanagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        bookService.createBook(new Book("abcd", "Atomic Habits", 3, 3));
//        bookService.createBook(new Book("sasda", "Zero to One", 2, 3));
//        bookService.createBook(new Book("hgkg", "The Alchemist", 1, 3));
//        bookService.createBook(new Book("hgkh", "Ikigai", 4, 3));
//        bookService.createBook(new Book("gkj", "Seven Days", 0, 3));
//        bookService.createBook(new Book("ghhk", "The 5AM Club", 1, 3));
    }
}
