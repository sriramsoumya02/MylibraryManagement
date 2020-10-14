package com.example.mylibrarymanagement.controller;

import com.example.mylibrarymanagement.config.Constants;
import com.example.mylibrarymanagement.model.BorrowedBook;
import com.example.mylibrarymanagement.model.User;
import com.example.mylibrarymanagement.service.BookService;
import com.example.mylibrarymanagement.service.BorrowedBookService;
import com.example.mylibrarymanagement.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("book")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowedBookService borrowedBookService;

    @Autowired
    private UserService userService;

    //    @Autowired
//    public BookController(BookServiceImpl bookServiceImpl) {
//        this.bookServiceImpl = bookServiceImpl;
//    }
//
//    @GetMapping
//    public ResponseEntity getBooks() {
//        List<Book> books = bookService.getAvailableBooks();
//        return new ResponseEntity(books, HttpStatus.OK);
//    }
//
//    @GetMapping
//    public ResponseEntity myBooks() {
//
//        return new ResponseEntity("gfdgf", HttpStatus.OK);
//    }
    @GetMapping("/available")
    public ResponseEntity getAllBooks() {
        return new ResponseEntity(bookService.getAvailableBooks(), HttpStatus.OK);
    }

    @PostMapping("borrow/{userId}")
    public ResponseEntity BorrowABook(@RequestBody Map<String, List<Long>> bookIds, @PathVariable long userId) throws JsonProcessingException {
        List<Long> booklist = bookIds.get("bookIds");
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        User user = userService.findUserById(userId);
        try {
            int booksInTransaction = booklist.size();

            if (booksInTransaction < Constants.MAX_LIMIT_TO_TRANSACTION_TO_BORROW) {
                List<BorrowedBook> userBorrowedList = user.getBorrowersList();//borrowedBookService.getBorrowedBooksByUser(userId);
                int userBorrowedListCount = userBorrowedList.isEmpty() ? 0 : userBorrowedList.size();
                if (userBorrowedListCount < Constants.MAX_LIMIT_TO_BORROW) {
                    if (userBorrowedListCount + booksInTransaction < Constants.MAX_LIMIT_TO_BORROW) {
                        if (bookService.isBooksAvailableToBorrow(booklist, userBorrowedList)) {
                            borrowedBooks = bookService.borrowBooks(booklist, userId);

                        }
                    } else {
                        int presentBorrowlimit = Constants.MAX_LIMIT_TO_BORROW - userBorrowedListCount;
                        return new ResponseEntity("You can only borrow : " + presentBorrowlimit + " books", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity("You reached Max limit For borrow :" + Constants.MAX_LIMIT_TO_BORROW, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity("Only " + Constants.MAX_LIMIT_TO_TRANSACTION_TO_BORROW + " books per transaction is allowed", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
//        Book book = bookService.findBookByID(bookId);
//        User user = userService.findUserById(userId);
//        if (bookService.isBookAvailableToBorrow(bookId, userId)) {
//
//            //borrow book
//        } else {
//            return new ResponseEntity("Book with id" + bookId + "Not available to borrow", HttpStatus.BAD_REQUEST);
//        }
//        BorrowedBook borrowedBook = borrowedBookService.createBorrowedBook(book, user);
//        return new ResponseEntity(borrowedBook, HttpStatus.CREATED);
        return new ResponseEntity(borrowedBooks, HttpStatus.OK);
    }
}
