package org.skillbox.devtales.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EditSettingsRequest {

    @JsonProperty("MULTIUSER_MODE")
    private boolean multiuserMode;
    @JsonProperty("POST_PREMODERATION")
    private boolean postPremoderation;
    @JsonProperty("STATISTICS_IS_PUBLIC")
    private boolean statisticsIsPublic;
}
