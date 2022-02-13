package org.skillbox.devtales.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.skillbox.devtales.api.request.EditSettingsRequest;
import org.skillbox.devtales.api.response.SettingsResponse;
import org.skillbox.devtales.exception.UserAccessDeniedException;
import org.skillbox.devtales.model.GlobalSetting;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.model.enums.SettingCode;
import org.skillbox.devtales.repository.GlobalSettingRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;

import static org.skillbox.devtales.config.Constants.WRONG_PARAMETER;

@Component
@RequiredArgsConstructor
@Setter
@Getter
public class SettingsService {

    private final GlobalSettingRepository globalSettingRepository;
    private final AuthUserService userService;

    public SettingsResponse getGlobalSettings() {

        return new SettingsResponse()
                .setMultiuserMode(getSettingValue(getSittingByCode(SettingCode.MULTIUSER_MODE.name())))
                .setPostPremoderation(getSettingValue(getSittingByCode(SettingCode.POST_PREMODERATION.name())))
                .setStatisticsIsPublic(getSettingValue(getSittingByCode(SettingCode.STATISTICS_IS_PUBLIC.name())));
    }

    public SettingsResponse saveGlobalSettings(EditSettingsRequest editSettingsRequest, Principal principal) throws UserAccessDeniedException {
        User user = userService.getUserByEmail(principal.getName());
        if (user.getIsModerator() != 1) {
            throw new UserAccessDeniedException();
        }

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
