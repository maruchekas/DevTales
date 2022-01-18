package org.skillbox.devtales.service.impl;

import lombok.RequiredArgsConstructor;
import org.skillbox.devtales.api.request.AuthRequest;
import org.skillbox.devtales.api.request.RegisterRequest;
import org.skillbox.devtales.api.response.AuthResponse;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.api.response.UserDto;
import org.skillbox.devtales.config.AppConfig;
import org.skillbox.devtales.exception.DuplicateUserEmailException;
import org.skillbox.devtales.exception.FailedToUploadImageException;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.repository.CaptchaRepository;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.repository.UserRepository;
import org.skillbox.devtales.service.AuthUserService;
import org.skillbox.devtales.service.UploadImageService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;
    private final PostRepository postRepository;
    private final UploadImageService uploadImageService;

    public CommonResponse register(RegisterRequest registerRequest) throws DuplicateUserEmailException{
        PasswordEncoder encoder = new BCryptPasswordEncoder(12);
        CommonResponse response = new CommonResponse();
        Map<String, String> errors = validateUserRegisterRequest(registerRequest);

        if (errors.size() > 0) {
            response.setResult(false);
            response.setErrors(errors);

            return response;
        }

        User user = new User()
                .setName(registerRequest.getName())
                .setPassword(encoder.encode(registerRequest.getPassword()))
                .setEmail(registerRequest.getEmail())
                .setRegTime(LocalDateTime.now())
                .setIsModerator(0)
                .setCode(String.valueOf(registerRequest.getCaptchaSecret()));
                try{
                user.setPhoto(uploadImageService.uploadImage(registerRequest.getEmail()));
                } catch (IOException e){
                    throw new FailedToUploadImageException("Не удалось установить аватар по умолчанию для пользователя "
                            + registerRequest.getEmail());
                }
        userRepository.save(user);

        response.setResult(true);

        return response;
    }

    public AuthResponse login(AuthRequest authRequest, AuthenticationManager authenticationManager) {

        if (userRepository.findByEmail(authRequest.getEmail()).isEmpty() || authRequest.getPassword().isBlank()) {
            return new AuthResponse();
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        if (!passwordEncoder.matches(authRequest.getPassword(), getUserByEmail(authRequest.getEmail()).getPassword())) {
            return new AuthResponse();
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);
        final String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        int userId = getUserByEmail(authRequest.getEmail()).getId();
        AppConfig.addSessionId(session, userId);
        org.springframework.security.core.userdetails.User user
                = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        return getAuthResponse(user.getUsername());
    }

    public AuthResponse check(Principal principal) {
        if (principal == null) {
            return new AuthResponse();
        }
        User user = getUserByEmail(principal.getName());
        if (!AppConfig.getSessions().containsValue(user.getId())) {
            return new AuthResponse();
        }

        return getAuthResponse(user.getEmail());
    }

    public AuthResponse logout() {
        AuthResponse authResponse = new AuthResponse();
        final String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        if (isAuthorized(session)) {
            AppConfig.removeSession(session);
            SecurityContextHolder.clearContext();
        }

        authResponse.setResult(true);
        return authResponse;
    }

    private boolean isAuthorized(String session) {
        return AppConfig.getSessions().containsKey(session);
    }

    public AuthResponse getAuthResponse(String email) {
        User authUser = getUserByEmail(email);

        AuthResponse authResponse = new AuthResponse();

        UserDto userDto = new UserDto();
        userDto.setId(authUser.getId());
        userDto.setEmail(authUser.getEmail());
        userDto.setName(authUser.getName());
        userDto.setPhoto(authUser.getPhoto());
        userDto.setModeration(authUser.getIsModerator() == 1);
        userDto.setModerationCount(getNumPostsForModeration(authUser));

        authResponse.setResult(true);
        authResponse.setUserDto(userDto);

        return authResponse;
    }

    private User getUserByEmail(String userName) {
        return userRepository.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + userName + " not found"));
    }

    private int getNumPostsForModeration(User authUser) {
        return authUser.getIsModerator() == 1 ? postRepository.findCountPostsForModeration() : 0;
    }

    private Map<String, String> validateUserRegisterRequest(RegisterRequest request) {
        final String email = request.getEmail();
        final String name = request.getName().trim();
        final String password = request.getPassword().trim();
        final String captcha = request.getCaptcha();
        final String captchaSecretCode = request.getCaptchaSecret();

        Map<String, String> errors = new HashMap<>();

        if (userRepository.findByEmail(email).isPresent()) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }

        if (name.trim().length() < 2) {
            errors.put("name", "Имя указано неверно");
        }

        if (password.length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }

        if (!captcha.equals(captchaRepository.findBySecretCode(captchaSecretCode).getCode())) {
            errors.put("captcha", "Код с картинки введён неверно");
        }

        return errors;
    }

}
