package org.skillbox.devtales.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NonNull;

@Data
@Entity
@Table(name = "post_comments")
public class PostComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NonNull
  private LocalDateTime time;

  @Column(columnDefinition = "TEXT")
  @NonNull
  private String text;

  @ManyToOne(optional = true)
  private PostComment parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private List<PostComment> postComments;

  @ManyToOne()
  @NonNull
  private User user;

  @ManyToOne()
  @NonNull
  private Post post;

}
