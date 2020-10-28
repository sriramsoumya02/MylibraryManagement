package com.example.mylibrarymanagement.service;

import com.example.mylibrarymanagement.model.Book;
import com.example.mylibrarymanagement.model.User;
import com.example.mylibrarymanagement.model.WaitingList;

public interface WaitingListService {
    //public WaitingList addToWaitingList(WaitingList waitingList);
    public WaitingList addToWaitingList(Book book, User user);

    public void removeFromWaitingListBYId(Book book, User user);

    public WaitingList getWaitingListByBookAndUser(Book book, User user);

}
