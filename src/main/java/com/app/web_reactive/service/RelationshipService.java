package com.app.web_reactive.service;

import com.app.web_reactive.persistence.entity.Media;
import com.app.web_reactive.persistence.entity.MediaUsers;
import com.app.web_reactive.persistence.repository.RelationshipRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

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

    public Flux<Media> getMediaByUser(long userId) {
        String sql = """
            SELECT m.* FROM media m
            JOIN media_users mu ON m.identifier = mu.media_identifier
            WHERE mu.users_identifier = :userId
        """;

        return databaseClient.sql(sql)
            .bind("userId", userId)
            .map((row, metadata) -> new Media(
                row.get("identifier", Long.class),
                row.get("title", String.class),
                row.get("release_date", LocalDate.class),
                row.get("average_rating", Float.class),
                row.get("type", Boolean.class)))
            .all()
            .doOnNext(media -> logger.info("Retrieved media: {}", media));
    }
}
