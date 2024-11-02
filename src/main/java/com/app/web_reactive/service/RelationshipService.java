package com.app.web_reactive.service;

import com.app.web_reactive.persistence.entity.MediaUsers;
import com.app.web_reactive.persistence.repository.RelationshipRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RelationshipService {

    private static final Logger logger = LoggerFactory.getLogger(RelationshipService.class);

    @Autowired
    private RelationshipRepository mediaUsersRepository;

    @Autowired
    private DatabaseClient databaseClient;

    public Mono<Void> associateMediaWithUser(long mediaIdentifier, long usersIdentifier) {
        MediaUsers mediaUsers = new MediaUsers(mediaIdentifier, usersIdentifier);
        return mediaUsersRepository.save(mediaUsers)
            .doOnSuccess(c -> logger.info("Associated media {} with user {}", mediaIdentifier, usersIdentifier))
            .then();
    }

    public Mono<Void> disassociateMediaFromUser(long mediaIdentifier, long usersIdentifier) {
        // TODO: Talvez estas queries deviam estar no repository e nao aqui
        String sql = """
            DELETE FROM media_users
            WHERE media_identifier = :mediaId AND users_identifier = :userId
        """;

        return databaseClient.sql(sql)
            .bind("mediaId", mediaIdentifier)
            .bind("userId", usersIdentifier)
            .fetch()
            .rowsUpdated()
            .doOnSuccess(rows -> {
                if (rows > 0) {
                    logger.info("Disassociated media {} from user {}", mediaIdentifier, usersIdentifier);
                } else {
                    logger.warn("No association found for media {} and user {}", mediaIdentifier, usersIdentifier);
                }
            })
            .then();
    }

    // TODO: Read  relationship.  This  service  can  only  return  the  identifiers  of  some 
    /* media/user, not the entire media data, i.e., students should not create a service 
    that immediately provides, say, a user with all data of all the user’s media. */
    public Flux<Long> getMediaByUser(long userId) {
        String sql = """
            SELECT m.identifier FROM media m
            JOIN media_users mu ON m.identifier = mu.media_identifier
            WHERE mu.users_identifier = :userId
        """;
    
        return databaseClient.sql(sql)
            .bind("userId", userId)
            .map((row, metadata) -> row.get("identifier", Long.class))
            .all()
            .doOnNext(id -> logger.info("Retrieved media identifier: {}", id));
    }
}
