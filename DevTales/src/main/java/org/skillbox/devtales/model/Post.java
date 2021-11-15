package org.skillbox.devtales.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import org.skillbox.devtales.model.data.ModerationStatus;

@Data
@Entity
@Table(name = "posts")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int Id;
  @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
  private int isActive;
  @Column(nullable = false)
  private String title;
  @Column(nullable = false, columnDefinition = "TEXT")
  private String text;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ModerationStatus moderationStatus;
  @Column(nullable = false)
  private LocalDateTime dateTime;
  private int viewCount;

  @ManyToOne(optional = true)
  private User moderator;

  @ManyToOne(optional = false)
  private User user;

  @OneToMany(mappedBy = "post")
  private List<PostVote> postVotes;

  @ManyToMany
  @JoinTable(name="post_comments",
      joinColumns=@JoinColumn(name="post_id"),
      inverseJoinColumns=@JoinColumn(name="user_id"))
  private List<User> users;

  @ManyToMany
  @JoinTable(name="tag2post",
      joinColumns=@JoinColumn(name="post_id"),
      inverseJoinColumns=@JoinColumn(name="tag_id"))
  private List<Tag> tags;

}
