package com.app.web_reactive.service;

import com.app.web_reactive.persistence.entity.Media;
import com.app.web_reactive.persistence.repository.MediaRepository;
import com.app.web_reactive.exception.ResourceNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MediaService {

    private static final Logger logger = LoggerFactory.getLogger(MediaService.class);

    @Autowired
    private MediaRepository mediaRepository;

    public Mono<Media> createMedia(Media media) {

        return mediaRepository.save(media)
                .doOnSuccess(createdMedia -> logger.info("Created media: {}", createdMedia))
                .doOnError(error -> {
                    logger.error("Error creating media", error);
                    if (error instanceof DataIntegrityViolationException) {
                        throw (DataIntegrityViolationException) error;
                    } else {
                        throw new RuntimeException("Unexpected error occurred while creating media", error);
                    }
                });
    }

    public Flux<Media> getAllMedia() {
        return mediaRepository.findAll()
                .doOnComplete(() -> logger.info("Fetched all media"))
                .doOnError(error -> logger.error("Error fetching all media", error));
    }

    public Mono<Media> getMediaById(Long id) {
        return mediaRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("Media not found with id: {}", id);
                    return Mono.error(new ResourceNotFoundException("Media not found with id " + id));
                }))
                .doOnNext(media -> logger.info("Fetched media: {}", media))
                .doOnError(error -> logger.error("Error fetching media with id: {}", id, error));
    }

    public Mono<Media> updateMedia(Long id, Media media) {
        return mediaRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("Media not found with id: {}", id);
                    return Mono.error(new ResourceNotFoundException("Media not found with id " + id));
                }))
                .flatMap(existingMedia -> {
                    existingMedia.setTitle(media.getTitle());
                    existingMedia.setRelease_date(media.getRelease_date());
                    existingMedia.setAverage_rating(media.getAverage_rating());
                    existingMedia.setType(media.isType());
                    return mediaRepository.save(existingMedia);
                })
                .doOnSuccess(updatedMedia -> logger.info("Updated media: {}", updatedMedia))
                .doOnError(error -> {
                    logger.error("Error updating media with id: {}", id, error);
                    if (error instanceof DataIntegrityViolationException) {
                        throw (DataIntegrityViolationException) error;
                    } else {
                        throw new RuntimeException("Unexpected error occurred while updating media with id " + id,
                                error);
                    }
                });
    }

    @Autowired
    private DatabaseClient databaseClient;

    public Mono<Void> deleteMedia(Long id) {
        return mediaRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("Media not found with id: {}", id);
                    return Mono.error(new ResourceNotFoundException("Media not found with id " + id));
                }))
                .flatMap(media -> {
                    // Check for associations in media_users table
                    String checkAssociationsQuery = """
                                SELECT COUNT(*) FROM media_users WHERE media_identifier = :mediaId
                            """;
                    return databaseClient.sql(checkAssociationsQuery)
                            .bind("mediaId", id)
                            .map(row -> row.get(0, Long.class))
                            .one()
                            .flatMap(count -> {
                                if (count == 0) {
                                    // No associations, proceed with delete
                                    return mediaRepository.deleteById(id)
                                            .doOnSuccess(unused -> logger.info("Deleted media with id: {}", id));
                                } else {
                                    logger.warn("Cannot delete media with id {} because it is associated with users",
                                            id);
                                    return Mono.error(new RuntimeException("Cannot delete media, associations found"));
                                }
                            });
                })
                .doOnError(error -> logger.error("Error deleting media with id: {}", id, error));
    }

}