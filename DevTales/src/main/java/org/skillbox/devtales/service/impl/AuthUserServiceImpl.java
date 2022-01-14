package org.skillbox.devtales.service.impl;

import lombok.RequiredArgsConstructor;
import org.skillbox.devtales.api.request.AuthRequest;
import org.skillbox.devtales.api.request.RegisterRequest;
import org.skillbox.devtales.api.response.AuthResponse;
import org.skillbox.devtales.api.response.UserDto;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.exception.DuplicateUserEmailException;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.repository.CaptchaRepository;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.repository.UserRepository;
import org.skillbox.devtales.service.AuthUserService;
import org.skillbox.devtales.util.UserAvatarUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;
    private final PostRepository postRepository;

    public CommonResponse register(RegisterRequest registerRequest) throws DuplicateUserEmailException {
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
                .setPhoto(UserAvatarUtil.createDefaultRoboticAvatar(registerRequest.getName()))
                .setCode(String.valueOf(registerRequest.getCaptchaSecret()));

        userRepository.save(user);

        response.setResult(true);

        return response;
    }

    public AuthResponse login(AuthRequest authRequest, AuthenticationManager authenticationManager) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        return getAuthResponse(user.getUsername());
    }

    public AuthResponse logout() {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(true);
        SecurityContextHolder.clearContext();

        return authResponse;
    }

    public AuthResponse getAuthResponse(String userName) {
        org.skillbox.devtales.model.User authUser =
                userRepository.findByEmail(userName)
                        .orElseThrow(() -> new UsernameNotFoundException("User with email " + userName + " not found"));

        UserDto userDto = new UserDto();
        userDto.setId(authUser.getId());
        userDto.setEmail(authUser.getEmail());
        userDto.setName(authUser.getName());
        userDto.setPhoto(authUser.getPhoto());
        userDto.setModeration(authUser.getIsModerator() == 1);
        userDto.setModerationCount(getNumPostsForModeration(authUser));

        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(true);
        authResponse.setUserDto(userDto);

        return authResponse;
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
