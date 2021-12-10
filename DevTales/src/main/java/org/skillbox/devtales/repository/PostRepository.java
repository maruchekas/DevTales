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

    @Query(QUERY)
    List<Post> findAll();

}
