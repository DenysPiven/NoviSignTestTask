package com.novisign.slideshow.service;

import com.novisign.slideshow.model.Image;
import com.novisign.slideshow.model.Slideshow;
import com.novisign.slideshow.repository.SlideshowRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SlideshowServiceTest {

    @Mock
    private SlideshowRepository slideshowRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;


    @InjectMocks
    private SlideshowService slideshowService;

    public SlideshowServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSlideshow() {
        Image image1 = new Image();
        image1.setUrl("http://example.com/image1.jpg");
        image1.setDuration(5);

        Image image2 = new Image();
        image2.setUrl("http://example.com/image2.jpg");
        image2.setDuration(3);

        Slideshow slideshow = new Slideshow();
        slideshow.setImages(List.of(image1, image2));

        when(slideshowRepository.save(any(Slideshow.class))).thenReturn(slideshow);
        doNothing().when(eventPublisher).publishEvent(any());

        Slideshow result = slideshowService.addSlideshow(slideshow);

        assertNotNull(result);
        assertEquals(2, result.getImages().size());
        assertEquals("http://example.com/image1.jpg", result.getImages().getFirst().getUrl());
        verify(slideshowRepository, times(1)).save(any(Slideshow.class));
    }

    @Test
    void testDeleteSlideshow() {
        Long slideshowId = 1L;

        doNothing().when(slideshowRepository).deleteById(slideshowId);
        doNothing().when(eventPublisher).publishEvent(any());

        slideshowService.deleteSlideshow(slideshowId);

        verify(slideshowRepository, times(1)).deleteById(slideshowId);
    }

    @Test
    void testGetSlideshowById() {
        Long slideshowId = 1L;

        Image image = new Image();
        image.setUrl("http://example.com/image1.jpg");
        image.setDuration(5);

        Slideshow slideshow = new Slideshow();
        slideshow.setId(slideshowId);
        slideshow.setImages(List.of(image));

        when(slideshowRepository.findById(slideshowId)).thenReturn(Optional.of(slideshow));

        Optional<Slideshow> result = slideshowService.getSlideshowById(slideshowId);

        assertTrue(result.isPresent());
        assertEquals(slideshowId, result.get().getId());
        assertEquals(1, result.get().getImages().size());
        verify(slideshowRepository, times(1)).findById(slideshowId);
    }

    @Test
    void testGetSlideshowOrder() {
        Long slideshowId = 1L;

        Image image1 = new Image();
        image1.setUrl("http://example.com/image1.jpg");
        image1.setDuration(5);

        Image image2 = new Image();
        image2.setUrl("http://example.com/image2.jpg");
        image2.setDuration(3);

        Slideshow slideshow = new Slideshow();
        slideshow.setImages(List.of(image1, image2));

        when(slideshowRepository.findById(slideshowId)).thenReturn(Optional.of(slideshow));

        List<Image> result = slideshowService.getSlideshowOrder(slideshowId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("http://example.com/image1.jpg", result.getFirst().getUrl());
        verify(slideshowRepository, times(1)).findById(slideshowId);
    }
}
