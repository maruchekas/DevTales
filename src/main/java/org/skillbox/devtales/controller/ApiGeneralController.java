package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.request.EditSettingsRequest;
import org.skillbox.devtales.api.response.CalendarResponse;
import org.skillbox.devtales.api.response.InitResponse;
import org.skillbox.devtales.api.response.SettingsResponse;
import org.skillbox.devtales.api.response.TagResponse;
import org.skillbox.devtales.exception.UserAccessDeniedException;
import org.skillbox.devtales.service.CalendarService;
import org.skillbox.devtales.service.SettingsService;
import org.skillbox.devtales.service.TagService;
import org.skillbox.devtales.service.UploadImageService;
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
    private final SettingsService settingsService;
    private final TagService tagService;
    private final CalendarService calendarService;
    private final UploadImageService uploadImageService;

    @GetMapping("/init")
    public InitResponse init() {
        return initResponse;
    }

    @GetMapping("/settings")
    public ResponseEntity<SettingsResponse> settings() {
        return new ResponseEntity<>(settingsService.getGlobalSettings(), HttpStatus.OK);
    }

    @PutMapping("/settings")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<SettingsResponse> editSettings(
            @RequestBody EditSettingsRequest editSettingsRequest, Principal principal) throws UserAccessDeniedException {
        return new ResponseEntity<>(settingsService.saveGlobalSettings(editSettingsRequest, principal), HttpStatus.OK);
    }

    @GetMapping("/tag")
    public ResponseEntity<TagResponse> tag() {
        return new ResponseEntity<>(tagService.getAllTags(), HttpStatus.OK);
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
