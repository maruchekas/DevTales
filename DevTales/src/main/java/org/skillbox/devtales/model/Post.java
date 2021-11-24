package org.skillbox.devtales.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.skillbox.devtales.model.data.ModerationStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int Id;
  @Column(columnDefinition = "TINYINT", length = 1)
  @NonNull
  private int isActive;
  @NonNull
  private String title;
  @Column(columnDefinition = "TEXT")
  @NonNull
  private String text;
  @Enumerated(EnumType.STRING)
  @NonNull
  private ModerationStatus moderationStatus;
  @NonNull
  private LocalDateTime dateTime;
  private int viewCount;

  @ManyToOne(optional = true)
  private User moderator;

  @ManyToOne()
  @NonNull
  private User user;

//  @OneToMany(mappedBy = "post")
//  private List<PostVote> postVotes;

//  @OneToMany(mappedBy = "post")
//  private List<PostComment> postComments;

//  @ManyToMany
//  @JoinTable(name="post_comments",
//      joinColumns=@JoinColumn(name="post_id"),
//      inverseJoinColumns=@JoinColumn(name="user_id"))
//  private List<User> users;

//  @ManyToMany
//  @JoinTable(name="tag2post",
//      joinColumns=@JoinColumn(name="post_id"),
//      inverseJoinColumns=@JoinColumn(name="tag_id"))
//  private List<Tag> tags;

}
