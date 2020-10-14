package com.example.mylibrarymanagement.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicUpdate
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String author;
    private String title;
    private int availablecopies;
    private int totalcopies;
    @CreationTimestamp
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
    @UpdateTimestamp
    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;
    @OneToMany(mappedBy = "book")
    List<BorrowedBook> borrowersList = new ArrayList<>();

    protected Book() {
    }

//    public Book(String author, String title) {
//        this.author = author;
//        this.title = title;
//    }

    public Book(String author, String title, int availablecopies, int totalcopies) {
        this.author = author;
        this.title = title;
        this.availablecopies = availablecopies;
        this.totalcopies = totalcopies;
    }

    public long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAvailablecopies() {
        return availablecopies;
    }

    public void setAvailablecopies(int availablecopies) {
        this.availablecopies = availablecopies;
    }

    public int getTotalcopies() {
        return totalcopies;
    }

    public void setTotalcopies(int totalcopies) {
        this.totalcopies = totalcopies;
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

    public List<BorrowedBook> getBorrowersList() {
        return borrowersList;
    }

    public BorrowedBook addToBorrowerList(BorrowedBook borrowedBook) {
        this.borrowersList.add(borrowedBook);
        return borrowedBook;
    }

    public boolean removeFromBorrowerList(BorrowedBook borrowedBook) {
        return this.borrowersList.remove(borrowedBook);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", availableCopies=" + availablecopies +
                ", totalCopies=" + totalcopies +
                '}';
    }
}
