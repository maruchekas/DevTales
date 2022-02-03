package org.skillbox.devtales.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.skillbox.devtales.api.request.EditSettingsRequest;
import org.skillbox.devtales.api.response.SettingsResponse;
import org.skillbox.devtales.model.GlobalSetting;
import org.skillbox.devtales.model.enums.SettingCode;
import org.skillbox.devtales.repository.GlobalSettingRepository;
import org.skillbox.devtales.service.SettingsService;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

import static org.skillbox.devtales.config.Constants.WRONG_PARAMETER;

@Component
@RequiredArgsConstructor
@Setter
@Getter
public class SettingsServiceImpl implements SettingsService {

    private final GlobalSettingRepository globalSettingRepository;

    public SettingsResponse getGlobalSettings() {

        return new SettingsResponse()
                .setMultiuserMode(getSettingValue(getSittingByCode(SettingCode.MULTIUSER_MODE.name())))
                .setPostPremoderation(getSettingValue(getSittingByCode(SettingCode.POST_PREMODERATION.name())))
                .setStatisticsIsPublic(getSettingValue(getSittingByCode(SettingCode.STATISTICS_IS_PUBLIC.name())));
    }

    public SettingsResponse saveGlobalSettings(EditSettingsRequest editSettingsRequest) {
        GlobalSetting modeSetting = getSittingByCode(SettingCode.MULTIUSER_MODE.name());
        modeSetting
                .setValue(editSettingsRequest.isMultiuserMode() ? "YES" : "NO");
        globalSettingRepository.save(modeSetting);
        GlobalSetting postSetting = getSittingByCode(SettingCode.POST_PREMODERATION.name());
        postSetting
                .setValue(editSettingsRequest.isPostPremoderation() ? "YES" : "NO");
        globalSettingRepository.save(postSetting);
        GlobalSetting statSetting = getSittingByCode(SettingCode.STATISTICS_IS_PUBLIC.name());
        statSetting
                .setValue(editSettingsRequest.isStatisticsIsPublic() ? "YES" : "NO");
        globalSettingRepository.save(statSetting);

        return getGlobalSettings();
    }

    private GlobalSetting getSittingByCode(String code) {
        return globalSettingRepository.findByCode(code).orElseThrow(
                () -> new EntityNotFoundException(WRONG_PARAMETER));
    }

    private boolean getSettingValue(GlobalSetting setting) {
        return setting.getValue().equals("YES");
    }
}
