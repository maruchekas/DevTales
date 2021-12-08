package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Integer> {

    @Query(value = "SELECT count(value) FROM post_votes pv " +
            "WHERE pv.post_id = :id and pv.value = 1",
            nativeQuery = true)
    int findCountLikesOfPostById(@Param("id") int postId);

    @Query(value = "SELECT count(value) FROM post_votes pv " +
            "WHERE pv.post_id = :id and pv.value = -1",
            nativeQuery = true)
    int findCountDislikesOfPostById(@Param("id") int postId);

}
