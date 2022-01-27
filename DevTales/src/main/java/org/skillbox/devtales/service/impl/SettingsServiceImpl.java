package org.skillbox.devtales.service.impl;

import org.skillbox.devtales.api.response.SettingsResponse;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl {

    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiuserMode(true);
        settingsResponse.setStatisticsIsPublic(true);

        return settingsResponse;
    }

}
