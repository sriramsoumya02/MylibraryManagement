package com.example.mylibrarymanagement.repository;

import com.example.mylibrarymanagement.model.Book;
import com.example.mylibrarymanagement.model.User;
import com.example.mylibrarymanagement.model.WaitingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WaitingListRepository extends JpaRepository<WaitingList, Long> {
    @Query("Select w from WaitingList w Where w.book=:book AND w.user=:user")
    public WaitingList findByBookAndUser(Book book, User user);
}
