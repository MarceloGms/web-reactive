package com.app.web_reactive.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.app.web_reactive.data.MediaUsers;

public interface MediaUsersRepository extends ReactiveCrudRepository<MediaUsers, Long> {

}
