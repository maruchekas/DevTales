package org.skillbox.devtales.config;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {

    private final static Map<String, Integer> sessions = new HashMap<>();

    public static void addSessionId(String session, Integer userId){
        sessions.put(session, userId);
        System.out.println(sessions);
    }

    public static void removeSession(String session){
        sessions.remove(session);
        System.out.println(sessions);
    }

    public static Map<String, Integer> getSessions(){
        return new HashMap<>(sessions);
    }
}
