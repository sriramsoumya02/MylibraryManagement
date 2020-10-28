package com.example.mylibrarymanagement.service;

import com.example.mylibrarymanagement.CustomException;
import com.example.mylibrarymanagement.model.Book;
import com.example.mylibrarymanagement.model.BorrowedBook;
import com.example.mylibrarymanagement.model.User;

import java.util.List;
import java.util.Map;

public interface BookService {
    public List<Book> getAvailableBooks();

    public Book createBook(Book book);

    public void DeleteBook(long bookid);

    public Book findBookByID(long bookid);

    public List<BorrowedBook> borrowBooks(List<Long> books, long userId);

    boolean isBooksAvailableToBorrow(List<Long> bookIds, List<BorrowedBook> borrowedBooks) throws CustomException;

    void returnBook(List<Long> bookList, long userId) throws CustomException;

    public BorrowedBook renewBook(long bookId, long userId);

    public Map calculateOverdueAmount(User user) throws CustomException;
}
