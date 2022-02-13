package org.skillbox.devtales.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.skillbox.devtales.config.Constants.ENCODING_STRENGTH;

@Configuration
public class AppConfig {

    private final static Map<String, Integer> sessions = new HashMap<>();

    public static void addSessionId(String session, Integer userId) {
        sessions.put(session, userId);
    }

    public static void removeSession(String session) {
        sessions.remove(session);
    }

    public static Map<String, Integer> getSessions() {
        return new HashMap<>(sessions);
    }

    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(ENCODING_STRENGTH);
    }
}
