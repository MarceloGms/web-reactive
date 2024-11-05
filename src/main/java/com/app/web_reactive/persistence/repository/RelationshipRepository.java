package com.app.web_reactive.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.app.web_reactive.persistence.entity.MediaUsers;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RelationshipRepository extends ReactiveCrudRepository<MediaUsers, Long> {
    Flux<MediaUsers> findByUsersIdentifier(long userId);

    Mono<Integer> deleteByMediaIdentifierAndUsersIdentifier(long mediaIdentifier, long usersIdentifier);
}
