package com.novisign.slideshow.controller;

import com.novisign.slideshow.exception.BadRequestException;
import com.novisign.slideshow.exception.ResourceNotFoundException;
import com.novisign.slideshow.model.Image;
import com.novisign.slideshow.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Operation(summary = "Add a new image", description = "Adds an image with a URL and duration.")
    @PostMapping("/add")
    public ResponseEntity<Image> addImage(@RequestBody Image image) {
        if (image.getUrl() == null || image.getUrl().isBlank()) {
            throw new BadRequestException("Image URL cannot be empty");
        }
        if (!imageService.isValidImageUrl(image.getUrl())) {
            throw new BadRequestException("Invalid image URL or unsupported format");
        }
        return ResponseEntity.ok(imageService.addImage(image));
    }

    @Operation(summary = "Delete an image", description = "Deletes an image by its ID.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        imageService.getImageById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with ID: " + id));
        imageService.deleteImage(id);
        return ResponseEntity.ok("Image deleted successfully");
    }

    @Operation(summary = "Search images", description = "Search images by keyword in their URL.")
    @GetMapping("/search")
    public ResponseEntity<List<Image>> searchImages(@RequestParam String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new BadRequestException("Keyword cannot be empty");
        }

        List<Image> images = imageService.searchImages(keyword);
        if (images.isEmpty()) {
            throw new ResourceNotFoundException("No images found matching the keyword: " + keyword);
        }
        return ResponseEntity.ok(images);
    }

    @Operation(summary = "Get image by ID", description = "Retrieve a single image by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable Long id) {
        Image image = imageService.getImageById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with ID: " + id));
        return ResponseEntity.ok(image);
    }
}
