package com.example.mylibrarymanagement.service;

import com.example.mylibrarymanagement.model.Book;
import com.example.mylibrarymanagement.model.BorrowedBook;
import com.example.mylibrarymanagement.model.BorrowedBookId;
import com.example.mylibrarymanagement.model.User;

import java.util.List;

public interface BorrowedBookService {
    BorrowedBook createBorrowedBook(Book book, User user);

    List<BorrowedBook> createBorrowedBooks(List<BorrowedBook> borrowedBooks);

    BorrowedBook findBorrowedBookById(BorrowedBookId borrowedBookId);

    List<BorrowedBook> getBorrowedBooksByUser(User user);

    BorrowedBook updateBorrowedBook(BorrowedBook borrowedBook);
    // List<Book> getBorrowedBookIdsByUser(User user);
}
