// MediaController.java
package com.app.web_reactive.server;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.app.web_reactive.data.Media;

@RestController
@RequestMapping("/media")
public class MediaController {

      @Autowired
      private DatabaseClient databaseClient;

      // Create new media
      @PostMapping("/create")
      public Mono<Long> createMedia(@RequestBody Media media) {
            String sql = "INSERT INTO media (title, release_date, average_rating, type) VALUES (:title, :release_date, :average_rating, :type)";

            try {
                  LocalDate releaseDate = media.getRelease_date();
                  return databaseClient.sql(sql)
                              .bind("title", media.getTitle())
                              .bind("release_date", releaseDate)
                              .bind("average_rating", media.getAverage_rating())
                              .bind("type", media.isType())
                              .fetch()
                              .rowsUpdated()
                              .doOnSuccess(rows -> {

                              });
            } catch (DateTimeParseException e) {
                  return Mono.error(
                              new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid release date format."));
            }
      }

      @GetMapping("/test")
      public Mono<String> test() {
            return Mono.just("Server is up and running");
      }

      // Read all media
      @GetMapping("/readall")
      public Flux<Media> getAllMedia() {
            String sql = "SELECT * FROM media";
            return databaseClient.sql(sql)
                        .map((row, metadata) -> new Media(
                                    row.get("identifier", Long.class),
                                    row.get("title", String.class),
                                    row.get("release_date", LocalDate.class),
                                    row.get("average_rating", Float.class),
                                    row.get("type", Boolean.class)))
                        .all();
      }

      /* ------------------------------------------------------------ */
      /* FEITO ATÉ AQUI */
      /* ------------------------------------------------------------ */

      // Update specific media
      @PutMapping("/update/{id}")
      public Mono<Long> updateMedia(@PathVariable long id, @RequestBody Media media) {
            String sql = "UPDATE media SET title = :title, release_date = :release_date, average_rating = :average_rating, type = :type WHERE id = :id";
            return databaseClient.sql(sql)
                        .bind("title", media.getTitle())
                        .bind("release_date", media.getRelease_date())
                        .bind("average_rating", media.getAverage_rating())
                        .bind("type", media.isType())
                        .bind("id", id)
                        .fetch()
                        .rowsUpdated();
      }

      // Delete specific media
      @DeleteMapping("/delete/{id}")
      public Mono<Long> deleteMedia(@PathVariable long id) {
            String sql = "DELETE FROM media WHERE id = :id";
            return databaseClient.sql(sql)
                        .bind("id", id)
                        .fetch()
                        .rowsUpdated();
      }
}
