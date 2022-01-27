package org.skillbox.devtales.service;

import org.skillbox.devtales.api.request.EditProfileRequest;
import org.skillbox.devtales.api.request.EditProfileWithPhotoRequest;
import org.skillbox.devtales.api.response.CommonResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;

@Service
public interface ProfileService {

    CommonResponse editProfile(EditProfileWithPhotoRequest profileRequest, Principal principal) throws IOException;

    CommonResponse editProfile(EditProfileRequest profileRequest, Principal principal) throws IOException;

}
