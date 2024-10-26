package com.app.web_reactive.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.app.web_reactive.persistence.entity.Media;
import com.app.web_reactive.service.RelationshipService;

@RestController
@RequestMapping("/relationship")
public class RelationshipController {

    private static final Logger logger = LoggerFactory.getLogger(RelationshipController.class);

    private final RelationshipService relationshipService;

    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @PostMapping("/associate")
    public Mono<Void> associateMediaWithUser(@RequestParam long media_identifier,
                                             @RequestParam long users_identifier) {
        logger.info("Associating media {} with user {}", media_identifier, users_identifier);
        return relationshipService.associateMediaWithUser(media_identifier, users_identifier);
    }

    @DeleteMapping("/disassociate")
    public Mono<Void> disassociateMediaFromUser(@RequestParam long media_identifier,
                                                @RequestParam long users_identifier) {
        logger.info("Disassociating media {} from user {}", media_identifier, users_identifier);
        return relationshipService.disassociateMediaFromUser(media_identifier, users_identifier);
    }

    @GetMapping("/user/{userId}/media")
    public Flux<Media> getMediaByUser(@PathVariable long userId) {
        logger.info("Fetching media for user {}", userId);
        return relationshipService.getMediaByUser(userId);
    }
}