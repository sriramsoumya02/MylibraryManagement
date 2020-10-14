package com.example.mylibrarymanagement.service;

import com.example.mylibrarymanagement.model.Book;
import com.example.mylibrarymanagement.model.BorrowedBookId;
import com.example.mylibrarymanagement.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BookServiceTest {
    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowedBookService borrowedBookService;

    @Test
    public void isBookAvailableToBorrowTest() {
        Book book = new Book("The Four agreements", "Don Miguel Ruiz", 3, 3);
        when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(book));
        when(borrowedBookService.findBorrowedBookById(Mockito.any(BorrowedBookId.class))).thenReturn(null);
        //boolean isAvailable = bookService.isBookAvailableToBorrow(Long.valueOf("1"), Long.valueOf("1"));
        //  assertTrue(isAvailable);
    }

}
