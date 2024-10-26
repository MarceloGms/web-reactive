package com.app.web_reactive.api;

import com.app.web_reactive.persistence.entity.User;
import com.app.web_reactive.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping
    public Mono<User> createUser(@Valid @RequestBody User user) {
        logger.info("Creating user: {}", user);
        return userService.createUser(user);
    }

    @GetMapping
    public Flux<User> getAllUsers() {
        logger.info("Fetching all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable Long id) {
        logger.info("Fetching user with id: {}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public Mono<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        logger.info("Updating user with id: {}", id);
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with id: {}", id);
        return userService.deleteUser(id);
    }
}
