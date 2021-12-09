package org.skillbox.devtales.model;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Data
@Entity
@Table(name = "global_settings")
public class GlobalSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String code;

    @NonNull
    private String name;

    @NonNull
    private String value;

}
