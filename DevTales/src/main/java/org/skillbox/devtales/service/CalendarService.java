package org.skillbox.devtales.service;

import org.skillbox.devtales.api.response.CalendarResponse;
import org.springframework.stereotype.Service;

@Service
public interface CalendarService {

    CalendarResponse getPostsForCalendar(int year);
}
