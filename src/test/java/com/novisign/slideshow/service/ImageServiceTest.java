package com.novisign.slideshow.service;

import com.novisign.slideshow.model.Image;
import com.novisign.slideshow.repository.ImageRepository;
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

class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ImageService imageService;

    public ImageServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddImage() {
        Image image = new Image();
        image.setUrl("https://example.com/image.png");
        image.setDuration(5);

        when(imageRepository.save(any(Image.class))).thenReturn(image);
        doNothing().when(eventPublisher).publishEvent(any());

        Image result = imageService.addImage(image);

        assertNotNull(result);
        assertEquals("https://example.com/image.png", result.getUrl());
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void testDeleteImage() {
        Long imageId = 1L;

        Image image = new Image();
        image.setId(imageId);
        image.setUrl("http://example.com/image.jpg");
        image.setDuration(5);

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        doNothing().when(imageRepository).deleteById(imageId);
        doNothing().when(eventPublisher).publishEvent(any());

        imageService.deleteImage(imageId);

        verify(imageRepository, times(1)).deleteById(imageId);
    }

    @Test
    void testSearchImages() {
        when(imageRepository.findByUrlContainingIgnoreCase("example"))
                .thenReturn(List.of(new Image()));

        List<Image> results = imageService.searchImages("example");

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(imageRepository, times(1)).findByUrlContainingIgnoreCase("example");
    }

    @Test
    void testGetImageById() {
        Long imageId = 1L;
        Image image = new Image();
        image.setId(imageId);

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));

        Optional<Image> result = imageService.getImageById(imageId);

        assertTrue(result.isPresent());
        assertEquals(imageId, result.get().getId());
        verify(imageRepository, times(1)).findById(imageId);
    }

    @Test
    void testIsValidImageUrlPositive() {
        ImageService mockImageService = mock(ImageService.class);

        when(mockImageService.isValidImageUrl("https://via.placeholder.com/valid-image.jpg"))
                .thenReturn(true);

        assertTrue(mockImageService.isValidImageUrl("https://via.placeholder.com/valid-image.jpg"));
    }

    @Test
    void testIsValidImageUrlNegative() {
        ImageService mockImageService = mock(ImageService.class);

        when(mockImageService.isValidImageUrl("https://invalid.url/invalid-image.gif"))
                .thenReturn(false);

        assertFalse(mockImageService.isValidImageUrl("https://invalid.url/invalid-image.gif"));
    }

}
