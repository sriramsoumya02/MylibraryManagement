package com.example.mylibrarymanagement.repository;

import java.util.List;

public interface BookRepositoryCustom {
    void updateAvailableBooksByMinusOne(List<Long> books);
}
