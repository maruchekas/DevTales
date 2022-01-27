package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.response.CalendarResponse;
import org.skillbox.devtales.api.response.InitResponse;
import org.skillbox.devtales.api.response.SettingsResponse;
import org.skillbox.devtales.api.response.TagResponse;
import org.skillbox.devtales.service.UploadImageService;
import org.skillbox.devtales.service.impl.SettingsServiceImpl;
import org.skillbox.devtales.service.impl.CalendarServiceImpl;
import org.skillbox.devtales.service.impl.TagServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingsServiceImpl settingsService;
    private final TagServiceImpl tagServiceImpl;
    private final CalendarServiceImpl calendarService;
    private final UploadImageService uploadImageService;

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

    @PostMapping("/image")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> uploadImage(@RequestPart(required = false) MultipartFile image,
                                         Principal principal) throws IOException {

        return uploadImageService.saveImage(image, principal);
    }

}
