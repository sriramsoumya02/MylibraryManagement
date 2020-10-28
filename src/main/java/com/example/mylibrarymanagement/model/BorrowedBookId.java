package com.example.mylibrarymanagement.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BorrowedBookId implements Serializable {
    @Column(name = "BOOKID")
    private long bookId;
    @Column(name = "USERID")
    private long userId;

    public BorrowedBookId() {

    }

    public BorrowedBookId(long bookId, long userId) {
        this.bookId = bookId;
        this.userId = userId;
    }

    public long getBookId() {
        return bookId;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BorrowedBookId)) return false;
        BorrowedBookId that = (BorrowedBookId) o;
        return bookId == that.bookId &&
                userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, userId);
    }
}
