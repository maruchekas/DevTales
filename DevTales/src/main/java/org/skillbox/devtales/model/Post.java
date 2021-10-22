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
import org.skillbox.devtales.model.data.ModerationStatus;

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


  public int getId() {
    return Id;
  }

  public void setId(int id) {
    Id = id;
  }

  public int getIsActive() {
    return isActive;
  }

  public void setIsActive(int isActive) {
    this.isActive = isActive;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public ModerationStatus getModerationStatus() {
    return moderationStatus;
  }

  public void setModerationStatus(ModerationStatus moderationStatus) {
    this.moderationStatus = moderationStatus;
  }

  public Date getDateTime() {
    return dateTime;
  }

  public void setDateTime(Date dateTime) {
    this.dateTime = dateTime;
  }

  public int getViewCount() {
    return viewCount;
  }

  public void setViewCount(int viewCount) {
    this.viewCount = viewCount;
  }

  public int getModeratorId() {
    return moderatorId;
  }

  public void setModeratorId(int moderatorId) {
    this.moderatorId = moderatorId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
