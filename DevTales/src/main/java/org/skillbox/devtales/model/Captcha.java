package org.skillbox.devtales.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NonNull;

@Data
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
