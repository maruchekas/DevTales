package org.skillbox.devtales.api.response;

import lombok.Data;

import java.util.Map;

@Data
public class CalendarResponse {

    private Integer[] years;
    private Map<String, Integer> posts;
}
