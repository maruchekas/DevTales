package org.skillbox.devtales.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tags")
public class Tag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NonNull
  private String name;

//  @ManyToMany
//  @JoinTable(name = "tag2post",
//      joinColumns = @JoinColumn(name = "tag_id"),
//      inverseJoinColumns = @JoinColumn(name = "post_id"))
//  private List<Post> posts;

}
