package org.skillbox.devtales.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tag2post")
public class TagToPost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

}
