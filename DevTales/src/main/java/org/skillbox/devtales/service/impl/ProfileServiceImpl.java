package org.skillbox.devtales.service.impl;

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
import org.skillbox.devtales.service.AuthUserService;
import org.skillbox.devtales.service.ProfileService;
import org.skillbox.devtales.service.UploadImageService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Setter
@Getter
public class ProfileServiceImpl implements ProfileService {

    private final AuthUserService userService;
    private final UploadImageService imageService;
    private final UserRepository userRepository;

    private final String namePattern = "^[a-zа-яA-ZА-Я0-9 _.-]*$";
    private final String mailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    public CommonResponse editProfile(EditProfileWithPhotoRequest editRequest, Principal principal) throws IOException {
        EditProfileData editProfileData = validateAndEditRequest(editRequest, principal);
        Map<String, String> errors = editProfileData.getErrors();

        if (errors.isEmpty()){
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
            if (!Pattern.matches(mailPattern, editRequest.getEmail())) {
                errors.put("email", "Неверный формат email");
            }
            if (user.getEmail().equals(editRequest.getEmail())) {
                errors.put("email", "Этот e-mail уже зарегистрирован");
            } else user.setEmail(editRequest.getEmail());
        }
        if (!editRequest.getName().isBlank()) {
            if (!Pattern.matches(namePattern, editRequest.getName())
                    || editRequest.getName().length() < 2) {
                errors.put("name", "Имя указано неверно");
            } else user.setName(editRequest.getName());
        }
        if (!StringUtils.isBlank(editRequest.getPassword())) {
            if (editRequest.getPassword().length() < 6) {
                errors.put("password", "Пароль короче 6-ти символов");
            } else user.setPassword(encoder.encode(editRequest.getPassword()));
        }
        if (editRequest instanceof EditProfileData) {
            if (StringUtils.isBlank((CharSequence) editRequest.getPhoto()) && editRequest.getRemovePhoto() == 1) {
                user.setPhoto(null);
            } else if (editRequest.getPhoto() != null) {
                user.setPhoto(imageService.uploadImgToCloudAndGetUrl((MultipartFile) editRequest.getPhoto(), principal));
            }
        }

        return new EditProfileData().setUser(user).setErrors(errors);
    }

    @Setter
    @Getter
    @RequiredArgsConstructor
    @Accessors(chain = true)
    static class EditProfileData {
        private User user;
        private Map<String, String> errors;
    }

}
