package org.skillbox.devtales.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarResponse {

    private Integer[] years;
    private Map<String, Integer> posts;
}
