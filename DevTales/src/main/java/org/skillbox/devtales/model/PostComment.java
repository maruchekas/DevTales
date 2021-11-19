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

@Data
@Entity
@Table(name = "post_comments")
public class PostComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private LocalDateTime time;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String text;

  @ManyToOne(optional = true)
  private PostComment parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private List<PostComment> postComments;

  @ManyToOne(optional = false)
  private User user;

  @ManyToOne(optional = false)
  private Post post;

}
