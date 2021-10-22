package org.skillbox.devtales.model;

import java.sql.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
  @Column(nullable = false, columnDefinition = "TINYINT", length = 2)
  private int isActive;
  @Column(nullable = false)
  private String title;
  @Column(nullable = false)
  private String text;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ModerationStatus moderationStatus;
  @Column(nullable = false)
  private Date dateTime;
  private int viewCount;
  @Column(nullable = true)
  private int moderatorId;

  @ManyToOne(optional = false)
  private User user;

  @OneToMany(mappedBy = "post")
  private List<Vote> votes;

  @OneToMany(mappedBy = "post")
  private List<Comment> comments;

}
