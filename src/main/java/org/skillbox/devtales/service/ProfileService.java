package org.skillbox.devtales.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.skillbox.devtales.api.request.EditProfileRequest;
import org.skillbox.devtales.api.request.EditProfileWithPhotoRequest;
import org.skillbox.devtales.api.request.EditRequest;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.skillbox.devtales.config.Constants.*;

@Component
@RequiredArgsConstructor
@Setter
@Getter
public class ProfileService {

    private final AuthUserService userService;
    private final UploadImageService imageService;
    private final UserRepository userRepository;

    public CommonResponse editProfile(EditProfileWithPhotoRequest editRequest, Principal principal) throws IOException {
        EditProfileData editProfileData = validateAndEditRequest(editRequest, principal);
        Map<String, String> errors = editProfileData.getErrors();

        if (errors.isEmpty()) {
            User user = editProfileData.getUser();
            userRepository.save(user);
            return new CommonResponse().setResult(true);
        } else return new CommonResponse().setResult(false).setErrors(errors);
    }

    public CommonResponse editProfile(EditProfileRequest editRequest, Principal principal) throws IOException {
        EditProfileData editProfileData = validateAndEditRequest(editRequest, principal);
        Map<String, String> errors = editProfileData.getErrors();

        if (errors.isEmpty()) {
            User user = editProfileData.getUser();
            userRepository.save(user);
            return new CommonResponse().setResult(true);
        } else return new CommonResponse().setResult(false).setErrors(errors);
    }

    private EditProfileData validateAndEditRequest(EditRequest editRequest, Principal principal) throws IOException {
        PasswordEncoder encoder = new BCryptPasswordEncoder(12);
        User user = userService.getUserByEmail(principal.getName());
        Map<String, String> errors = new HashMap<>();

        if (!StringUtils.isBlank(editRequest.getEmail()) && !editRequest.getEmail().equals(user.getEmail())) {
            if (!Pattern.matches(EMAIL_FORMAT_PATTERN, editRequest.getEmail())) {
                errors.put(EMAIL_ERR, EMAIL_FORMAT_ANSWER);
            }
            if (userRepository.findByEmail(editRequest.getEmail()).isPresent()) {
                errors.put(EMAIL_ERR, EMAIL_ANSWER);
            } else user.setEmail(editRequest.getEmail());
        }
        if (!editRequest.getName().isBlank()) {
            if (!Pattern.matches(NAME_FORMAT_PATTERN, editRequest.getName())
                    || editRequest.getName().length() < 2) {
                errors.put(NAME_ERR, NAME_ANSWER);
            } else user.setName(editRequest.getName());
        }
        if (!StringUtils.isEmpty(editRequest.getPassword())) {
            if (editRequest.getPassword().trim().length() < 6) {
                errors.put(PASSWORD_ERR, PASSWORD_ANSWER);
            } else user.setPassword(encoder.encode(editRequest.getPassword()));
        }
        if (editRequest instanceof EditProfileData) {
            if (StringUtils.isBlank((String) editRequest.getPhoto()) && editRequest.getRemovePhoto() == 1) {
                user.setPhoto(null);
            }
        } else if (editRequest.getPhoto() != null) {
            if(validatePhoto((MultipartFile) editRequest.getPhoto()).isEmpty())
            user.setPhoto(imageService.uploadImgToCloudAndGetUrl((MultipartFile) editRequest.getPhoto(), principal));
        }

        return new EditProfileData().setUser(user).setErrors(errors);
    }

    private Map<String, String> validatePhoto(MultipartFile photo){
        Map<String, String> errors = new HashMap<>();
        if (!Pattern.matches("image/(jpg|png|jpeg)", photo.getContentType())){
            errors.put(CONTENT_TYPE_ERR, CONTENT_TYPE_ANSWER);
        }
        if (photo.getSize() > IMG_SIZE_LIMIT) {
            errors.put(IMAGE_ERR, IMAGE_OVERSIZE_ANSWER);
        }

        return errors;
    }


}

    @Setter
    @Getter
    @RequiredArgsConstructor
    @Accessors(chain = true)
    class EditProfileData {
        private User user;
        private Map<String, String> errors;
    }
