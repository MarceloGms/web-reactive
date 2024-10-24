package com.app.web_reactive.repository;

import com.app.web_reactive.data.User;

import reactor.core.publisher.Flux;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
      Flux<User> findByName(String name);
      Flux<User> findByAge(int age);
}
