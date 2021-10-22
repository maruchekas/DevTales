package org.skillbox.devtales.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(nullable = false, columnDefinition = "TINYINT", length = 2)
  private int isModerator;
  @Column(nullable = false)
  private Date regTime;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String email;
  @Column(nullable = false)
  private String password;
  private String code;
  private String photo;

  @OneToMany(mappedBy = "user")
  private List<Post> posts;

  @OneToMany(mappedBy = "user")
  private List<Vote> votes;

  @OneToMany(mappedBy = "user")
  private List<Comment> comments;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getIsModerator() {
    return isModerator;
  }

  public void setIsModerator(int isModerator) {
    this.isModerator = isModerator;
  }

  public Date getRegTime() {
    return regTime;
  }

  public void setRegTime(Date regTime) {
    this.regTime = regTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }
}
