package com.novisign.slideshow.service;

import com.novisign.slideshow.model.EventLog;
import com.novisign.slideshow.repository.EventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventLogService {

    private final EventLogRepository eventLogRepository;

    @Autowired
    public EventLogService(EventLogRepository eventLogRepository) {
        this.eventLogRepository = eventLogRepository;
    }

    public EventLog logEvent(String eventType, String message) {
        EventLog eventLog = new EventLog();
        eventLog.setEventType(eventType);
        eventLog.setMessage(message);
        return eventLogRepository.save(eventLog);
    }
}
