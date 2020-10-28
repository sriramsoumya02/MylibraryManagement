package com.example.mylibrarymanagement.service;

import com.example.mylibrarymanagement.CustomException;
import com.example.mylibrarymanagement.config.Constants;
import com.example.mylibrarymanagement.model.Book;
import com.example.mylibrarymanagement.model.BorrowedBook;
import com.example.mylibrarymanagement.model.BorrowedBookId;
import com.example.mylibrarymanagement.model.User;
import com.example.mylibrarymanagement.repository.BookRepository;
import com.example.mylibrarymanagement.repository.BorrowedBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class BookServiceImpl implements BookService {
    private static Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    @Autowired
    BookRepository bookRepository;

    @Autowired
    BorrowedBookRepository borrowedBookRepository;

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
            //borrowedBook.setUser(user);
            //borrowedBook.setBook(book);
            borrowedBooks.add(borrowedBook);
            user.addToBorrowerList(borrowedBook);
            book.addToBorrowerList(borrowedBook);
        }
        List<BorrowedBook> resultBooks = borrowedBookService.createBorrowedBooks(borrowedBooks);
        bookRepository.updateAvailableBooksByMinusOne(bookIds);
        return resultBooks;
    }

    @Override
    public BorrowedBook renewBook(long bookId, long userId) {
        BorrowedBook borrowedBook = borrowedBookService.findBorrowedBookById(new BorrowedBookId(bookId, userId));
        int renewals = borrowedBook.getNoOfRenewals();
        LocalDateTime issuedate = LocalDateTime.now();
        borrowedBook.getBook().removeFromBorrowerList(borrowedBook);
        borrowedBook.getUser().removeFromBorrowerList(borrowedBook);
        borrowedBook.setIssuedDate(issuedate);
        borrowedBook.setNoOfRenewals(renewals + 1);
        borrowedBook.setDueDate(issuedate.plusDays(30));
        borrowedBook.getBook().addToBorrowerList(borrowedBook);
        borrowedBook.getUser().addToBorrowerList(borrowedBook);
        return borrowedBookService.updateBorrowedBook(borrowedBook);
    }

    @Override
    public boolean isBooksAvailableToBorrow(List<Long> bookIds, List<BorrowedBook> borrowedBooks) throws CustomException {
        List<Book> books = bookRepository.findByIdIn(bookIds);
        List<Long> borrowedBookIds = borrowedBooks.stream().map(borrowedBook -> borrowedBook.getBook().getId()).collect(Collectors.toList());//borrowedBookService.getBorrowedBookIdsByUser(userId);
        for (Book book : books) {
            if (borrowedBookIds.contains(book.getId())) {
                throw new CustomException("Book id : " + book.getId() + " is borrowed by user ");
            } else if (book.getAvailablecopies() <= Constants.AVAILABLE_COPIES) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void returnBook(List<Long> bookList, long userId) throws CustomException {
        try {
            User user = userService.findUserById(userId);
            // logger.info("Soumya :" + borrowedBookRepository.getBookByUser(user).toString() + " :" + borrowedBookService.getBorrowedBookIdsByUser(user).toString());
            HashSet<Long> borrowedBookIds = borrowedBookService.getBorrowedBooksByUser(user).stream().map(book -> book.getBook().getId()).collect(Collectors.toCollection(HashSet::new));
            for (long bookid : bookList) {
                if (borrowedBookIds.contains(bookid)) {
                    BorrowedBookId borrowedBookId = new BorrowedBookId(bookid, userId);
                    BorrowedBook borrowedBook = borrowedBookRepository.findById(borrowedBookId).get();
                    Book book = bookRepository.findById(bookid).get();
                    user.removeFromBorrowerList(borrowedBook);
                    book.removeFromBorrowerList(borrowedBook);
                    borrowedBookRepository.deleteById(borrowedBookId);
                    int availableBooks = book.getAvailablecopies();
                    book.setAvailablecopies(availableBooks + 1);
                    bookRepository.save(book);
                } else {
                    throw new CustomException("Book id: " + bookid + " is not borrowed by User " + userId);
                }
            }
        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }
    }

    public Map calculateOverdueAmount(User user) throws CustomException {
        try {
            LocalDateTime todayDate = LocalDateTime.now();
            AtomicInteger overdueAmount = new AtomicInteger();
            List<BorrowedBook> borrowedBooks = borrowedBookRepository.findAllByUserAndDueDateLessThan(user, todayDate);
            borrowedBooks.stream().forEach(borrowedBook -> {
                LocalDateTime tempDateTime = LocalDateTime.from(borrowedBook.getDueDate());
                int days = (int) tempDateTime.until(todayDate, ChronoUnit.DAYS);
                overdueAmount.set(overdueAmount.addAndGet(days));
            });
            HashMap<String, Object> response = new HashMap<>();
            response.put("OverdueAmount", overdueAmount.intValue());
            response.put("OverdueBooks", borrowedBooks);
            return response;
        } catch (Exception ex) {
            throw new CustomException("Exception occured while calucating overdue" + ex.getMessage());
        }
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

}
