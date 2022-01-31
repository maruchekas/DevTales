package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.Post;
import org.skillbox.devtales.model.PostVote;
import org.skillbox.devtales.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Integer> {

    @Query("SELECT count(pv) FROM PostVote pv " +
            "WHERE pv.post.id = :id and pv.value = 1")
    Optional<Integer> findCountLikesOfPostById(@Param("id") int postId);

    @Query("SELECT count(pv) FROM PostVote pv " +
            "WHERE pv.post.id = :id and pv.value = -1")
    Optional<Integer> findCountDislikesOfPostById(@Param("id") int postId);

    @Query("SELECT count(pv) FROM PostVote pv " +
            "WHERE pv.value = 1")
    Optional<Integer> findCountLikes();

    @Query("SELECT count(pv) FROM PostVote pv " +
            "WHERE pv.value = 1 AND pv.user.id = :id")
    Optional<Integer> findCountLikesByUser(int id);

    @Query("SELECT count(pv) FROM PostVote pv " +
            "WHERE pv.value = -1")
    Optional<Integer> findCountDislikes();

    @Query("SELECT count(pv) FROM PostVote pv " +
            "WHERE pv.value = -1 AND pv.user.id = :id")
    Optional<Integer> findCountDislikesByUser(int id);

    @Query("SELECT pv FROM PostVote pv " +
            "WHERE pv.post = :post AND pv.user = :user")
    Optional<PostVote> findPostVoteByPostAndUser(Post post, User user);

}
