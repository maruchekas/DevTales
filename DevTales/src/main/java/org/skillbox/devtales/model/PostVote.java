package org.skillbox.devtales.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post_votes")
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private LocalDateTime time;

    @Column(columnDefinition = "TINYINT")
    @NonNull
    private int value;

    @ManyToOne()
    @NonNull
    private User user;

    @ManyToOne()
    @NonNull
    private Post post;

}
