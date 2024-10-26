// MediaRepository.java
package com.app.web_reactive.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.app.web_reactive.persistence.entity.Media;

@Repository
public interface MediaRepository extends ReactiveCrudRepository<Media, Long> {
}
