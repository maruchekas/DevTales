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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(columnDefinition = "TINYINT", length = 1)
  @NonNull
  private int isModerator;

  @NonNull
  private LocalDateTime regTime;

  @NonNull
  private String name, email, password;

  private String code, photo;

//  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//  private List<Post> posts;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<PostVote> postVotes;

//  @ManyToMany
//  @JoinTable(name = "post_comments",
//      joinColumns = @JoinColumn(name = "user_id"),
//      inverseJoinColumns = @JoinColumn(name = "post_id"))
//  private List<Post> postList;

}
