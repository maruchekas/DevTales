package org.skillbox.devtales.api.response;

import lombok.Data;

@Data
public class AuthUserResponse {

    private int id;
    private String name;
    private String photo;
    private String email;
    private boolean moderation;
    private int moderationCount;
    private boolean settings;
}
