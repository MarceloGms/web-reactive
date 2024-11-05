package com.app.web_reactive.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.app.web_reactive.persistence.entity.MediaUsers;
import com.app.web_reactive.service.RelationshipService;

@RestController
@RequestMapping("/relationship")
public class RelationshipController {

    private static final Logger logger = LoggerFactory.getLogger(RelationshipController.class);

    @Autowired
    private RelationshipService relationshipService;

    @PostMapping
    public Mono<Void> associateMediaWithUser(@RequestBody MediaUsers mediaUsers) {
        logger.info("Associating media {} with user {}", mediaUsers.getMediaIdentifier(), mediaUsers.getUsersIdentifier());
        return relationshipService.associateMediaWithUser(mediaUsers);
    }

    @DeleteMapping
    public Mono<Void> disassociateMediaFromUser(@RequestBody MediaUsers mediaUsers) {
        logger.info("Disassociating media {} from user {}", mediaUsers.getMediaIdentifier(), mediaUsers.getUsersIdentifier());
        return relationshipService.disassociateMediaFromUser(mediaUsers.getMediaIdentifier(), mediaUsers.getUsersIdentifier());
    }

    // TODO: oq exatamente é para fazer no ultimo req?
    @GetMapping("/{userId}")
    public Flux<Long> getMediaByUser(@PathVariable long userId) {
        logger.info("Fetching media for user {}", userId);
        return relationshipService.getMediaByUser(userId);
    }

    @GetMapping
    public Flux<Long> getMediaUsers() {
        logger.info("Fetching media users");
        return relationshipService.getMediaUsers()
                .flatMapMany(Flux::fromIterable)
                .flatMap(Flux::fromIterable);
    }
}