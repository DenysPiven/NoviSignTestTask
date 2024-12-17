package com.novisign.slideshow.service;

import com.novisign.slideshow.exception.ResourceNotFoundException;
import com.novisign.slideshow.model.Image;
import com.novisign.slideshow.model.ProofOfPlay;
import com.novisign.slideshow.model.Slideshow;
import com.novisign.slideshow.repository.ProofOfPlayRepository;
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
    private ProofOfPlayRepository proofOfPlayRepository;
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
        assertEquals("http://example.com/image1.jpg", result.getImages().get(0).getUrl());
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
        assertEquals("http://example.com/image1.jpg", result.get(0).getUrl());
        verify(slideshowRepository, times(1)).findById(slideshowId);
    }

    @Test
    void testRecordProofOfPlay_Success() {
        // Arrange
        Long slideshowId = 1L;
        Long imageId = 2L;

        Image image = new Image();
        image.setId(imageId);
        image.setUrl("http://example.com/image.jpg");

        Slideshow slideshow = new Slideshow();
        slideshow.setId(slideshowId);
        slideshow.setImages(List.of(image));

        when(slideshowRepository.findById(slideshowId)).thenReturn(Optional.of(slideshow));
        when(proofOfPlayRepository.save(any(ProofOfPlay.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        assertDoesNotThrow(() -> slideshowService.recordProofOfPlay(slideshowId, imageId));

        // Assert
        verify(slideshowRepository, times(1)).findById(slideshowId);
        verify(proofOfPlayRepository, times(1)).save(any(ProofOfPlay.class));
    }

    @Test
    void testRecordProofOfPlay_SlideshowNotFound() {
        // Arrange
        Long slideshowId = 1L;
        Long imageId = 2L;

        when(slideshowRepository.findById(slideshowId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> slideshowService.recordProofOfPlay(slideshowId, imageId));

        assertEquals("Slideshow not found with ID: " + slideshowId, exception.getMessage());
        verify(slideshowRepository, times(1)).findById(slideshowId);
        verify(proofOfPlayRepository, never()).save(any());
    }

    @Test
    void testRecordProofOfPlay_ImageNotFound() {
        // Arrange
        Long slideshowId = 1L;
        Long imageId = 2L;

        Image image = new Image();
        image.setId(3L); // Інший ID

        Slideshow slideshow = new Slideshow();
        slideshow.setId(slideshowId);
        slideshow.setImages(List.of(image));

        when(slideshowRepository.findById(slideshowId)).thenReturn(Optional.of(slideshow));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> slideshowService.recordProofOfPlay(slideshowId, imageId));

        assertEquals("Image not found with ID: " + imageId + " in slideshow ID: " + slideshowId, exception.getMessage());
        verify(slideshowRepository, times(1)).findById(slideshowId);
        verify(proofOfPlayRepository, never()).save(any());
    }

}
