package org.skillbox.devtales.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post_votes")
public class PostVote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NonNull
  private LocalDateTime time;

  @Column(columnDefinition = "TINYINT")
  @NonNull
  private int value;

  @ManyToOne()
  @NonNull
  private User user;

  @ManyToOne()
  @NonNull
  private Post post;

}
