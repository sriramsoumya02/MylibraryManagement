package com.example.mylibrarymanagement.repository;

import com.example.mylibrarymanagement.model.BorrowedBook;
import com.example.mylibrarymanagement.model.BorrowedBookId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, BorrowedBookId> {
    List<BorrowedBook> findAllByUser(long userId);

    List<Long> getBookByUser(long userId);
}
