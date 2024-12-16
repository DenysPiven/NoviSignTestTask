package com.novisign.slideshow;

import com.novisign.slideshow.model.Image;
import com.novisign.slideshow.model.Slideshow;
import com.novisign.slideshow.service.ImageService;
import com.novisign.slideshow.service.SlideshowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class SlideshowApplicationTests {

	@Autowired
	private ImageService imageService;

	@Autowired
	private SlideshowService slideshowService;

	@Test
	void contextLoads() {
		assertNotNull(imageService);
		assertNotNull(slideshowService);
	}

	@Test
	void testFullImageWorkflow() {
		Image image = new Image();
		image.setUrl("https://via.placeholder.com/300.jpg");
		image.setDuration(5);

		// Validate URL before adding
		assertTrue(imageService.isValidImageUrl(image.getUrl()));

		// Add Image
		Image savedImage = imageService.addImage(image);

		assertNotNull(savedImage);
		assertEquals("https://via.placeholder.com/300.jpg", savedImage.getUrl());

		// Retrieve Image
		Image retrievedImage = imageService.getImageById(savedImage.getId()).orElse(null);
		assertNotNull(retrievedImage);
		assertEquals(savedImage.getId(), retrievedImage.getId());

		// Validate Image URL after adding
		assertTrue(imageService.isValidImageUrl(savedImage.getUrl()));

		// Delete Image
		imageService.deleteImage(savedImage.getId());
		assertTrue(imageService.getImageById(savedImage.getId()).isEmpty());
	}

	@Test
	void testFullSlideshowWorkflow() {
		Image image1 = new Image();
		image1.setUrl("https://via.placeholder.com/300.jpg");
		image1.setDuration(5);

		Image image2 = new Image();
		image2.setUrl("https://via.placeholder.com/300.png");
		image2.setDuration(10);

		Image savedImage1 = imageService.addImage(image1);
		Image savedImage2 = imageService.addImage(image2);

		List<Image> imageIds = List.of(savedImage1, savedImage2);

		Slideshow slideshow = new Slideshow();
		slideshow.setImages(imageIds);

		Slideshow savedSlideshow = slideshowService.addSlideshow(slideshow);

		assertNotNull(savedSlideshow);
		assertEquals(2, savedSlideshow.getImages().size());

		List<Image> retrievedOrder = slideshowService.getSlideshowOrder(savedSlideshow.getId());
		assertEquals(imageIds, retrievedOrder);

		slideshowService.deleteSlideshow(savedSlideshow.getId());
		assertTrue(slideshowService.getSlideshowById(savedSlideshow.getId()).isEmpty());

		imageService.deleteImage(savedImage1.getId());
		imageService.deleteImage(savedImage2.getId());

		assertTrue(imageService.getImageById(savedImage1.getId()).isEmpty());
		assertTrue(imageService.getImageById(savedImage2.getId()).isEmpty());
	}
}
