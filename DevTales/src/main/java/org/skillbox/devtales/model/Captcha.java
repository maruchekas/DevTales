package org.skillbox.devtales.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "captcha_codes")
public class Captcha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "DATETIME")
    @NonNull
    private LocalDateTime time;

    @Column(columnDefinition = "TINYTEXT")
    @NonNull
    private String code;

    @Column(columnDefinition = "TINYTEXT")
    @NonNull
    private String secretCode;

}
