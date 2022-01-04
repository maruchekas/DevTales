package org.skillbox.devtales.service.impl;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.response.AuthResponse;
import org.skillbox.devtales.api.response.AuthUserResponse;
import org.skillbox.devtales.repository.UserRepository;
import org.skillbox.devtales.service.AuthService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthResponse getAuthResponse(String userName) {
        org.skillbox.devtales.model.User authUser =
                userRepository.findByEmail(userName)
                        .orElseThrow(() -> new UsernameNotFoundException("User with email " + userName + " not found"));

        AuthUserResponse authUserResponse = new AuthUserResponse();
        authUserResponse.setId(authUser.getId());
        authUserResponse.setEmail(authUser.getEmail());
        authUserResponse.setName(authUser.getName());
        authUserResponse.setPhoto(authUser.getPhoto());
        authUserResponse.setModeration(authUser.getIsModerator() == 1);
        authUserResponse.setModerationCount(authUser.getPosts().size());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(true);
        authResponse.setAuthUserResponse(authUserResponse);

        return authResponse;
    }

}
