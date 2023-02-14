package com.tamaspinter.userservice.controller;

import com.tamaspinter.userservice.dto.UserDTO;
import com.tamaspinter.userservice.exceptions.UserAlreadyExistsException;
import com.tamaspinter.userservice.exceptions.UserNotFoundException;
import com.tamaspinter.userservice.exceptions.WeakPasswordException;
import com.tamaspinter.userservice.model.User;
import com.tamaspinter.userservice.service.JwtUtil;
import com.tamaspinter.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping()
    public ResponseEntity<User> getUserData(@RequestParam Long userId) {
        try {
            User user = userService.getUserData(userId);
            if (user == null) throw new UserNotFoundException(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserDTO newUserDTO) {
        try {
            User newUser = newUserDTO.userFromDTO();
            User user = userService.register(newUser);
            log.info("USER CREATED: " + newUser);
            return ResponseEntity.ok(user);
        } catch (UserAlreadyExistsException exception) {
            log.error("USER ALREADY EXISTS: " + exception.getUsername());
            log.error(exception.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (WeakPasswordException exception) {
            log.error(exception.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception exception) {
            log.error("Something went wrong during registration...");
            log.error(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping
    public ResponseEntity<User> editUser(@Valid @RequestBody UserDTO editedUserDTO, @RequestHeader HttpHeaders headers) {
        try {
            User user = jwtUtil.getUserFromToken(headers);
            User editedUser = editedUserDTO.userFromDTOWithId(user.getId());
            user = userService.updateProfile(editedUser);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            log.error("Could not find user, you might need to log in again");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/edit-password")
    public ResponseEntity<String> editPassword(@RequestBody String password, @RequestHeader HttpHeaders headers) {
        try {
            User user = jwtUtil.getUserFromToken(headers);
            log.info(user.toString());
            userService.editPassword(user.getId(), password);
            return new ResponseEntity<>(user.getPassword(), HttpStatus.OK);
        } catch (WeakPasswordException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user-by-username/{username}")
    ResponseEntity<User> findUserByUsername(@PathVariable String username) {
        try {
            User user = userService.findUserByUsername(username);
            if (user == null) throw new UserNotFoundException();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user-by-userId/{userId}")
    ResponseEntity<User> findUserByUserId(@PathVariable Long userId) {
        try {
            User user = userService.findUserById(userId);
            if (user == null) throw new UserNotFoundException();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
