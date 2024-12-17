package com.novisign.slideshow.service;

import com.novisign.slideshow.event.SlideshowEvent;
import com.novisign.slideshow.exception.ProofOfPlayException;
import com.novisign.slideshow.model.Image;
import com.novisign.slideshow.model.Slideshow;
import com.novisign.slideshow.repository.ImageRepository;
import com.novisign.slideshow.repository.SlideshowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SlideshowService {

    private final SlideshowRepository slideshowRepository;
    private final ImageRepository imageRepository;
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger logger = LoggerFactory.getLogger(SlideshowService.class);

    @Autowired
    public SlideshowService(SlideshowRepository slideshowRepository, ApplicationEventPublisher eventPublisher, ImageRepository imageRepository) {
        this.slideshowRepository = slideshowRepository;
        this.eventPublisher = eventPublisher;
        this.imageRepository = imageRepository;
    }

    public Slideshow addSlideshow(Slideshow slideshow) {
        logger.debug("Entering addSlideshow method with slideshow: {}", slideshow);
        Slideshow savedSlideshow = slideshowRepository.save(slideshow);
        logger.info("Slideshow added with ID: {}", savedSlideshow.getId());
        eventPublisher.publishEvent(new SlideshowEvent(this, "add", "Slideshow added with ID: " + savedSlideshow.getId()));
        if (slideshow.getImages() != null) {
            for (Image image : slideshow.getImages()) {
                imageRepository.save(image);
                String message = String.format("Image with URL %s added to Slideshow ID: %d", image.getUrl(), savedSlideshow.getId());
                eventPublisher.publishEvent(new SlideshowEvent(this, "add-image", message));
                logger.info("Event published for image: {}", image.getUrl());
            }
        }
        return slideshowRepository.save(slideshow);
    }


    public void deleteSlideshow(Long id) {
        logger.debug("Entering deleteSlideshow method with ID: {}", id);
        slideshowRepository.deleteById(id);
        logger.info("Slideshow deleted with ID: {}", id);
        eventPublisher.publishEvent(new SlideshowEvent(this, "delete", "Slideshow deleted with ID: " + id));
    }

    public Optional<Slideshow> getSlideshowById(Long id) {
        logger.debug("Entering getSlideshowById method with ID: {}", id);
        Optional<Slideshow> slideshow = slideshowRepository.findById(id);

        if (slideshow.isPresent()) {
            logger.info("Slideshow found with ID: {}", id);
        } else {
            logger.warn("No slideshow found with ID: {}", id);
        }

        return slideshow;
    }

    public List<Image> getSlideshowOrder(Long id) {
        logger.debug("Fetching slideshow order for ID: {}", id);
        return slideshowRepository.findById(id)
                .map(slideshow -> {
                    logger.info("Slideshow order retrieved for ID: {}", id);
                    return slideshow.getImages();
                })
                .orElseGet(() -> {
                    logger.warn("No slideshow found with ID: {}", id);
                    return List.of();
                });
    }

    public void recordProofOfPlay(Long slideshowId, Long imageId) {
        try {
            Optional<Slideshow> slideshow = slideshowRepository.findById(slideshowId);
            if (slideshow.isEmpty()) {
                throw new ProofOfPlayException("Slideshow not found with ID: " + slideshowId);
            }

            boolean containsImage = slideshow.get().getImages().stream()
                    .anyMatch(image -> image.getId().equals(imageId));
            if (!containsImage) {
                throw new ProofOfPlayException("Image ID " + imageId + " is not part of Slideshow ID: " + slideshowId);
            }

            logger.info("Proof of play recorded for image ID: {} in slideshow ID: {}", imageId, slideshowId);
        } catch (Exception e) {
            throw new ProofOfPlayException("An error occurred while recording proof-of-play: " + e.getMessage());
        }
    }
}
