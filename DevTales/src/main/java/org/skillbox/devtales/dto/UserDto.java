package org.skillbox.devtales.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDto {

    private int id;
    private String name;
    private String photo;
}
