/* // MediaController.java
package com.app.web_reactive.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.app.web_reactive.persistence.MediaRepository;
import com.app.web_reactive.persistence.entity.Media;

@RestController
@RequestMapping("/media")
public class MediaController {

      @Autowired
      private MediaRepository mediaRepository;

      private static final Logger logger = LoggerFactory.getLogger(MediaController.class);

      // Create new media
      @PostMapping("/create")
      public Mono<Media> createMedia(@RequestBody Media media) {
            logger.info("Creating media: {}", media.getTitle());

            // Verification of values
            if (media.getTitle() == null || media.getTitle().isEmpty()) {
                  return Mono.error(new IllegalArgumentException("Title cannot be null or empty"));
            }

            if (media.getRelease_date() == null) {
                  return Mono.error(new IllegalArgumentException("Release date cannot be null"));
            }

            if (media.getAverage_rating() < 0 || media.getAverage_rating() > 10) {
                  logger.error("Invalid average rating: {}", media.getAverage_rating());
                  return Mono.error(new IllegalArgumentException("Average rating must be between 0 and 10"));
            }

            return mediaRepository.save(media)
                        .doOnSuccess(savedMedia -> logger.info("Media created successfully: {}", savedMedia))
                        .doOnError(e -> logger.error("Error creating media: {}", e.getMessage()));
      }

      // Read all media
      @GetMapping("/readall")
      public Flux<Media> getAllMedia() {
            logger.info("Fetching all media");
            return mediaRepository.findAll();
      }

      // Read media by title
      @GetMapping("/readbytitle/{title}")
      public Flux<Media> getMediaByTitle(@PathVariable String title) {
            title = title.replace("%20", " ");

            logger.info("Fetching media by title: {}", title);
            return mediaRepository.findByTitle(title);
      }

      // Update specific media
      @PutMapping("/update/{id}")
      public Mono<Media> updateMedia(@PathVariable long id, @RequestBody Media media) {
            logger.info("Updating media with ID: {}", id);

            // Verification of values
            if (media.getTitle() == null || media.getTitle().isEmpty()) {
                  return Mono.error(new IllegalArgumentException("Title cannot be null or empty"));
            }

            if (media.getRelease_date() == null) {
                  return Mono.error(new IllegalArgumentException("Release date cannot be null"));
            }

            if (media.getAverage_rating() < 0 || media.getAverage_rating() > 10) {
                  logger.error("Invalid average rating: {}", media.getAverage_rating());
                  return Mono.error(new IllegalArgumentException("Average rating must be between 0 and 10"));
            }

            return mediaRepository.findById(id)
                        .flatMap(existingMedia -> {
                              existingMedia.setTitle(media.getTitle());
                              existingMedia.setRelease_date(media.getRelease_date());
                              existingMedia.setAverage_rating(media.getAverage_rating());
                              existingMedia.setType(media.isType());
                              return mediaRepository.save(existingMedia);
                        })
                        .doOnSuccess(updatedMedia -> logger.info("Media updated successfully: {}", updatedMedia))
                        .doOnError(e -> logger.error("Error updating media: {}", e.getMessage()));
      }

      // Delete specific media
      @DeleteMapping("/delete/{id}")
      public Mono<Void> deleteMedia(@PathVariable long id) {
            logger.info("Deleting media with ID: {}", id);
            return mediaRepository.deleteById(id)
                        .doOnSuccess(aVoid -> logger.info("Media deleted successfully"))
                        .doOnError(e -> logger.error("Error deleting media: {}", e.getMessage()));
      }
} */