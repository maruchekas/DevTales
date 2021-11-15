package org.skillbox.devtales.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
  private int isModerator;
  @Column(nullable = false)
  private LocalDateTime regTime;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String email;
  @Column(nullable = false)
  private String password;
  private String code;
  private String photo;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Post> posts;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<PostVote> postVotes;

  @ManyToMany
  @JoinTable(name="post_comments",
      joinColumns=@JoinColumn(name="user_id"),
      inverseJoinColumns=@JoinColumn(name="post_id"))
  private List<Post> postList;

}
