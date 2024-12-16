package com.novisign.slideshow.repository;

import com.novisign.slideshow.model.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {
}
