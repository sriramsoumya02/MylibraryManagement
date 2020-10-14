package com.example.mylibrarymanagement.service;

import com.example.mylibrarymanagement.model.User;
import com.example.mylibrarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User findUserById(long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.isPresent() ? optionalUser.get() : null;
    }
}
