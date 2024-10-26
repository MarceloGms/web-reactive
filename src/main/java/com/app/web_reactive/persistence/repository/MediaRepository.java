// MediaRepository.java
package com.app.web_reactive.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.app.web_reactive.persistence.entity.Media;

import reactor.core.publisher.Flux;

@Repository
public interface MediaRepository extends ReactiveCrudRepository<Media, Long> {
    // Custom query method to find media by title
    Flux<Media> findByTitle(String title);
}
