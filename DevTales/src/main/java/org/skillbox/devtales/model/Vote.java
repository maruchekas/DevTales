package org.skillbox.devtales.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "post_votes")
public class Vote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private Date time;

  @Column(nullable = false, columnDefinition = "TINYINT")
  private int value;

  @ManyToOne(optional = false)
  private User user;

  @ManyToOne(optional = false)
  private Post post;

}
