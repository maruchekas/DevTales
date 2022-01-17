package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.response.CalendarResponse;
import org.skillbox.devtales.api.response.InitResponse;
import org.skillbox.devtales.api.response.SettingsResponse;
import org.skillbox.devtales.api.response.TagResponse;
import org.skillbox.devtales.service.PostService;
import org.skillbox.devtales.service.SettingsService;
import org.skillbox.devtales.service.impl.CalendarServiceImpl;
import org.skillbox.devtales.service.impl.TagServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final TagServiceImpl tagServiceImpl;
    private final CalendarServiceImpl calendarService;
    private final PostService postService;

    @GetMapping("/init")
    public InitResponse init() {
        return initResponse;
    }

    @GetMapping("/settings")
    public ResponseEntity<SettingsResponse> settings() {
        return new ResponseEntity<>(settingsService.getGlobalSettings(), HttpStatus.OK);
    }

    @GetMapping("/tag")
    public ResponseEntity<TagResponse> tag() {
        return new ResponseEntity<>(tagServiceImpl.getAllTags(), HttpStatus.OK);
    }

    @GetMapping("/calendar")
    public ResponseEntity<CalendarResponse> postCalendar(int year) {

        return new ResponseEntity<>(calendarService.getPostsForCalendar(year), HttpStatus.OK);
    }

}
