package com.example.mylibrarymanagement.repository;

import com.example.mylibrarymanagement.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
    List<Book> findAllByAvailablecopiesGreaterThan(int availableCopies);

    List<Book> findByIdIn(List<Long> bookId);

    //UPDATE LocalizedText L SET L.content = :content WHERE EXISTS (SELECT T FROM QuestionGroupText T WHERE T.localizedText.id = L.id AND T.generatedId = :generatedId)
    //
//    @Query(value = "SELECT u FROM User u WHERE u.name IN :names")
//    List<User> findUserByNameList(@Param("names") Collection<String> names);
//    @Query("UPDATE Book b SET b.availablecopies =: LAST_INSERT_ID(b.availablecopies-1) WHERE b.id IN :books")
//    List<Book> updateAvailableBooksByMinusOne(@Param("books") Collection<Long> books);
}
