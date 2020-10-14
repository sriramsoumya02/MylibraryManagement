package com.example.mylibrarymanagement.repository;

import com.example.mylibrarymanagement.model.Book;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void updateAvailableBooksByMinusOne(List<Long> books) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Book> cu = cb.createCriteriaUpdate(Book.class);
        Root<Book> bookRoot = cu.from(Book.class);
        cu.set(bookRoot.<Integer>get("availablecopies"), cb.diff(bookRoot.<Integer>get("availablecopies"), 1));
        cu.where(bookRoot.get("id").in(books));
        entityManager.createQuery(cu).executeUpdate();
    }
}
