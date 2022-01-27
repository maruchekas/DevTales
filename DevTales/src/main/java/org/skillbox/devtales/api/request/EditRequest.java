package org.skillbox.devtales.api.request;

public interface EditRequest {
    String getEmail();

    String getName();

    CharSequence getPassword();

    Object getPhoto();

    int getRemovePhoto();
}
