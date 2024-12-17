package com.novisign.slideshow.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ImageEvent extends ApplicationEvent {
    private final String action;
    private final String message;

    public ImageEvent(Object source, String action, String message) {
        super(source);
        this.action = action;
        this.message = message;
    }

}
