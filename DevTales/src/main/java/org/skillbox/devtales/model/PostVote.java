package org.skillbox.devtales.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "post_votes")
public class PostVote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private LocalDateTime time;

  @Column(nullable = false, columnDefinition = "TINYINT")
  private int value;

  @ManyToOne(optional = false)
  private User user;

  @ManyToOne(optional = false)
  private Post post;

}
