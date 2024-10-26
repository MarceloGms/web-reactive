package com.app.web_reactive.api;

import com.app.web_reactive.persistence.entity.Media;
import com.app.web_reactive.service.MediaService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/media")
public class MediaController {

    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);

    @Autowired
    private MediaService mediaService;

    @PostMapping
    public Mono<Media> createMedia(@Valid @RequestBody Media media) {
        logger.info("Creating media: {}", media);
        return mediaService.createMedia(media);
    }

    @GetMapping
    public Flux<Media> getAllMedia() {
        logger.info("Fetching all media");
        return mediaService.getAllMedia();
    }

    @GetMapping("/{id}")
    public Mono<Media> getMediaById(@PathVariable Long id) {
        logger.info("Fetching media with id: {}", id);
        return mediaService.getMediaById(id);
    }

    @PutMapping("/{id}")
    public Mono<Media> updateMedia(@PathVariable Long id, @Valid @RequestBody Media media) {
        logger.info("Updating media with id: {}", id);
        return mediaService.updateMedia(id, media);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteMedia(@PathVariable Long id) {
        logger.info("Deleting media with id: {}", id);
        return mediaService.deleteMedia(id);
    }
}