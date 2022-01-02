package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    String QUERY = "SELECT p FROM Post p " +
            "WHERE p.isActive = 1  AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.dateTime <= current_time()";

    @Query(QUERY)
    Page<Post> findAllActiveAndAcceptedPosts(Pageable pageable);

    @Query("select p " +
            "from Post p " +
            "where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' " +
            "and p.dateTime <= current_time " +
            "order by p.dateTime")
    Page<Post> findRecentPostsSortedByDate(Integer offset, Integer limit, Pageable pageable);

    @Query("select p, sum(pv.value) as num_likes " +
            "from Post p " +
            "join PostVote pv on p = pv.post " +
            "where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' " +
            "and p.dateTime <= current_time " +
            "group by p order by num_likes ")
    Page<Post> findBestPostsSortedByLikeCount(Integer offset, Integer limit, Pageable pageable);



    @Query(QUERY)
    List<Post> findAll();

}
