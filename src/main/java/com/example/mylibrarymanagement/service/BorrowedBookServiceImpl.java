package com.example.mylibrarymanagement.service;

import com.example.mylibrarymanagement.model.Book;
import com.example.mylibrarymanagement.model.BorrowedBook;
import com.example.mylibrarymanagement.model.BorrowedBookId;
import com.example.mylibrarymanagement.model.User;
import com.example.mylibrarymanagement.repository.BorrowedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BorrowedBookServiceImpl implements BorrowedBookService {

    @Autowired
    BorrowedBookRepository borrowedBookRepository;

    @Override
    public BorrowedBook createBorrowedBook(Book book, User user) {
        return borrowedBookRepository.save(new BorrowedBook(book, user, LocalDateTime.now(), LocalDateTime.now().plusDays(30)));
    }


    @Override
    public List<BorrowedBook> createBorrowedBooks(List<BorrowedBook> borrowedBooks) {
        return borrowedBookRepository.saveAll(borrowedBooks);
    }


    @Override
    public BorrowedBook findBorrowedBookById(BorrowedBookId borrowedBookId) {
        Optional<BorrowedBook> borrowedBook = borrowedBookRepository.findById(borrowedBookId);
        return borrowedBook.isPresent() ? borrowedBook.get() : null;
    }

    @Override
    public List<BorrowedBook> getBorrowedBooksByUser(User user) {
        return borrowedBookRepository.findAllByUser(user);
    }

    @Override
    public BorrowedBook updateBorrowedBook(BorrowedBook borrowedBook) {
        return borrowedBookRepository.save(borrowedBook);
    }

//    @Override
//    public List<Book> getBorrowedBookIdsByUser(User user) {
//        return borrowedBookRepository.getBookByUser(user);
//    }
}
