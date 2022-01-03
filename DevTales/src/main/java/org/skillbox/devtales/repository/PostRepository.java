package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    String HEAD_QUERY = "select p " +
            "from Post p " +
            "where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' " +
            "and p.dateTime <= current_time ";

    @Query(HEAD_QUERY)
    Page<Post> findAllActiveAndAcceptedPosts(Pageable pageable);

    @Query(HEAD_QUERY +
            "order by p.dateTime desc ")
    Page<Post> findRecentPostsSortedByDate(Pageable pageable);

    @Query(HEAD_QUERY +
            "order by p.dateTime asc ")
    Page<Post> findNewPostsSortedByDate(Pageable pageable);

    @Query("select p from Post p " +
            "where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' " +
            "and p.dateTime <= current_time " +
            "group by p " +
            "order by p.postVotes.size desc ")
    Page<Post> findBestPostsSortedByLikeCount(Pageable pageable);

    @Query(HEAD_QUERY +
            "group by p order by p.comments.size desc")
    Page<Post> findPopularPostsSortedByCommentsCount(Pageable pageable);

}
