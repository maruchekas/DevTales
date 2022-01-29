package org.skillbox.devtales.service;

import org.skillbox.devtales.api.request.EditSettingsRequest;
import org.skillbox.devtales.api.response.SettingsResponse;
import org.springframework.stereotype.Service;

@Service
public interface SettingsService {

    SettingsResponse getGlobalSettings();

    SettingsResponse saveGlobalSettings(EditSettingsRequest editSettingsRequest);
}
