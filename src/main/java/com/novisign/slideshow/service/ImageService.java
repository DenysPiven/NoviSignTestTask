package com.novisign.slideshow.service;

import com.novisign.slideshow.event.ImageEvent;
import com.novisign.slideshow.exception.ResourceNotFoundException;
import com.novisign.slideshow.model.Image;
import com.novisign.slideshow.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private static final List<String> SUPPORTED_IMAGE_TYPES = List.of("image/jpeg", "image/png", "image/webp");

    @Autowired
    public ImageService(ImageRepository imageRepository, ApplicationEventPublisher eventPublisher) {
        this.imageRepository = imageRepository;
        this.eventPublisher = eventPublisher;
    }

    public Image addImage(Image image) {
        logger.debug("Entering addImage method with image: {}", image);
        Image savedImage = imageRepository.save(image);
        logger.info("Image added with ID: {}", savedImage.getId());
        eventPublisher.publishEvent(new ImageEvent(this, "add", "Image added with ID: " + savedImage.getId()));
        return savedImage;
    }

    public void deleteImage(Long id) {
        logger.debug("Entering deleteImage method with ID: {}", id);
        imageRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Image not found with ID: {}", id);
                    return new ResourceNotFoundException("Image not found.");
                });
        imageRepository.deleteById(id);
        logger.info("Image deleted with ID: {}", id);
        eventPublisher.publishEvent(new ImageEvent(this, "delete", "Image deleted with ID: " + id));
    }

    public List<Image> searchImages(String keyword) {
        logger.debug("Entering searchImages method with keyword: {}", keyword);
        List<Image> images = imageRepository.findByUrlContainingIgnoreCase(keyword);
        logger.info("Found {} images matching keyword '{}'", images.size(), keyword);
        return images;
    }

    public Optional<Image> getImageById(Long id) {
        logger.debug("Entering getImageById method with ID: {}", id);
        Optional<Image> image = imageRepository.findById(id);

        if (image.isPresent()) {
            logger.info("Image found with ID: {}", id);
        } else {
            logger.warn("No image found with ID: {}", id);
        }

        return image;
    }

    public boolean isValidImageUrl(String urlString) {
        logger.debug("Entering getImageById method with URL: {}", urlString);
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();

            String contentType = connection.getContentType();
            return SUPPORTED_IMAGE_TYPES.contains(contentType);
        } catch (Exception e) {
            logger.warn("Not found image with URL: {}", urlString);
            return false;
        }
    }
}
