package org.skillbox.devtales.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.skillbox.devtales.api.request.AuthRequest;
import org.skillbox.devtales.api.request.ChangePasswordRequest;
import org.skillbox.devtales.api.request.RegisterRequest;
import org.skillbox.devtales.api.response.AuthResponse;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.config.AppConfig;
import org.skillbox.devtales.dto.UserDto;
import org.skillbox.devtales.exception.DuplicateUserEmailException;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.repository.CaptchaRepository;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.skillbox.devtales.config.Constants.*;

@Component
@RequiredArgsConstructor
public class AuthUserService {

    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;
    private final PostRepository postRepository;
    private final UploadImageService uploadImageService;
    private final MailService mailService;

    public CommonResponse register(RegisterRequest registerRequest) throws DuplicateUserEmailException {

        Map<String, String> errors = validateUserRegisterRequest(registerRequest);

        if (!errors.isEmpty()) {
            return new CommonResponse().setResult(false).setErrors(errors);
        }

        User user = createNewUser(registerRequest);
        userRepository.save(user);

        return new CommonResponse().setResult(true);
    }

    public AuthResponse login(AuthRequest authRequest, AuthenticationManager authenticationManager) {

        if (userRepository.findByEmail(authRequest.getEmail()).isEmpty() || authRequest.getPassword().isBlank()) {
            return new AuthResponse();
        }

        PasswordEncoder passwordEncoder = AppConfig.getPasswordEncoder();
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

    public CommonResponse restorePassword(String email) {
        User user = getUserByEmail(email);
        if (StringUtils.isEmpty(email) || user == null) {
            return new CommonResponse().setResult(false);
        }
        String code = UUID.randomUUID().toString();
        mailService.sendMail(email,
                String.format(RECOVERY_PASSWORD_MESSAGE_TITLE, email),
                MAIL_MASSAGE_BASE_URL + code);
        User userFromDB = getUserByEmail(email)
                .setCode(code);
        userRepository.save(userFromDB);
        return new CommonResponse().setResult(true);
    }

    public CommonResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        Map<String, String> errors = validateChangePasswordRequest(changePasswordRequest);

        if (!errors.isEmpty()) {
            return new CommonResponse().setResult(false).setErrors(errors);
        }

        User user = getUserByCode(changePasswordRequest.getCode())
                .setPassword(getEncodePassword(changePasswordRequest.getPassword()))
                .setCode(String.valueOf(changePasswordRequest.getCaptchaSecret()));
        userRepository.save(user);

        return new CommonResponse().setResult(true);
    }

    private boolean isAuthorized(String session) {
        return AppConfig.getSessions().containsKey(session);
    }

    private Map<String, String> validateUserRegisterRequest(RegisterRequest request) {
        final String email = request.getEmail();
        final String name = request.getName().trim();
        final String password = request.getPassword().trim();
        final String captcha = request.getCaptcha();
        final String captchaSecretCode = request.getCaptchaSecret();

        Map<String, String> errors = new HashMap<>();

        if (userRepository.findByEmail(email).isPresent()) {
            errors.put(EMAIL_ERR, EMAIL_ANSWER);
        }

        if (name.length() < 2) {
            errors.put(NAME_ERR, NAME_ANSWER);
        }

        if (!Pattern.matches(NAME_FORMAT_PATTERN, name) || name.length() < 2) {
            errors.put(NAME_ERR, NAME_ANSWER);
        }

        if (password.length() < 6) {
            errors.put(PASSWORD_ERR, PASSWORD_ANSWER);
        }

        if (!captcha.equals(captchaRepository.findBySecretCode(captchaSecretCode).getCode())) {
            errors.put(CAPTCHA_ERR, CAPTCHA_ANSWER);
        }

        return errors;
    }

    private Map<String, String> validateChangePasswordRequest(ChangePasswordRequest changePasswordRequest) {
        String code = changePasswordRequest.getCode();
        String password = changePasswordRequest.getPassword().trim();
        String captcha = changePasswordRequest.getCaptcha();
        String captchaSecret = changePasswordRequest.getCaptchaSecret();

        Map<String, String> errors = new HashMap<>();

        if (getUserByCode(code) == null) {
            errors.put(CODE_ERR, CODE_ANSWER);
        }
        if (StringUtils.isBlank(password) || password.length() < 6) {
            errors.put(PASSWORD_ERR, PASSWORD_ANSWER);
        }
        if (!captcha.equals(captchaRepository.findBySecretCode(captchaSecret).getCode())) {
            errors.put(CAPTCHA_ERR, CAPTCHA_ANSWER);
        }

        return errors;
    }

    private User createNewUser(RegisterRequest registerRequest) {
        User user = new User()
                .setName(registerRequest.getName())
                .setPassword(getEncodePassword(registerRequest.getPassword()))
                .setEmail(registerRequest.getEmail())
                .setRegTime(LocalDateTime.now())
                .setIsModerator(0)
                .setCode(String.valueOf(registerRequest.getCaptchaSecret()));
        try {
            user.setPhoto(uploadImageService.createAndSaveDefaultAvatarForUser(registerRequest.getEmail()));
        } catch (IOException e) {
            user.setPhoto(null);
            e.printStackTrace();
        }

        return user;
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

    public User getUserByEmail(String userName) {
        return userRepository.findByEmail(userName)
                .orElse(null);
    }

    private User getUserByCode(String code) {
        return userRepository.findByCode(code)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_OR_BLOCKED));
    }

    private String getEncodePassword(String password) {
        return AppConfig.getPasswordEncoder().encode(password);
    }

    private int getNumPostsForModeration(User authUser) {
        return authUser.getIsModerator() == 1 ? postRepository.findCountPostsForModeration() : 0;
    }

}
