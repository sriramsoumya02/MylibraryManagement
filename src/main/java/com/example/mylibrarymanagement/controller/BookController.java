package com.example.mylibrarymanagement.controller;

import com.example.mylibrarymanagement.config.Constants;
import com.example.mylibrarymanagement.model.*;
import com.example.mylibrarymanagement.service.BookService;
import com.example.mylibrarymanagement.service.BorrowedBookService;
import com.example.mylibrarymanagement.service.UserService;
import com.example.mylibrarymanagement.service.WaitingListService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    private WaitingListService waitingListService;

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

    @PutMapping("{bookId}/renew/{userId}")
    public ResponseEntity RenewBook(@PathVariable long bookId, @PathVariable long userId) {
        BorrowedBook resultBook = null;
        if (bookId > 0 && userId > 0) {
            Book book = bookService.findBookByID(bookId);
            User user = userService.findUserById(userId);
            if (book != null && user != null) {
                int waitingListCount = book.getWaitingLists().size();
                BorrowedBook borrowedBook = borrowedBookService.findBorrowedBookById(new BorrowedBookId(bookId, userId));
                if (waitingListCount == 0 && borrowedBook != null && borrowedBook.getNoOfRenewals() < Constants.MAX_LIMIT_TO_RENEWAL) {
                    resultBook = bookService.renewBook(bookId, userId);
                } else {
                    return new ResponseEntity("Cant renew the book either book has more waitinglist or exceeded book max renewal limit", HttpStatus.PRECONDITION_FAILED);
                }
            } else {
                return new ResponseEntity("Invalid Book or User", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity("Please Provide valid bookId", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(resultBook, HttpStatus.OK);
    }

    @DeleteMapping("return/{userId}")
    public ResponseEntity returnABook(@RequestBody Map<String, List<Long>> bookIds, @PathVariable long userId) {
        try {
            List<Long> bookList = bookIds.get("bookIds");
            bookService.returnBook(bookList, userId);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/waitinglist")
    public ResponseEntity addToWaitingList(@RequestBody Map<String, Long> input) {
        WaitingList waitingList = null;
        try {
            long bookId = input.get("bookId");
            long userId = input.get("userId");
            Book book = bookId != 0 ? bookService.findBookByID(bookId) : null;
            User user = userId != 0 ? userService.findUserById(userId) : null;
            boolean isAvailableToBorrow = bookService.isBooksAvailableToBorrow(Arrays.asList(book.getId()), user.getBorrowersList());
            if (bookId != 0 && userId != 0 && book != null && user != null & !isAvailableToBorrow) {
                waitingList = waitingListService.addToWaitingList(book, user);
            } else {
                String msg = isAvailableToBorrow ? "Book is Available to borrow" : "userId or Book Id value is not valid";
                return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(waitingList, HttpStatus.OK);
    }

    @DeleteMapping("/waitinglist")
    public ResponseEntity removeWaitingList(@RequestBody Map<String, Long> input) {
        try {
            long bookId = input.get("bookId");
            long userId = input.get("userId");
            Book book = bookId != 0 ? bookService.findBookByID(bookId) : null;
            User user = userId != 0 ? userService.findUserById(userId) : null;
            if (bookId != 0 && userId != 0 && book != null && user != null) {
                waitingListService.removeFromWaitingListBYId(book, user);
            }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/overdue/{userId}")
    public ResponseEntity getOverdueAmount(@PathVariable long userId) {
        Map response;
        try {
            User user = userService.findUserById(userId);
            if (user != null) {
                response = bookService.calculateOverdueAmount(user);
            } else {
                return new ResponseEntity("Not valid User", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }


}
