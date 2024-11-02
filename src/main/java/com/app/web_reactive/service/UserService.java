package com.app.web_reactive.service;

import com.app.web_reactive.persistence.entity.User;
import com.app.web_reactive.persistence.repository.UserRepository;
import com.app.web_reactive.exception.ConflictException;
import com.app.web_reactive.exception.ResourceNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.r2dbc.core.DatabaseClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DatabaseClient databaseClient;

    public Mono<User> createUser(User user) {
        return userRepository.save(user)
                .doOnSuccess(createdUser -> logger.info("Created user: {}", createdUser))
                .doOnError(error -> {
                    logger.error("Error creating user", error);
                    if (error instanceof DataIntegrityViolationException) {
                        throw (DataIntegrityViolationException) error;
                    } else {
                        throw new RuntimeException("Unexpected error occurred while creating user", error);
                    }
                });
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll()
                .doOnComplete(() -> logger.info("Fetched all users"))
                .doOnError(error -> logger.error("Error fetching all users", error));
    }

    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("User not found with id: {}", id);
                    return Mono.error(new ResourceNotFoundException("User not found with id " + id));
                }))
                .doOnNext(user -> logger.info("Fetched user: {}", user))
                .doOnError(error -> logger.error("Error fetching user with id: {}", id, error));
    }

    public Mono<User> updateUser(Long id, User user) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("User not found with id: {}", id);
                    return Mono.error(new ResourceNotFoundException("User not found with id " + id));
                }))
                .flatMap(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setAge(user.getAge());
                    existingUser.setGender(user.getGender());
                    return userRepository.save(existingUser);
                })
                .doOnSuccess(updatedUser -> logger.info("Updated user: {}", updatedUser))
                .doOnError(error -> {
                    logger.error("Error updating user with id: {}", id, error);
                    if (error instanceof DataIntegrityViolationException) {
                        throw (DataIntegrityViolationException) error;
                    } else {
                        throw new RuntimeException("Unexpected error occurred while updating user with id " + id,
                                error);
                    }
                });
    }

    public Mono<Void> deleteUser(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("User not found with id: {}", id);
                    return Mono.error(new ResourceNotFoundException("User not found with id " + id));
                }))
                .flatMap(user -> {
                    // Check for associations in media_users table
                    String checkAssociationsQuery = """
                                SELECT COUNT(*) FROM media_users WHERE users_identifier = :userId
                            """;
                    return databaseClient.sql(checkAssociationsQuery)
                            .bind("userId", id)
                            .map(row -> row.get(0, Long.class))
                            .one()
                            .flatMap(count -> {
                                if (count == 0) {
                                    // No associations, proceed with delete
                                    return userRepository.deleteById(id)
                                            .doOnSuccess(unused -> logger.info("Deleted user with id: {}", id));
                                } else {
                                    logger.warn("Cannot delete user with id {} because they are associated with media",
                                            id);
                                    return Mono.error(new ConflictException("Cannot delete user, associations found"));
                                }
                            });
                })
                .doOnError(error -> logger.error("Error deleting user with id: {}", id, error));
    }
}
