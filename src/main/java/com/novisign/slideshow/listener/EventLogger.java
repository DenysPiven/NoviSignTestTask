package com.novisign.slideshow.listener;

import com.novisign.slideshow.event.ImageEvent;
import com.novisign.slideshow.event.SlideshowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventLogger {

    private static final Logger logger = LoggerFactory.getLogger(EventLogger.class);

    @EventListener
    public void handleImageEvent(ImageEvent event) {
        logger.info("Image Event - Action: {}, Message: {}", event.getAction(), event.getMessage());
    }

    @EventListener
    public void handleSlideshowEvent(SlideshowEvent event) {
        logger.info("Slideshow Event - Action: {}, Message: {}", event.getAction(), event.getMessage());
    }

}
