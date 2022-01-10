package org.skillbox.devtales.service.impl;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.response.CalendarResponse;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.service.CalendarService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final PostRepository postRepository;


    public CalendarResponse getPostsForCalendar(int year) {
        CalendarResponse calendarResponse = new CalendarResponse();
        Integer[] years = postRepository.findYearsOfPosts();
        List<String> dataLines = postRepository.findPostsByYear(year);
        Map<String, Integer> postsByDates = new HashMap<>();

        writeDataToPostsMap(dataLines, postsByDates);
        calendarResponse.setYears(years);
        calendarResponse.setPosts(postsByDates);

        return calendarResponse;
    }

    private void writeDataToPostsMap(List<String> dataLines, Map<String, Integer> postsByDates) {
        for (String line : dataLines
        ) {
            postsByDates.put(line.split(",")[0], Integer.parseInt(line.split(",")[1]));
        }
    }
}
