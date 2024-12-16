package com.novisign.slideshow.service;

import com.novisign.slideshow.event.SlideshowEvent;
import com.novisign.slideshow.exception.ResourceNotFoundException;
import com.novisign.slideshow.model.Image;
import com.novisign.slideshow.model.ProofOfPlay;
import com.novisign.slideshow.model.Slideshow;
import com.novisign.slideshow.repository.ProofOfPlayRepository;
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
    private final ProofOfPlayRepository proofOfPlayRepository;
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger logger = LoggerFactory.getLogger(SlideshowService.class);

    @Autowired
    public SlideshowService(SlideshowRepository slideshowRepository, ProofOfPlayRepository proofOfPlayRepository, ApplicationEventPublisher eventPublisher) {
        this.slideshowRepository = slideshowRepository;
        this.proofOfPlayRepository = proofOfPlayRepository;
        this.eventPublisher = eventPublisher;
    }

    public Slideshow addSlideshow(Slideshow slideshow) {
        logger.debug("Entering addSlideshow method with slideshow: {}", slideshow);
        Slideshow savedSlideshow = slideshowRepository.save(slideshow);
        logger.info("Slideshow added with ID: {}", savedSlideshow.getId());
        eventPublisher.publishEvent(new SlideshowEvent(this, "add", "Slideshow added with ID: " + savedSlideshow.getId()));
        return savedSlideshow;
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
        Slideshow slideshow = slideshowRepository.findById(slideshowId)
                .orElseThrow(() -> new ResourceNotFoundException("Slideshow not found with ID: " + slideshowId));

        Image image = slideshow.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with ID: " + imageId + " in slideshow ID: " + slideshowId));

        ProofOfPlay proof = new ProofOfPlay();
        proof.setSlideshow(slideshow);
        proof.setImageUrl(image.getUrl());
        proofOfPlayRepository.save(proof);
    }
}
