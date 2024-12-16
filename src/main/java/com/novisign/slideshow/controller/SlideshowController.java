package com.novisign.slideshow.controller;

import com.novisign.slideshow.exception.BadRequestException;
import com.novisign.slideshow.exception.ResourceNotFoundException;
import com.novisign.slideshow.model.Image;
import com.novisign.slideshow.model.Slideshow;
import com.novisign.slideshow.service.SlideshowService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slideshows")
public class SlideshowController {

    private final SlideshowService slideshowService;

    @Autowired
    public SlideshowController(SlideshowService slideshowService) {
        this.slideshowService = slideshowService;
    }

    @Operation(summary = "Add a new slideshow", description = "Adds a new slideshow with an array of image IDs.")
    @PostMapping("/add")
    public ResponseEntity<Slideshow> addSlideshow(@RequestBody Slideshow slideshow) {
        if (slideshow == null || slideshow.getImages() == null || slideshow.getImages().isEmpty()) {
            throw new BadRequestException("Slideshow must contain at least one image with URL and duration.");
        }
        return ResponseEntity.ok(slideshowService.addSlideshow(slideshow));
    }

    @Operation(summary = "Delete a slideshow", description = "Deletes a slideshow by its ID.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSlideshow(@PathVariable Long id) {
        slideshowService.getSlideshowById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Slideshow not found with ID: " + id));
        slideshowService.deleteSlideshow(id);
        return ResponseEntity.ok("Slideshow deleted successfully");
    }

    @Operation(summary = "Get slideshow order", description = "Retrieve the order of images in a slideshow by ID.")
    @GetMapping("/{id}/slideshowOrder")
    public ResponseEntity<List<Image>> getSlideshowOrder(@PathVariable Long id) {
        Slideshow slideshow = slideshowService.getSlideshowById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Slideshow not found with ID: " + id));
        return ResponseEntity.ok(slideshow.getImages());
    }

    @Operation(summary = "Proof of play", description = "Records proof of play when an image is shown in a slideshow.")
    @PostMapping("/{id}/proof-of-play/{imageId}")
    public ResponseEntity<String> proofOfPlay(@PathVariable Long id, @PathVariable Long imageId) {
        slideshowService.recordProofOfPlay(id, imageId);
        return ResponseEntity.ok("Proof of play recorded for image ID: " + imageId + " in slideshow ID: " + id);
    }

}
