package com.novisign.slideshow.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProofOfPlayEvent extends ApplicationEvent {
    private final Long slideshowId;
    private final String imageUrl;

    public ProofOfPlayEvent(Object source, Long slideshowId, String imageUrl) {
        super(source);
        this.slideshowId = slideshowId;
        this.imageUrl = imageUrl;
    }

}
