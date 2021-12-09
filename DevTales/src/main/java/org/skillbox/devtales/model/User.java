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

    @OneToMany(mappedBy = "user")
    private List<PostVote> postVotes;

    @OneToMany(mappedBy = "user")
    private List<Post> postList;

}
