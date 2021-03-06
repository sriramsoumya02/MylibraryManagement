package com.example.mylibrarymanagement.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String email;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    List<BorrowedBook> borrowersList = new ArrayList<BorrowedBook>();
    @CreationTimestamp
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
    @UpdateTimestamp
    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    List<WaitingList> waitingLists = new ArrayList<>();

    protected User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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


    public List<WaitingList> getWaitingLists() {
        return waitingLists;
    }

    public boolean removeFromWaitingLists(WaitingList waitingList) {
        return this.waitingLists.remove(waitingList);
    }

    public WaitingList addToWaitingLists(WaitingList waitingList) {
        this.waitingLists.add(waitingList);
        return waitingList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
