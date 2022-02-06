package org.skillbox.devtales.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Accessors(chain = true)
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
