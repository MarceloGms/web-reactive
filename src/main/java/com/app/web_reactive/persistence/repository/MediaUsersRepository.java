package com.app.web_reactive.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.app.web_reactive.persistence.entity.MediaUsers;

public interface MediaUsersRepository extends ReactiveCrudRepository<MediaUsers, Long> {

}
