package com.tamaspinter.userservice.service;

import com.tamaspinter.userservice.exceptions.UserAlreadyExistsException;
import com.tamaspinter.userservice.exceptions.UserNotFoundException;
import com.tamaspinter.userservice.exceptions.WeakPasswordException;
import com.tamaspinter.userservice.model.User;
import com.tamaspinter.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bcryptEncoder;

    static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";

    public User register(User newUser) throws WeakPasswordException, UserAlreadyExistsException {
        if (userRepository.findUserByUsername(newUser.getUsername()) != null) {
            throw new UserAlreadyExistsException(newUser);
        }
        if (!newUser.getPassword().matches(PASSWORD_REGEX)) throw new WeakPasswordException();
        newUser.setPassword(bcryptEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    public User getUserData(Long userId) throws UserNotFoundException {
        try {
            return userRepository.findUserById(userId);
        } catch (Exception e) {
            throw new UserNotFoundException(userId);
        }
    }

    public User updateProfile(User editedUser) throws UserAlreadyExistsException {
        User user = userRepository.findUserById(editedUser.getId());
        User existingUser = userRepository.findUserByUsername(editedUser.getUsername());
        if (existingUser != null && !existingUser.getId().equals(user.getId()))
            throw new UserAlreadyExistsException(existingUser);
        editedUser.setPassword(user.getPassword());
        user = editedUser;
        return userRepository.save(user);
    }

    public void editPassword(Long userId, String newPassword) throws WeakPasswordException {
        if (!newPassword.matches(PASSWORD_REGEX)) throw new WeakPasswordException();
        log.info("New password: " + newPassword);
        User user = userRepository.findUserById(userId);
        String encryptedPassword = bcryptEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }

    public User findUserById(Long userId) {
        return userRepository.findUserById(userId);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
