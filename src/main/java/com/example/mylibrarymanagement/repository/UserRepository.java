package com.example.mylibrarymanagement.repository;

import com.example.mylibrarymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
