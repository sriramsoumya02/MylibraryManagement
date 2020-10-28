package com.example.mylibrarymanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BORROWED_BOOK")
public class BorrowedBook {
    @EmbeddedId
    @Column(name = "Id")
    private BorrowedBookId id;
    /*
     * @MapsId means that we tie those fields to a part of the key, and they're the foreign keys of a many-to-one relationship.
     * We need it, because as we mentioned above, in the composite key we can't have entities.
     * */
    @JoinColumn(name = "BOOKID", insertable = false, updatable = false)
    @MapsId("bookId")
    @JsonBackReference
    @ManyToOne
    private Book book;
    @JoinColumn(name = "USERID", insertable = false, updatable = false)
    @MapsId("userId")
    @JsonBackReference
    @ManyToOne
    private User user;
    @Column(name = "NO_OF_RENEWALS")
    private int noOfRenewals;
    @Column(name = "ISSUED_DATE")
    private LocalDateTime issuedDate;
    @Column(name = "DUE_DATE")
    private LocalDateTime dueDate;
    @CreationTimestamp
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @UpdateTimestamp
    @Column(name = "UPDATED_DATE", nullable = false)
    private LocalDateTime updatedDate;

    protected BorrowedBook() {
    }

    public BorrowedBook(Book book, User user, LocalDateTime issuedDate, LocalDateTime dueDate) {
        this.id = new BorrowedBookId(book.getId(), user.getId());
        this.book = book;
        this.user = user;
        this.noOfRenewals = 0;
        this.issuedDate = issuedDate;
        this.dueDate = dueDate;
    }

    public BorrowedBookId getId() {
        return id;
    }

    public void setId(BorrowedBookId id) {
        this.id = id;
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

    public int getNoOfRenewals() {
        return noOfRenewals;
    }

    public void setNoOfRenewals(int noOfRenewals) {
        this.noOfRenewals = noOfRenewals;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}
