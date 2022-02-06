package org.skillbox.devtales.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "captcha_codes")
public class Captcha {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false, columnDefinition = "DATETIME")
  private Date time;

  @Column(nullable = false, columnDefinition = "TINYTEXT")
  private String code;

  @Column(nullable = false, columnDefinition = "TINYTEXT")
  private String secretCode;

}
