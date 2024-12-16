package com.novisign.slideshow.listener;

import com.novisign.slideshow.event.ImageEvent;
import com.novisign.slideshow.event.ProofOfPlayEvent;
import com.novisign.slideshow.event.SlideshowEvent;
import com.novisign.slideshow.service.EventLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventLogger {

    private static final Logger logger = LoggerFactory.getLogger(EventLogger.class);
    private final EventLogService eventLogService;

    public EventLogger(EventLogService eventLogService) {
        this.eventLogService = eventLogService;
    }

    @EventListener
    public void handleImageEvent(ImageEvent event) {
        logger.info("Image Event - Action: {}, Message: {}", event.getAction(), event.getMessage());
        eventLogService.logEvent("IMAGE_" + event.getAction().toUpperCase(), event.getMessage());
    }

    @EventListener
    public void handleSlideshowEvent(SlideshowEvent event) {
        logger.info("Slideshow Event - Action: {}, Message: {}", event.getAction(), event.getMessage());
        eventLogService.logEvent("SLIDESHOW_" + event.getAction().toUpperCase(), event.getMessage());
    }

    @EventListener
    public void handleProofOfPlayEvent(ProofOfPlayEvent event) {
        logger.info("Proof-of-Play Event - Slideshow ID: {}, Image URL: {}",
                event.getSlideshowId(), event.getImageUrl());
        eventLogService.logEvent("PROOF_OF_PLAY",
                "Slideshow ID: " + event.getSlideshowId() + ", Image URL: " + event.getImageUrl());
    }

}
