package com.example.mylibrarymanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WAITING_LIST", uniqueConstraints = @UniqueConstraint(columnNames = {"Book_ID", "User_Id"}))
public class WaitingList {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "Book_ID", nullable = false)
    private Book book;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;
    @CreationTimestamp
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @PreUpdate
    private void preUpdate() {
        throw new UnsupportedOperationException(" waiting list is not updatable");
    }

    protected WaitingList() {
    }

    public WaitingList(Book book, User user) {
        this.book = book;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

}
