package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Integer> {

    @Query("SELECT count(pv) FROM PostVote pv " +
            "WHERE pv.post.id = :id and pv.value = 1")
    int findCountLikesOfPostById(@Param("id") int postId);

    @Query("SELECT count(pv) FROM PostVote pv " +
            "WHERE pv.post.id = :id and pv.value = -1")
    int findCountDislikesOfPostById(@Param("id") int postId);

    @Query("SELECT count(pv) FROM PostVote pv " +
            "WHERE pv.value = 1")
    int findCountLikes();

    @Query("SELECT count(pv) FROM PostVote pv " +
            "WHERE pv.value = 1 AND pv.user.id = :id")
    int findCountLikesByUser(int id);

    @Query("SELECT count(pv) FROM PostVote pv " +
            "WHERE pv.value = -1")
    int findCountDislikes();

    @Query("SELECT count(pv) FROM PostVote pv " +
            "WHERE pv.value = -1 AND pv.user.id = :id")
    int findCountDislikesByUser(int id);

}
