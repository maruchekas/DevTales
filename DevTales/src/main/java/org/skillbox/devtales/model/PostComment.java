package org.skillbox.devtales.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
