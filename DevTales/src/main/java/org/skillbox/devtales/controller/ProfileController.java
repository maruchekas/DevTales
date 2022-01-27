package org.skillbox.devtales.controller;

import lombok.RequiredArgsConstructor;
import org.skillbox.devtales.api.request.EditProfileRequest;
import org.skillbox.devtales.api.request.EditProfileWithPhotoRequest;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/profile/my")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CommonResponse> editProfile(@ModelAttribute EditProfileWithPhotoRequest editProfileRequest,
                                                      Principal principal) throws IOException {

        return new ResponseEntity<>(profileService.editProfile(editProfileRequest, principal), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CommonResponse> editProfileData(
            @RequestBody EditProfileRequest editProfileRequest, Principal principal) throws IOException {
        return new ResponseEntity<>(profileService.editProfile(editProfileRequest, principal), HttpStatus.OK);
    }
}
