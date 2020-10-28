package com.example.mylibrarymanagement.service;

import com.example.mylibrarymanagement.model.Book;
import com.example.mylibrarymanagement.model.User;
import com.example.mylibrarymanagement.model.WaitingList;
import com.example.mylibrarymanagement.repository.WaitingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class WaitingListServiceImpl implements WaitingListService {
    @Autowired
    WaitingListRepository waitingListRepository;

    @Override
    public WaitingList addToWaitingList(Book book, User user) {
        WaitingList isexistedWaitingList = getWaitingListByBookAndUser(book, user);
        if (isexistedWaitingList == null) {
            WaitingList waitingList = new WaitingList(book, user);
            book.addToWaitingLists(waitingList);
            user.addToWaitingLists(waitingList);
            return waitingListRepository.save(waitingList);
        } else {
            return isexistedWaitingList;
        }
    }

    @Override
    public void removeFromWaitingListBYId(Book book, User user) {
        WaitingList isexistedWaitingList = getWaitingListByBookAndUser(book, user);
        if (isexistedWaitingList != null) {
            book.removeFromWaitingLists(isexistedWaitingList);
            user.removeFromWaitingLists(isexistedWaitingList);
            waitingListRepository.deleteById(isexistedWaitingList.getId());
        }
    }

    @Override
    public WaitingList getWaitingListByBookAndUser(Book book, User user) {
        return waitingListRepository.findByBookAndUser(book, user);
    }
}
