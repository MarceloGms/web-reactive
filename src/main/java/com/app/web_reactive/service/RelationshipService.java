package com.app.web_reactive.service;

import com.app.web_reactive.persistence.entity.MediaUsers;
import com.app.web_reactive.persistence.entity.Media;
import com.app.web_reactive.persistence.repository.MediaRepository;
import com.app.web_reactive.persistence.repository.RelationshipRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RelationshipService {

    private static final Logger logger = LoggerFactory.getLogger(RelationshipService.class);

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private MediaRepository mediaRepository;

    public Mono<Void> associateMediaWithUser(MediaUsers mediaUsers) {
        return relationshipRepository.save(mediaUsers)
                .doOnSuccess(c -> logger.info("Associated media {} with user {}", mediaUsers.getMediaIdentifier(), mediaUsers.getUsersIdentifier()))
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
                .then();
    }


    // TODO: este metodos podem estar mal
    public Flux<Long> getMediaByUser(long userId) {
        return relationshipRepository.findByUsersIdentifier(userId) // Fetch MediaUsers by userId
                .flatMap(mediaUser -> mediaRepository.findById(mediaUser.getMediaIdentifier())) // Fetch the media by //
                                                                                                // identifier
                .map(Media::getIdentifier) // Extract the identifier from the media entity
                .doOnNext(id -> logger.info("Retrieved media identifier: {}", id));
    }

    /*
     * output:[
     * [ 1, 1 ],
     * [ 1, 2 ],
     * [ 1, 3 ],
     * [ 2, 1 ],
     * [ 2, 2 ],
     * ]
     */
    public Mono<List<List<Long>>> getMediaUsers() {
        return relationshipRepository.findAll() // Fetch all MediaUsers entries reactively
                .map(mediaUser -> {
                    Long mediaId = mediaUser.getMediaIdentifier();
                    Long userId = mediaUser.getUsersIdentifier();
                    // Return a pair as a list
                    return List.of(mediaId, userId); // Use List.of() for simplicity and immutability
                })
                .collectList() // Collect all elements of the Flux into a single List<List<Long>>
                .doOnNext(pairs -> logger.info("Retrieved media-user pairs: {}", pairs)); // Log the entire collection
    }
}
