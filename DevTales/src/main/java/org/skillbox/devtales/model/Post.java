package org.skillbox.devtales.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.skillbox.devtales.model.enums.ModerationStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Accessors(chain = true)
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "TINYINT", length = 1)
    @NonNull
    private int isActive;
    @NonNull
    private String title;
    @Column(columnDefinition = "TEXT")
    @NonNull
    private String text;
    @Enumerated(EnumType.STRING)
    @NonNull
    private ModerationStatus moderationStatus;
    @NonNull
    private LocalDateTime dateTime;
    private int viewCount;

    @ManyToOne(optional = true)
    private User moderator;

    @ManyToOne()
    @NonNull
    private User user;

    @ManyToMany
    @JoinTable(name = "post_comments",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private List<PostComment> comments;

    @ManyToMany
    @JoinTable(name = "post_votes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private List<PostVote> postVotes;

    @ManyToMany
    @JoinTable(name = "tag2post",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "Id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags = new java.util.LinkedHashSet<>();

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Tag> getTags() {
        return tags;
    }

}
