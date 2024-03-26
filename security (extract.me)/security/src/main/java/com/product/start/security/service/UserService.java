package com.product.start.security.service;

import com.product.start.security.dto.UserDTO;
import com.product.start.security.entity.User;
import com.product.start.security.storage.UserStorage;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@AllArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserStorage userStorage;

    public User findUserByUsername(String username) {
        return userStorage.findUserByName(username);
    }

    public User addUser(UserDTO userDTO) {
        User user = new User(userDTO.getUsername(), bCryptPasswordEncoder.encode(userDTO.getPassword()), List.of("USER"));
        userStorage.addUser(user);
        return user;
    }
}
