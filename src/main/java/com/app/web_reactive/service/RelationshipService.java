package com.app.web_reactive.service;

import com.app.web_reactive.persistence.entity.MediaUsers;
import com.app.web_reactive.persistence.entity.User;
import com.app.web_reactive.persistence.entity.Media;
import com.app.web_reactive.persistence.repository.MediaRepository;
import com.app.web_reactive.persistence.repository.RelationshipRepository;
import com.app.web_reactive.persistence.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RelationshipService {

    private static final Logger logger = LoggerFactory.getLogger(RelationshipService.class);

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private UserRepository userRepository;

    public Mono<Void> associateMediaWithUser(MediaUsers mediaUsers) {
        return relationshipRepository.save(mediaUsers)
                .doOnSuccess(c -> logger.info("Associated media {} with user {}", mediaUsers.getMediaIdentifier(), mediaUsers.getUsersIdentifier()))
                .doOnError(error -> {
                    logger.error("Error associating media {} with user {}", mediaUsers.getMediaIdentifier(), mediaUsers.getUsersIdentifier(), error);
                    if (error instanceof DataIntegrityViolationException) {
                        throw (DataIntegrityViolationException) error;
                    } else {
                        throw new RuntimeException("Unexpected error occurred while associating media with user", error);
                    }
                })
                .then();
    }

    public Mono<Void> disassociateMediaFromUser(long mediaIdentifier, long usersIdentifier) {
        return relationshipRepository.deleteByMediaIdentifierAndUsersIdentifier(mediaIdentifier, usersIdentifier)
                .doOnSuccess(rows -> {
                    if (rows > 0) {
                        logger.info("Disassociated media {} from user {}", mediaIdentifier, usersIdentifier);
                    } else {
                        logger.warn("No association found for media {} and user {}", mediaIdentifier, usersIdentifier);
                    }
                })
                .doOnError(error -> {
                    logger.error("Error disassociating media {} from user {}", mediaIdentifier, usersIdentifier, error);
                    if (error instanceof DataIntegrityViolationException) {
                        throw (DataIntegrityViolationException) error;
                    } else {
                        throw new RuntimeException("Unexpected error occurred while disassociating media from user", error);
                    }
                })
                .then();
    }

    public Flux<Long> getMediaByUser(long userId) {
        return relationshipRepository.findByUsersIdentifier(userId)
                .flatMap(mediaUser -> mediaRepository.findById(mediaUser.getMediaIdentifier()))
                .map(Media::getIdentifier)
                .doOnNext(id -> logger.info("Retrieved media identifier: {}", id))
                .doOnError(error -> logger.error("Error retrieving media for user with id: {}", userId, error));
    }

    public Flux<Long> getUsersByMedia(long mediaId) {
        return relationshipRepository.findByMediaIdentifier(mediaId)
                .flatMap(mediaUser -> userRepository.findById(mediaUser.getUsersIdentifier()))
                .map(User::getIdentifier)
                .doOnNext(id -> logger.info("Retrieved user identifier: {}", id))
                .doOnError(error -> logger.error("Error retrieving users for media with id: {}", mediaId, error));
    }
}
