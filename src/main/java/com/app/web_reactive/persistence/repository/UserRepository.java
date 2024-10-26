package com.app.web_reactive.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.app.web_reactive.persistence.entity.User;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
}
