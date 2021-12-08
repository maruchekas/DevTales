package org.skillbox.devtales.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TagDto {

    private int id;
    private String name;
    private double weight;
}
