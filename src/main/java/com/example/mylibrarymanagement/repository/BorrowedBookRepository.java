package com.example.mylibrarymanagement.repository;

import com.example.mylibrarymanagement.model.BorrowedBook;
import com.example.mylibrarymanagement.model.BorrowedBookId;
import com.example.mylibrarymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, BorrowedBookId>, JpaSpecificationExecutor<BorrowedBook> {
    List<BorrowedBook> findAllByUser(User user);

    List<BorrowedBook> findAllByUserAndDueDateLessThan(User user, LocalDateTime localDateTime);
    //List<Book> getBookByUser(User user);
}
