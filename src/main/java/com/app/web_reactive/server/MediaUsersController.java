package com.app.web_reactive.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.app.web_reactive.data.Media;
import com.app.web_reactive.data.MediaUsers;
import com.app.web_reactive.repository.MediaUsersRepository;

@RestController
@RequestMapping("/relationship")
public class MediaUsersController {

        private final DatabaseClient databaseClient;

        private final MediaUsersRepository mediaUsersRepository;
        private static final Logger logger = LoggerFactory.getLogger(MediaUsersController.class);

        public MediaUsersController(MediaUsersRepository mediaUsersRepository, DatabaseClient databaseClient) {
                this.mediaUsersRepository = mediaUsersRepository;
                this.databaseClient = databaseClient;
        }

        // Associar um usuário a uma mídia
        @PostMapping("/associate")
        public Mono<Void> associateMediaWithUser(@RequestParam long media_identifier,
                        @RequestParam long users_identifier) {

                MediaUsers mediaUsers = new MediaUsers(media_identifier, users_identifier);
                return mediaUsersRepository.save(mediaUsers)
                                .doOnSuccess(c -> logger.info("Associated media {} with user {}", media_identifier,
                                                users_identifier))
                                .then();
        }

        // Desassociar um usuário de uma mídia
        @DeleteMapping("/disassociate")
        public Mono<Void> disassociateMediaFromUser(@RequestParam long media_identifier,
                        @RequestParam long users_identifier) {
                String sql = """
                                    DELETE FROM media_users
                                    WHERE media_identifier = :mediaId AND users_identifier = :userId
                                """;

                return databaseClient.sql(sql)
                                .bind("mediaId", media_identifier)
                                .bind("userId", users_identifier)
                                .fetch()
                                .rowsUpdated()
                                .doOnSuccess(rows -> {
                                        if (rows > 0) {
                                                logger.info("Disassociated media {} from user {}", media_identifier,
                                                                users_identifier);
                                        } else {
                                                logger.warn("No association found for media {} and user {}",
                                                                media_identifier, users_identifier);
                                        }
                                })
                                .then();
        }

        // Listar todas as mídias associadas a um determinado usuário
        @GetMapping("/user/{userId}/media")
        public Flux<Media> getMediaByUser(@PathVariable long userId) {
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