package org.skillbox.devtales.controller;

import org.skillbox.devtales.api.request.AuthRequest;
import org.skillbox.devtales.api.response.AuthResponse;
import org.skillbox.devtales.api.response.AuthUserResponse;
import org.skillbox.devtales.repository.UserRepository;
import org.skillbox.devtales.service.impl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthServiceImpl authServiceImpl;

    public ApiAuthController(AuthenticationManager authenticationManager,
                             UserRepository userRepository,
                             AuthServiceImpl authServiceImpl) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.authServiceImpl = authServiceImpl;
    }

    @GetMapping("/check")
    public ResponseEntity<AuthResponse> check(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(new AuthResponse());
        }
        return ResponseEntity.ok(getAuthResponse(principal.getName()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = (User) auth.getPrincipal();

        return ResponseEntity.ok(getAuthResponse(user.getUsername()));
    }

    @GetMapping("/logout")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<AuthResponse> logout() {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(true);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(authResponse);
    }

    private AuthResponse getAuthResponse(String userName) {
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
