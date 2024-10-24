package com.app.web_reactive.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.app.web_reactive.data.User;
import com.app.web_reactive.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public Mono<ResponseEntity<User>> createUser(@RequestBody User user) {
        logger.info("Creating user: {}", user.getName());
        return userRepository.save(user)
            .map(savedUser -> {
                logger.info("User created successfully with ID: {}", savedUser.getIdentifier());
                return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
            })
            .onErrorResume(e -> {
                logger.error("Error creating user: {}", e.getMessage());
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
            });
    }

    @GetMapping("/read")
    public Mono<ResponseEntity<Flux<User>>> readUsers() {
        logger.info("Reading users");
        return Mono.just(
            ResponseEntity.ok(userRepository.findAll())
        ).onErrorResume(e -> {
            logger.error("Error reading users: {}", e.getMessage());
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Flux.empty()));
        });
    }

    @GetMapping("/read/{id}")
    public Mono<ResponseEntity<User>> readUserById(@PathVariable Long id) {
        logger.info("Reading user with ID: {}", id);
        return userRepository.findById(id)
            .map(user -> ResponseEntity.ok(user))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null))
            .onErrorResume(e -> {
                logger.error("Error reading user: {}", e.getMessage());
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
            });
    }

    // Add update and delete methods using the repository if needed.
}
