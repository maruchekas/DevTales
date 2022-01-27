package org.skillbox.devtales.api.request;

public interface EditRequest {
    String getEmail();

    String getName();

    String getPassword();

    Object getPhoto();

    int getRemovePhoto();
}
