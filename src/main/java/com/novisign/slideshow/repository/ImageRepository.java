package com.novisign.slideshow.repository;

import com.novisign.slideshow.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByUrlContainingIgnoreCase(String keyword);
}
