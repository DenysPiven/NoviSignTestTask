package com.novisign.slideshow.repository;

import com.novisign.slideshow.model.Slideshow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlideshowRepository extends JpaRepository<Slideshow, Long> {
}
