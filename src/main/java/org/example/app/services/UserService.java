package org.example.app.services;

import org.example.web.dto.Book;
import org.example.web.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        user.setId(hashCode());
        userRepository.store(user);
    }

    public List<User> getAllUsers () {
        return  userRepository.retreiveAll();
    }
}
