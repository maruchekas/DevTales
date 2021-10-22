package org.skillbox.devtales.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.skillbox.devtales.model.data.SettingCode;

@Data
@Entity
@Table(name = "global_settings")
public class GlobalSetting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SettingCode code;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String value;

}
