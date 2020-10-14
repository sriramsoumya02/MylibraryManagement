package com.example.mylibrarymanagement.service;

import com.example.mylibrarymanagement.CustomException;
import com.example.mylibrarymanagement.config.Constants;
import com.example.mylibrarymanagement.model.Book;
import com.example.mylibrarymanagement.model.BorrowedBook;
import com.example.mylibrarymanagement.model.User;
import com.example.mylibrarymanagement.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private static Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BorrowedBookService borrowedBookService;
    @Autowired
    UserService userService;

    public List<Book> getAvailableBooks() {
        return bookRepository.findAllByAvailablecopiesGreaterThan(Constants.AVAILABLE_COPIES);
    }

    @Override
    public Book createBook(Book book) {
        //  Optional<Book> existingBook = bookRepository.findById(book.getId());
//        long id = book.getId();
//        logger.info(("UpdateBook --Book id--" + id));
//        if (id > 0) {
//            Book updateBook = bookRepository.findById(book.getId()).get();
//            updateBook.setAuthor(book.getAuthor());
//            updateBook.setTitle(book.getTitle());
//            updateBook.setAvailableCopies(book.getAvailableCopies());
//            updateBook.setTotalCopies(book.getTotalCopies());
//            return bookRepository.save(updateBook);
//        } else {
        return bookRepository.save(book);
        // }
    }

    @Override
    public void DeleteBook(long bookid) {
        bookRepository.deleteById(bookid);
    }

    @Override
    public Book findBookByID(long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        return optionalBook.isPresent() ? optionalBook.get() : null;
    }

    @Override
    public List<BorrowedBook> borrowBooks(List<Long> bookIds, long userId) {
        List<Book> books = bookRepository.findByIdIn(bookIds);
        User user = userService.findUserById(userId);
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        for (Book book : books) {
            BorrowedBook borrowedBook = new BorrowedBook(book, user, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
            borrowedBooks.add(borrowedBook);
            user.addToBorrowerList(borrowedBook);
            book.addToBorrowerList(borrowedBook);
        }
        borrowedBookService.createBorrowedBooks(borrowedBooks);
        bookRepository.updateAvailableBooksByMinusOne(bookIds);
        return borrowedBooks;
    }

    @Override
    public boolean isBooksAvailableToBorrow(List<Long> bookIds, List<BorrowedBook> borrowedBooks) throws CustomException {
        List<Book> books = bookRepository.findByIdIn(bookIds);
        List<Long> borrowedBookIds = borrowedBooks.stream().map(borrowedBook -> borrowedBook.getBook().getId()).collect(Collectors.toList());//borrowedBookService.getBorrowedBookIdsByUser(userId);
        for (Book book : books) {
            if (book.getAvailablecopies() <= Constants.AVAILABLE_COPIES || borrowedBookIds.contains(book.getId())) {
                throw new CustomException("Book id : " + book.getId() + " is borrowed by user ");
            }
        }
        return true;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
