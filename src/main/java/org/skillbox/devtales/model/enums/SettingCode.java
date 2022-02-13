package org.skillbox.devtales.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SettingCode {
    MULTIUSER_MODE("Многопользовательский режим"),
    POST_PREMODERATION("Премодерация постов"),
    STATISTICS_IS_PUBLIC("Показывать всем статистику блога");

    private final String name;
}
