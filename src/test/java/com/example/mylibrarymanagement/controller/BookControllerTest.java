package com.example.mylibrarymanagement.controller;

import com.example.mylibrarymanagement.model.Book;
import com.example.mylibrarymanagement.model.BorrowedBook;
import com.example.mylibrarymanagement.model.User;
import com.example.mylibrarymanagement.service.BookService;
import com.example.mylibrarymanagement.service.BorrowedBookService;
import com.example.mylibrarymanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class BookControllerTest {
    private static final Logger log = LoggerFactory.getLogger(BookControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BorrowedBookService borrowedBookService;

    @MockBean
    private UserService userService;

    @Test
    public void getAllBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        books.add(new Book("The Four agreements", "Don Miguel Ruiz", 3, 3));
        when(bookService.getAvailableBooks()).thenReturn(books);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/book").contentType(MediaType.APPLICATION_JSON)).toString();
        log.warn("Soumya -->", result);
        mockMvc.perform(MockMvcRequestBuilders.get("/book/available").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(1))).andDo(print());
    }

    @Test
    public void borrowBookTestSucessScenario() throws Exception {

        BorrowedBook borrowBook = new BorrowedBook(new Book("The Four agreements", "Don Miguel Ruiz", 3, 3), new User("test9", "test9@email.com"), LocalDateTime.now(), LocalDateTime.now().plusDays(30));

        // when(bookService.borrowABook(Mockito.anyLong(), Mockito.anyLong())).thenReturn(borrowBook);
        when(bookService.findBookByID(Mockito.anyLong())).thenReturn(new Book("The Four agreements", "Don Miguel Ruiz", 3, 3));
        when(userService.findUserById(Mockito.anyLong())).thenReturn(new User("test9", "test9@email.com"));
        when(borrowedBookService.createBorrowedBook(Mockito.any(Book.class), Mockito.any(User.class))).thenReturn(borrowBook);
        ObjectMapper mapper = new ObjectMapper();
        String borrowBookJson = mapper.writeValueAsString(borrowBook);
        mockMvc.perform(post("/book/1/borrow/1").contentType(MediaType.APPLICATION_JSON).content(borrowBookJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.noOfRenewals").value(0))
                .andDo(print());
    }

}
